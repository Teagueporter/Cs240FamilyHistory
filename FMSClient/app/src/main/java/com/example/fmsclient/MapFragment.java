package com.example.fmsclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import model.Event;
import model.Person;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    DataCache dataCache = DataCache.getInstance();
    TextView eventStuff;
    ImageView imageStuff;
    Map<Marker, Event> markerMap = new HashMap<>();
    Event event = null;
    String eventID;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        //setting the event and image view
        eventStuff = view.findViewById(R.id.eventInfo);
        imageStuff = view.findViewById(R.id.genderImage);

        //creating support map frag
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //initializing the map fragment
        map = googleMap;
        map.setOnMapLoadedCallback(this);


        //Make base person birth marker
        Person BASEPERSON = dataCache.basePerson;
        List<Event> events = dataCache.personEvents.get(BASEPERSON.getPersonID());

        //make maternal and paternal sets
        dataCache.addParents(BASEPERSON.getMotherID(), dataCache.maternalAncestors, dataCache.mmaleEvents, dataCache.mfemaleEvents);
        dataCache.addParents(BASEPERSON.getFatherID(), dataCache.paternalAncestors, dataCache.pmaleEvents, dataCache.pfemaleEvents);

        //set default image holder
        Drawable androidIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).
                colorRes(R.color.black).sizeDp(30);
        imageStuff.setImageDrawable(androidIcon);

        //get Bundle
        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle != null){
            Event event = dataCache.events.get(bundle.get("eventID"));
            Person person = dataCache.findPerson(event.getPersonID());
            LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(location));
            if (person.getGender().equals("f")) {
                Drawable gender = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                        colorRes(R.color.pink).sizeDp(30);
                imageStuff.setImageDrawable(gender);
            } else {
                Drawable gender = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                        colorRes(R.color.blue).sizeDp(30);
                imageStuff.setImageDrawable(gender);
            }
            String info = person.getFirstName() + " " + person.getLastName() + "\n\n" +
                    event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + "\n\n" +
                    event.getYear();
            eventStuff.setText(info);
        }
        else {
            LatLng baseBirth = new LatLng(events.get(0).getLatitude(), events.get(0).getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(baseBirth));

        }


        Set<String> markers = dataCache.setMarkers();

        if (dataCache.maleBool && dataCache.femaleBool && dataCache.paternalBool && dataCache.maternalBool) {
            for (Event marker : dataCache.personEvents.get(BASEPERSON.getPersonID())) {
                LatLng location = new LatLng(marker.getLatitude(), marker.getLongitude());
                int hue = dataCache.eventColors.get(marker.getEventType().toUpperCase());
                map.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(hue))).setTag(marker);
            }
            for (Event marker : dataCache.personEvents.get(BASEPERSON.getSpouseID())) {
                LatLng location = new LatLng(marker.getLatitude(), marker.getLongitude());
                int hue = dataCache.eventColors.get(marker.getEventType().toUpperCase());
                map.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(hue))).setTag(marker);
            }
        }

        for (String event : markers) {
            for (Event marker : dataCache.personEvents.get(event)) {
                LatLng location = new LatLng(marker.getLatitude(), marker.getLongitude());
                int hue = dataCache.eventColors.get(marker.getEventType().toUpperCase());
                map.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(hue))).setTag(marker);
            }
        }


        //Listener for the markers

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                event = (Event) marker.getTag();
                assert event != null;
                Person person = dataCache.findPerson(event.getPersonID());
                String info = person.getFirstName() + " " + person.getLastName() + "\n\n" +
                        event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + "\n\n" +
                        event.getYear();
                eventStuff.setText(info);
                if (person.getGender().equals("f")) {
                    Drawable androidIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                            colorRes(R.color.pink).sizeDp(30);
                    imageStuff.setImageDrawable(androidIcon);
                } else {
                    Drawable androidIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                            colorRes(R.color.blue).sizeDp(30);
                    imageStuff.setImageDrawable(androidIcon);
                }
                return true;
            }
        });

        eventStuff.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra("personID", event.getPersonID());
            startActivity(intent);
        });

    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);

        if (getActivity() != null && getActivity().getClass() == MainActivity.class) {
            menuInflater.inflate(R.menu.menu, menu);
        }
    }


}
