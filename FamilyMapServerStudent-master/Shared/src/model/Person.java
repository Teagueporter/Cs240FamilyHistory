package model;

import java.util.Objects;

/**
 * The Person class conatins information of a person
 * Some of the important info of the person are:
 * username who is the base descendant
 * names, gender, parental info, and unique ID
 */

public class Person {
    /**
     * Unique identifier for this person
     */
    private String personID;
    /**
     * Username of user to which this person belongs
     */
    private String associatedUsername;
    /**
     * Person's first name
     */
    private String firstName;
    /**
     * Person's last name
     */
    private String lastName;
    /**
     * Person's gender either m or f
     */
    private String gender;
    /**
     * Person ID of person's father, may be null
     */
    private String fatherID;
    /**
     * Person ID of person's mother, may be null
     */
    private String motherID;
    /**
     * Person ID of person's spouse, may be null
     */
    private String spouseID;

    public Person(){

    }

    /**
     * This is the person Constructor
     * @param personID unique id for each person
     * @param associatedUsername this is the username of the base descendant
     * @param firstName this is the first name of the person
     * @param lastName this is the last name of the person
     * @param gender this is the gender of the person
     * @param fatherID this is the unique ID of the Father, not necessary
     * @param motherID this is the unique ID of the Mother, not necessary
     * @param spouseID this is the unique ID of the spouse, not necessary
     */


    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.personID) && Objects.equals(associatedUsername, person.associatedUsername) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(gender, person.gender) && Objects.equals(fatherID, person.fatherID) && Objects.equals(motherID, person.motherID) && Objects.equals(spouseID, person.spouseID);
    }
}
