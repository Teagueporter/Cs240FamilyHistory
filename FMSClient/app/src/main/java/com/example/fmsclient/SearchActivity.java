package com.example.fmsclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int PEOPLE_LIST = 0;
    private static final int EVENT_LIST = 1;
    DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Set<String> people = dataCache.setMarkers();
        List<String> eventList = new ArrayList<>();
        List<String > peopleList = dataCache.relatives;

        for(String person : peopleList){
            for(Event event : dataCache.personEvents.get(person)){
                eventList.add(event.getEventID());
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        SearchView searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                List<String> personFilter = filterPeople(s, peopleList);
                List<String> eventFilter = filterEvent(s, eventList);

                SearchAdapter adapter = new SearchAdapter(eventFilter, personFilter);
                recyclerView.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<String> personFilter = filterPeople(s, peopleList);
                List<String> eventFilter = filterEvent(s, eventList);

                SearchAdapter adapter = new SearchAdapter(eventFilter, personFilter);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });

    }

    private List<String> filterEvent(String s, List<String> eventList) {
        List<String> returnList = new ArrayList<>();

        for(String string : eventList){
            Event event = dataCache.events.get(string);
            String year = String.valueOf(event.getYear());
            if(event.getCountry().toLowerCase().contains(s) || event.getCity().toLowerCase().contains(s) ||
                    event.getEventType().toLowerCase().contains(s) || year.toLowerCase().contains(s) ||
                    event.getCountry().toUpperCase().contains(s) || event.getCity().toUpperCase().contains(s) ||
                    event.getEventType().toUpperCase().contains(s) || year.toUpperCase().contains(s)){
                returnList.add(string);
            }
        }

        return returnList;
    }

    private List<String> filterPeople(String s, List<String> peopleList) {
        List<String> returnList = new ArrayList<>();

        for(String string : peopleList){
            Person person = dataCache.findPerson(string);
            if(person.getFirstName().toLowerCase().contains(s) || person.getFirstName().toUpperCase().contains(s) || person.getLastName().toLowerCase().contains(s) || person.getLastName().toUpperCase().contains(s)){
                returnList.add(string);
            }
        }

        return returnList;
    }


    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final List<String> events;
        private final List<String> relatives;

        SearchAdapter(List<String> events, List<String> relatives) {
            this.events = events;
            this.relatives = relatives;
        }

        @Override
        //TODO you might need to switch these around
        public int getItemViewType(int position) {
            return position < relatives.size() ? PEOPLE_LIST : EVENT_LIST;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PEOPLE_LIST){
                view = getLayoutInflater().inflate(R.layout.relative_layout, parent, false);
            }
            else{
                view = getLayoutInflater().inflate(R.layout.event_layout, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < relatives.size()){
                holder.bind(relatives.get(position), "relative");
            }
            else{
                holder.bind(events.get(position - relatives.size()), "event");
            }
        }

        @Override
        public int getItemCount() {
            return events.size() + relatives.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView relativeInfo;
        private final TextView EventInfo;
        private final ImageView relativeIcon;
        private final ImageView eventIcon;

        String personID, eventID;

        private final int viewType;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PEOPLE_LIST){
                relativeInfo = itemView.findViewById(R.id.relativeText);
                relativeIcon = itemView.findViewById(R.id.relativeImage);
                EventInfo = null;
                eventIcon = null;
            }
            else{
                relativeInfo = null;
                relativeIcon = null;
                EventInfo = itemView.findViewById(R.id.eventText);
                eventIcon = itemView.findViewById(R.id.eventImage);
            }

        }

        private void bind(String info, String type){
            if(type.equals("relative")){
                personID = info;
                Person person = dataCache.findPerson(info);
                String text = person.getFirstName() + " " + person.getLastName();
                relativeInfo.setText(text);

                if(person.getGender().equals("f")){
                    Drawable androidIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).
                            colorRes(R.color.pink).sizeDp(20);
                    relativeIcon.setImageDrawable(androidIcon);
                }
                else{
                    Drawable androidIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).
                            colorRes(R.color.blue).sizeDp(20);
                    relativeIcon.setImageDrawable(androidIcon);
                }
            }
            else{
                eventID = info;
                Event event = dataCache.events.get(info);
                Person person = dataCache.findPerson(event.getPersonID());

                String text = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")\n\n " + person.getFirstName() + " " + person.getLastName();
                EventInfo.setText(text);
                Drawable map = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).
                        colorRes(R.color.blue).sizeDp(20);
                eventIcon.setImageDrawable(map);
            }
        }


        @Override
        public void onClick(View view) {
            if(viewType == PEOPLE_LIST){
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("personID", personID);
                startActivity(intent);
                //Toast.makeText(SearchActivity.this, "this is a person", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra("eventID", eventID);
                startActivity(intent);
                //Toast.makeText(SearchActivity.this, "this is an event", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}