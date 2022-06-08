package services;

import com.google.gson.Gson;
import dao.*;
import model.*;
import results.FillResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Populates the server's database with generated data for the specified username.
 * The required "username" parameter must be a user already registered with the server.
 * If there is any data in the database already associated with the given username, it is deleted.
 * The optional "generations" parameter lets the caller specify the number of generations of ancestors to be generated,
 * and must be a non-negative integer (the default is 4, which results in 31 new persons each with associated events).
 * */

public class FillService {

    ArrayList<String> fnames;
    ArrayList<Location> locations;
    ArrayList<String> mnames;
    ArrayList<String> snames;
    int genCounter = 0;
    int numGen = 0;
    int numPersons = 1;
    int numEvents = 0;
    int startYear = 2000;

    public FillResult fill(String username, int generations){
        DatabaseManager db = new DatabaseManager();
        numGen = generations;
        genCounter = generations;

        try {
            if (generations < 0) {
                return new FillResult("Error: invalid number of generations", false);
            }
            //populate all of the arrays
            populateFnames();
            populateMnames();
            populateSnames();
            populateLocations();

            //open data base and find a user based on the username given
            db.openDB();
            UserDao uDao = new UserDao(db.getConnection());
            PersonDao pDao = new PersonDao(db.getConnection());
            User rootUser = uDao.find(username);
            boolean found = pDao.findPerson(rootUser.getPersonID());

            
            Person basePerson = null;
            if(!found) {
                db.closeDB(true);
                basePerson = makeBasePerson(rootUser);
                makeEvents("Events", startYear, basePerson);
            }else{
                basePerson = pDao.find(rootUser.getPersonID());
                db.closeDB(true);
                makeEvents("Events", startYear, basePerson);
            }



            Person[] firstParents = createParents(basePerson, numGen - 1);


            basePerson.setMotherID(firstParents[0].getPersonID());
            basePerson.setFatherID(firstParents[1].getPersonID());

            db.openDB();
            PersonDao pDao2 = new PersonDao(db.getConnection());
            if (!found) {
                pDao2.insert(basePerson);
            }
            db.closeDB(true);

            System.out.println("Test");


        }catch (DataAccessException e){
            db.closeDB(false);
            e.printStackTrace();
            return new FillResult("Error: failed to fill generations", false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            db.closeDB(false);
            return new FillResult("Error: failed to find file", false);
        }
        return new FillResult("Successfully added " + numPersons + " persons and " + numEvents + " events to the database.", true);
    }

    public Person makeBasePerson(User user){
        Person person = new Person();

        person.setAssociatedUsername(user.getUsername());
        person.setGender(user.getGender());
        person.setPersonID(user.getPersonID());
        person.setFirstName(user.getFirstName());
        person.setLastName(user.getLastName());

        return person;
    }

    public Person[] createParents(Person currentPerson, int currGen) throws DataAccessException {
        Person dad = null;
        Person mom = null;
        if (currGen >= 0) {
            Random random = new Random();
            mom = new Person(UUID.randomUUID().toString(), currentPerson.getAssociatedUsername(), fnames.get(random.nextInt(fnames.size())),
                    snames.get(random.nextInt(snames.size())), "f", null, null, null);
            dad = new Person(UUID.randomUUID().toString(), currentPerson.getAssociatedUsername(), mnames.get(random.nextInt(mnames.size())),
                    snames.get(random.nextInt(snames.size())), "m", null, null, null);

            mom.setSpouseID(dad.getPersonID());
            dad.setSpouseID(mom.getPersonID());
            mom.setLastName(dad.getLastName());

            currentPerson.setMotherID(mom.getPersonID());
            currentPerson.setFatherID(dad.getPersonID());

            Event mar = makeEvents("events", startYear - (25 * (genCounter - currGen)), mom);
            makeDEvents(mar, startYear - (25 * (genCounter - currGen)), dad);

            createParents(mom, currGen - 1);
            createParents(dad, currGen - 1);


            DatabaseManager db = new DatabaseManager();
            PersonDao pDao = new PersonDao(db.getConnection());
            pDao.insert(mom);
            pDao.insert(dad);
            numPersons += 2;
            db.closeDB(true);
        }
        return new Person[]{mom, dad};
    }


    public void makeDEvents(Event event, int year, Person person) throws DataAccessException{
        numEvents +=3;
        Random random = new Random();
        int bindex = random.nextInt(locations.size());
        int dindex = random.nextInt(locations.size());

        event.setEventID(UUID.randomUUID().toString());
        event.setPersonID(person.getPersonID());

        Event birth = new Event(UUID.randomUUID().toString(), person.getAssociatedUsername(), person.getPersonID(),
                locations.get(bindex).getLatitude(), locations.get(bindex).getLongitude(),
                locations.get(bindex).getCountry(), locations.get(bindex).getCity(), "Birth", year);

        Event death = new Event(UUID.randomUUID().toString(), person.getAssociatedUsername(), person.getPersonID(),
                locations.get(dindex).getLatitude(), locations.get(dindex).getLongitude(),
                locations.get(dindex).getCountry(), locations.get(dindex).getCity(), "Death", year + 70);

        DatabaseManager db2 = new DatabaseManager();
        EventDao eDao2 = new EventDao(db2.getConnection());
        eDao2.insert(birth);
        eDao2.insert(event);
        eDao2.insert(death);
        db2.closeDB(true);
    }
    public Event makeEvents(String event, int year, Person person) throws DataAccessException {
        numEvents +=3;
        Random random = new Random();
        int bindex = random.nextInt(locations.size());
        int mindex = random.nextInt(locations.size());
        int dindex = random.nextInt(locations.size());


        Event marriage = new Event(UUID.randomUUID().toString(), person.getAssociatedUsername(), person.getPersonID(),
                locations.get(mindex).getLatitude(), locations.get(mindex).getLongitude(),
                locations.get(mindex).getCountry(), locations.get(mindex).getCity(), "Marriage", year + 20);

        Event birth = new Event(UUID.randomUUID().toString(), person.getAssociatedUsername(), person.getPersonID(),
                    locations.get(bindex).getLatitude(), locations.get(bindex).getLongitude(),
                locations.get(bindex).getCountry(), locations.get(bindex).getCity(), "Birth", year);

        Event death = new Event(UUID.randomUUID().toString(), person.getAssociatedUsername(), person.getPersonID(),
                locations.get(dindex).getLatitude(), locations.get(dindex).getLongitude(),
                locations.get(dindex).getCountry(), locations.get(dindex).getCity(), "Death", year + 70);

        DatabaseManager db2 = new DatabaseManager();
        EventDao eDao2 = new EventDao(db2.getConnection());
        eDao2.insert(birth);
        eDao2.insert(marriage);
        eDao2.insert(death);
        db2.closeDB(true);

        return marriage;
    }

    public void populateFnames() throws FileNotFoundException {
        Gson gson = new Gson();
        File file = new File("json/fnames.json");
        FileReader fr = new FileReader(file);
        fnames = gson.fromJson(fr, Data.class).getData();
    }
    public void populateSnames() throws FileNotFoundException {
        Gson gson = new Gson();
        File file = new File("json/snames.json");
        FileReader fr = new FileReader(file);
        snames = gson.fromJson(fr, Data.class).getData();
    }
    public void populateMnames() throws FileNotFoundException {
        Gson gson = new Gson();
        File file = new File("json/mnames.json");
        FileReader fr = new FileReader(file);
        mnames = gson.fromJson(fr, Data.class).getData();
    }
    public void populateLocations() throws FileNotFoundException {
        Gson gson = new Gson();
        File file = new File("json/locations.json");
        FileReader fr = new FileReader(file);
        locations = gson.fromJson(fr, Locations.class).getData();
    }
}
