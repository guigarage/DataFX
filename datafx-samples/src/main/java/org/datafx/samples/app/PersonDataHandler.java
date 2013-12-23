package org.datafx.samples.app;

import org.datafx.controller.injection.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PersonDataHandler {

    public List<Person> loadAllPersons() {
        List<Person> persons = new ArrayList<Person>();

        Person johan = new Person();
        johan.setName("Johan Vos");
        johan.setNotes("Johan is CTO at LodgON, a Java Champion, a member of the BeJUG steering group, the Devoxx steering group and he is a JCP member.");
        persons.add(johan);

        Person jonathan = new Person();
        jonathan.setName("Jonathan Giles");
        jonathan.setNotes("Jonathan Giles is the JavaFX UI controls technical lead at Oracle, where he has been involved with JavaFX since 2009.");
        persons.add(jonathan);

        Person hendrik = new Person();
        hendrik.setName("Hendrik Ebbers");
        hendrik.setNotes("Hendrik Ebbers is Senior Java Architect at Materna GmbH in Dortmund, Germany.");
        persons.add(hendrik);

        return persons;
    }
}
