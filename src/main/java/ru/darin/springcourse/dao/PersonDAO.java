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

    @Transactional(readOnly = true)
    public Person show(int id) {
        log.info("Hello from Slf4j");
        Session session = factory.getCurrentSession();
//        Person person = session.get(Person.class,id);
//        System.out.println("===================================");
//        System.out.println(person.getPerson_name());
//        System.out.println("===================================");

        return session.get(Person.class, id);
    }

    @Transactional(readOnly = true)
    public Optional<Person> show(String email) {
        Session session = factory.getCurrentSession();
        return session.createQuery("select p from Person p where p.email =:email").setParameter("email",email).getResultList().stream().findAny();
    }

    @Transactional
    public void save(Person person) {
        factory.getCurrentSession().persist(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        Person person = factory.getCurrentSession().get(Person.class,id);
        person.setAddress(updatedPerson.getAddress());
        person.setSurname(updatedPerson.getSurname());
        person.setPerson_name(updatedPerson.getPerson_name());
        person.setEmail(updatedPerson.getEmail());
        person.setAge(updatedPerson.getAge());
        //
        System.out.println("===============UpdATE====================");
        System.out.println("Person from DB " + person.getPerson_name() + ", Updated person" + updatedPerson.getPerson_name());
        System.out.println("================Update===================");
        factory.getCurrentSession().update(person);
    }

    @Transactional
    public void delete(int id) {
        factory.getCurrentSession().remove(factory.getCurrentSession().get(Person.class,id));
    }

}
