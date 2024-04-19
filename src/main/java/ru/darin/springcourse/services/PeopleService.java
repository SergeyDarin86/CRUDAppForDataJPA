package ru.darin.springcourse.services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darin.springcourse.models.Mood;
import ru.darin.springcourse.models.Person;
import ru.darin.springcourse.repositories.PeopleRepository;

import javax.persistence.EntityManager;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    private final EntityManager entityManager;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, EntityManager entityManager) {
        this.peopleRepository = peopleRepository;
        this.entityManager = entityManager;
    }

    public List<Person> allPeople(){
        log.info("PeopleService: start method allPeople()");
        return peopleRepository.findAll();
    }

    public Person show(Integer id){
        log.info("PeopleService: start method show(personId); personId is: {}",id);
        Person person = peopleRepository.findById(id).orElse(null);
        System.out.println(person.getItems());
        return peopleRepository.findById(id).orElse(null);
    }

    public Optional<Person> show(String email){
        log.info("PeopleService: start method show(email); email is: {}",email);
        return peopleRepository.findPeopleByEmail(email);
    }

    @Transactional
    public void save(Person person){
        log.info("PeopleService: start method save(person); personName is: {}; personAge is: {}",person.getPersonName(),person.getAge());
        person.setCreatedAt(new Date());
        person.setMood(Mood.SAD);
        person.setMoodString(Mood.SAD);
        peopleRepository.saveAndFlush(person);
    }

    @Transactional
    public void delete(Integer id){
        log.info("PeopleService: start method delete(personId); personId is: {}",id);
        peopleRepository.deleteById(id);
    }

    @Transactional
    public void update(Integer id, Person updatedPerson){
        log.info("PeopleService: start method update(personId, updatedPerson); personId is: {}; updatedPerson is: {}",id, updatedPerson);
        updatedPerson.setId(id);
        peopleRepository.saveAndFlush(updatedPerson);
    }

    public void findPeopleByPersonNameStartingWith(String startingWith){
        peopleRepository.findFirstByPersonNameStartingWith(startingWith);
    }

    public void findPeopleByPersonNameStartingWithAndOrderByAge(String startingWith){
        peopleRepository.findPeopleByPersonNameStartingWithOrderByAge(startingWith);
    }

    // мы создали этот метод, потому что будем тестровать методы из репозитория с помощью дебаггера
    // чтобы не создавать для каждого метода из репо новый метод здесь в сервере,
    //будет удобно обращаться к ним, находясь в этом методе
    public void test(){
        System.out.println("Testing here with debug. Inside hibernate transaction");
    }

    public void testNPlus1(){
        Session session = entityManager.unwrap(Session.class);
        List<Person>personList = session.createQuery("select p from Person p LEFT JOIN FETCH p.items").getResultList();
        Set<Person> people = new HashSet<>(personList);
        for (Person person: people)
            System.out.println("Person is - " + person.getPersonName() + " has " + person.getItems());
    }

}
