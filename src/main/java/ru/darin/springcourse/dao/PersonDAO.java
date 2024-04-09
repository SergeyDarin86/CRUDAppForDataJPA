package ru.darin.springcourse.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.darin.springcourse.models.Person;

import java.util.List;
import java.util.Optional;


@Slf4j
@Component
public class PersonDAO {

    private final SessionFactory factory;

    @Autowired
    public PersonDAO(SessionFactory factory) {
        this.factory = factory;
    }

    @Transactional(readOnly = true)
    public List<Person> index() {
        log.info("Hi from Sl4fj");
        Session session = factory.getCurrentSession();

        return session.createQuery("select p from Person p", Person.class).getResultList();
    }

    public Person show(int id) {
        log.info("Hello from Slf4j");
        return null;
    }

    public Optional<Person> show(String email) {
        return null;
    }

    public void save(Person person) {

    }

    public void update(int id, Person updatedPerson) {
        System.out.println(id + " id || " + updatedPerson.getId() + " <-- updatedPerson");
    }

    public void delete(int id) {

    }

}
