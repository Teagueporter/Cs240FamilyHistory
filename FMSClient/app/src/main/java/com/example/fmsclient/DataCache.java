package com.example.fmsclient;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import model.Event;
import model.Person;

public class DataCache {
    private static DataCache instance = new DataCache();

    private DataCache() {
    }

    public static DataCache getInstance() {
        return instance;
    }

    //start hue
    int hue = 0;

    //settings booleans
    Boolean paternalBool = true;
    Boolean maternalBool = true;
    Boolean familyBool = false;
    Boolean storyBool = false;
    Boolean maleBool = true;
    Boolean femaleBool = true;
    Boolean spouseBool = false;
    Boolean settingsChanged = false;

    //base person
    Person basePerson;

    //maps for sorting
    Map<String, Person> people = new HashMap<>();
    Map<String, Event> events = new HashMap<>();
    Map<String, List<Event>> personEvents = new HashMap<>(); //TODO check if they are sorted
    Map<String, Integer> eventColors = new HashMap<>();

    //sets for ancestors
    Set<String> paternalAncestors = new TreeSet<>();
    Set<String> maternalAncestors = new TreeSet<>();

    //list for testing aall events
    List<String> allEvents = new ArrayList<>();

    //sets to sort into genter and sie
    Set<String> pmaleEvents = new TreeSet<>();
    Set<String> mmaleEvents = new TreeSet<>();
    Set<String> pfemaleEvents = new TreeSet<>();
    Set<String> mfemaleEvents = new TreeSet<>();
    List<String> relatives = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateCache(String authToken, ServerProxy proxy) {

        //clearing all the maps and sets
        people.clear();
        events.clear();
        personEvents.clear();
        eventColors.clear();
        paternalAncestors.clear();
        maternalAncestors.clear();
        pmaleEvents.clear();
        mmaleEvents.clear();
        pfemaleEvents.clear();
        mfemaleEvents.clear();

        //getting the info from the database
        Person[] personArray = proxy.getPeople(authToken).getData();
        Event[] eventArray = proxy.getEvents(authToken).getData();

        //populating people map
        for (Person person : personArray) {
            people.put(person.getPersonID(), person);
            relatives.add(person.getPersonID());
        }

        //populating events map
        for (Event value : eventArray) {
            events.put(value.getEventID(), value);
            allEvents.add(value.getEventType());
            if (!eventColors.containsKey(value.getEventType())) {
                eventColors.put(value.getEventType().toUpperCase(), 0);
            }
        }

        //giving events colors
        for (Map.Entry<String, Integer> eventMap : eventColors.entrySet()) {
            eventColors.put(eventMap.getKey(), hue);
            hue += 360 / eventColors.size();
        }

        //sorting events to each person
        for (Event event : eventArray) {
            if (personEvents.containsKey(event.getPersonID())) {
                Objects.requireNonNull(personEvents.get(event.getPersonID())).add(event);
            } else {
                List<Event> events = new ArrayList<>();
                events.add(event);
                personEvents.put(event.getPersonID(), events);
            }
        }

        //sorting all the events in chron order
        for (Map.Entry<String, List<Event>> events : personEvents.entrySet()) {
            events.getValue().sort(Comparator.comparing(Event::getYear));
        }


    }

    //recursive function to get all of the ancestors from one side
    public void addParents(String currPerson, Set<String> parentSet, Set<String> males, Set<String> females) {
        if (currPerson == null) {
            return;
        }

        parentSet.add(currPerson);
        Person currentPerson = people.get(currPerson);

        if (currentPerson.getGender().equals("f")) {
            females.add(currPerson);
        } else {
            males.add(currPerson);
        }
        if (currentPerson.getFatherID() != null) {
            males.add(currentPerson.getFatherID());
            addParents(currentPerson.getFatherID(), parentSet, males, females);
        }
        if (currentPerson.getMotherID() != null) {
            females.add(currentPerson.getMotherID());
            addParents(currentPerson.getMotherID(), parentSet, males, females);
        }
    }

