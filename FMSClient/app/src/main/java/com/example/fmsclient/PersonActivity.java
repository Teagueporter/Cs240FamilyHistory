package com.example.fmsclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ipsec.ike.ChildSaProposal;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import model.Person;

public class PersonActivity extends AppCompatActivity {

    DataCache dataCache = DataCache.getInstance();
    TextView firstName, lastName, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Bundle bundleInfo = getIntent().getExtras();
        String personID = bundleInfo.getString("personID");

        ExpandableListView expandableListView = findViewById(R.id.personActivity);

        List<model.Event> events = dataCache.personEvents.get(personID);
        List<String> relatives = dataCache.findRelatives(personID);

        personAdapter personAdapter = new personAdapter(events, relatives);

        expandableListView.setAdapter(personAdapter);

        firstName = findViewById(R.id.personName);
        lastName = findViewById(R.id.personLastName);
        gender = findViewById(R.id.personGender);

        firstName.setText(dataCache.findPerson(personID).getFirstName());
        lastName.setText(dataCache.findPerson(personID).getLastName());
        gender.setText(dataCache.findPerson(personID).getGender());

    }

    private class personAdapter extends BaseExpandableListAdapter {

        private static final int PERSONS_LIST = 1;
        private static final int EVENT_LIST = 0;


        private List<model.Event> events = new ArrayList<>();
        private List<String> relatives = new ArrayList<>();
        private List<String> order = new ArrayList<>();

        personAdapter(List<model.Event> events, List<String> relatives) {
            order.add("Mother"); order.add("Father"); order.add("Spouse"); order.add("Child");
            this.events = events;
            this.relatives = relatives;
        }


        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int i) {
            switch (i) {
                case PERSONS_LIST:
                    return relatives.size();
                case EVENT_LIST:
                    return events.size();
                default:
                    throw new IllegalArgumentException("Unrecognized: " + i);
            }
        }

        @Override
        public Object getGroup(int i) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_LIST:
                    return events.get(childPosition);
                case PERSONS_LIST:
                    return relatives.get(childPosition);
            }
            return null;
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_title, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.title_template);

            switch (groupPosition) {
                case EVENT_LIST:
                    titleView.setText(R.string.eventTile);
                    break;
                case PERSONS_LIST:
                    titleView.setText(R.string.relativeTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch (groupPosition) {
                case EVENT_LIST:
                    itemView = getLayoutInflater().inflate(R.layout.event_layout, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSONS_LIST:
                    itemView = getLayoutInflater().inflate(R.layout.relative_layout, parent, false);
                    initializeRelativeView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View eventItem, final int childPosition) {
            TextView text = eventItem.findViewById(R.id.eventText);
            ImageView image = eventItem.findViewById(R.id.eventImage);
            String info = events.get(childPosition).getEventType() +
                    " " + events.get(childPosition).getCity() +
                    ", " + events.get(childPosition).getCountry() +
                    " (" + events.get(childPosition).getYear() + ")";
            text.setText(info);

            Drawable map = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.blue).sizeDp(20);
            image.setImageDrawable(map);


            eventItem.setOnClickListener(view -> {
                Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                intent.putExtra("eventID", events.get(childPosition).getEventID());
                startActivity(intent);
            });

        }

        private void initializeRelativeView(View relativeItem, final int childPosition) {
            TextView text = relativeItem.findViewById(R.id.relativeText);
            ImageView image = relativeItem.findViewById(R.id.relativeImage);
            Person person = dataCache.findPerson(relatives.get(childPosition));
            String info = order.get(childPosition) + ": " + person.getFirstName() + " " + person.getLastName();
            text.setText(info);
            if(person.getGender().equals("f")){
                Drawable androidIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).
                        colorRes(R.color.pink).sizeDp(20);
                image.setImageDrawable(androidIcon);
            }
            else{
                Drawable androidIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.blue).sizeDp(20);
                image.setImageDrawable(androidIcon);
            }

            relativeItem.setOnClickListener(view -> {
                Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                intent.putExtra("personID", person.getPersonID());
                startActivity(intent);
            });
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
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