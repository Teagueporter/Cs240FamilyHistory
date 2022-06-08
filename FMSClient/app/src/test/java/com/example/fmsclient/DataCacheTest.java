package com.example.fmsclient;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import model.Event;
import model.Person;
import request.RegRequest;
import results.RegisterResults;

class DataCacheTest {
    DataCache dataCache = DataCache.getInstance();
    ServerProxy proxy;
    RegRequest regRequest = new RegRequest();
    RegisterResults registerResults;
    Person person;

    @BeforeEach
    void setUp() {
        proxy = new ServerProxy("localhost", "8080");
        proxy.clear();
        regRequest.setGender("f"); regRequest.setEmail("email"); regRequest.setLastName("Porter"); regRequest.setFirstName("Teague"); regRequest.setUsername("twigs"); regRequest.setPassword("password");
        registerResults = proxy.register(regRequest);
        dataCache.updateCache(registerResults.getAuthtoken(), proxy);
        dataCache.setBasePerson(registerResults.getPersonID());
        person = dataCache.basePerson;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("find baseperson relative")
    void findRelatives() {
        List<String> relatives = dataCache.findRelatives(person.getPersonID());
        //assertTrue(relatives.contains(person.getSpouseID()));
        assertTrue(relatives.contains(person.getMotherID()));
        assertTrue(relatives.contains(person.getFatherID()));
    }
    @Test
    @DisplayName("find Father relative")

    void findRelativesOther() {
        List<String> relatives = dataCache.findRelatives(person.getFatherID());

        assertTrue(relatives.contains(person.getPersonID()));

        person = dataCache.findPerson(person.getFatherID());

        assertTrue(relatives.contains(person.getSpouseID()));
        assertTrue(relatives.contains(person.getMotherID()));
        assertTrue(relatives.contains(person.getFatherID()));
    }
    @Test
    @DisplayName("filter males")
    void filterMale(){
        dataCache.setMaleBool(false);
        Set<String> females = dataCache.setMarkers();

        for(String string : females){
            assertFalse(string.contains("m"));
        }
    }
    @Test
    @DisplayName("filter female")
    void filterFemale(){
        dataCache.setMaleBool(false);
        Set<String> males = dataCache.setMarkers();

        for(String string : males){
            assertFalse(string.contains("f"));
        }
    }
    @Test
    @DisplayName("filter paternal")
    void filterPaternal(){
        dataCache.setPaternalBool(false);
        Set<String> maternal = dataCache.setMarkers();
        assertFalse(maternal.contains(person.getFatherID()));
        person = dataCache.findPerson(person.getFatherID());
        assertFalse(maternal.contains(person.getFatherID()));
    }

    @Test
    @DisplayName("filter maternal")
    void filterMaternal(){
        dataCache.setMaternalBool(false);
        Set<String> paternal = dataCache.setMarkers();
        assertFalse(paternal.contains(person.getMotherID()));
        person = dataCache.findPerson(person.getMotherID());
        assertFalse(paternal.contains(person.getFatherID()));

    }

    @Test
    @DisplayName("sort base person chron")
    void sortChron(){
        List<Event> events = dataCache.personEvents.get(person.getPersonID());
        for(int i = 0; i < events.size() - 1; i ++){
            assertTrue(events.get(i).getYear() < events.get(i + 1).getYear());
        }
    }

    @Test
    @DisplayName("sort all chron")
    void sortAllChron(){
        for(Map.Entry<String, List<Event>> people : dataCache.personEvents.entrySet()){
            List<Event> events = people.getValue();
            for(int i = 0; i < events.size() - 1; i++){
                assertTrue(events.get(i).getYear() < events.get(i + 1).getYear());
            }
        }
    }

//    @Test
//    void searchEventPass(){
//        List<String> returnList = dataCache.filterEvent("birth", dataCache.allEvents);
//        for(String string : returnList){
//            assertFalse(string.contains("birth"));
//        }
//    }
}