    //seting all the markers
    public Set<String> setMarkers() {
        Set<String> displayMarkers = new TreeSet<>();
        if (paternalBool) {
            if (maleBool) {
                displayMarkers.addAll(pmaleEvents);
            }
            if (femaleBool) displayMarkers.addAll(pfemaleEvents);
        }
        if (maternalBool) {
            if (maleBool) {
                displayMarkers.addAll(mmaleEvents);
            }
            if (femaleBool) displayMarkers.addAll(mfemaleEvents);
        }
        return displayMarkers;
    }

    //finding a person
    public Person findPerson(String personID) {
        Person returnPerson = null;
        for (Map.Entry<String, Event> value : events.entrySet()) {
            if (!personID.equals(value.getValue().getPersonID())) {
            } else {
                returnPerson = people.get(personID);
            }
        }
        return returnPerson;
    }

    //finding all the relatives of a person
    public List<String> findRelatives(String personID) {
        //Map<String, String> relatives = new HashMap<>();
        List<String> relatives = new ArrayList<>();
        Person basePerson = findPerson(personID);

        if (basePerson.getFatherID() != null) {

            relatives.add(basePerson.getMotherID());
        }
        if (basePerson.getMotherID() != null) {
            relatives.add(basePerson.getFatherID());
        }
        if (basePerson.getSpouseID() != null) {
            relatives.add(basePerson.getSpouseID());
        }


        //find child
        for (Map.Entry<String, Person> person : people.entrySet()) {

            if (person.getValue().getFatherID() != null) {
                if (person.getValue().getFatherID().equals(personID)) {
                    relatives.add(person.getKey());
                }
            }
            if (person.getValue().getMotherID() != null) {
                if (person.getValue().getMotherID().equals(personID)) {
                    relatives.add(person.getKey());
                }
            }
        }

        return relatives;
    }

    //filters events
    public List<String> filterEvent(String s, List<String> eventList) {
        List<String> returnList = new ArrayList<>();

        for (String string : eventList) {
            Event event = events.get(string);
            String year = String.valueOf(event.getYear());
            if (event.getCountry().toLowerCase().contains(s) || event.getCity().toLowerCase().contains(s) ||
                    event.getEventType().toLowerCase().contains(s) || year.toLowerCase().contains(s) ||
                    event.getCountry().toUpperCase().contains(s) || event.getCity().toUpperCase().contains(s) ||
                    event.getEventType().toUpperCase().contains(s) || year.toUpperCase().contains(s)) {
                returnList.add(string);
            }
        }

        return returnList;
    }

    //filters people
    public List<String> filterPeople(String s, List<String> peopleList) {
        List<String> returnList = new ArrayList<>();
        for (String string : peopleList) {
            Person person = findPerson(string);
            if (person.getFirstName().toLowerCase().contains(s) || person.getFirstName().toUpperCase().contains(s) || person.getLastName().toLowerCase().contains(s) || person.getLastName().toUpperCase().contains(s)) {
                returnList.add(string);
            }
        }

        return returnList;
    }

    public void setBasePerson(String personID) {
        settingsChanged = true;
        this.basePerson = people.get(personID)
        ;
    }

    public void setPaternalBool(Boolean paternalBool) {
        settingsChanged = true;
        this.paternalBool = paternalBool;
    }

    public void setMaternalBool(Boolean maternalBool) {
        settingsChanged = true;
        this.maternalBool = maternalBool;
    }

    public void setFamilyBool(Boolean familyBool) {
        settingsChanged = true;
        this.familyBool = familyBool;
    }

    public void setStoryBool(Boolean storyBool) {
        settingsChanged = true;
        this.storyBool = storyBool;
    }

    public void setMaleBool(Boolean maleBool) {
        settingsChanged = true;
        this.maleBool = maleBool;
    }

    public void setFemaleBool(Boolean femaleBool) {
        settingsChanged = true;
        this.femaleBool = femaleBool;
    }

    public void setSpouseBool(Boolean spouseBool) {
        settingsChanged = true;
        this.spouseBool = spouseBool;
    }
}
