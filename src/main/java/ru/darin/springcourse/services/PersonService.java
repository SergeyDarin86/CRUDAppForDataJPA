package ru.darin.springcourse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darin.springcourse.models.Person;
import ru.darin.springcourse.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PeopleRepository repository;

    @Autowired
    public PersonService(PeopleRepository repository) {
        this.repository = repository;
    }

    public List<Person> allPeople(){
        log.info("PeopleService: start method allPeople()");
        return repository.findAll();
    }

    public Person show(Integer id){
        log.info("PeopleService: start method show(personId); personId is: {}",id);
        return repository.findById(id).orElse(null);
    }

    public Optional<Person> show(String email){
        log.info("PeopleService: start method show(email); email is: {}",email);
        return repository.findPeopleByEmail(email);
    }

    @Transactional
    public void save(Person person){
        log.info("PeopleService: start method save(person); personName is: {}; personAge is: {}",person.getPersonName(),person.getAge());
        repository.saveAndFlush(person);
    }

    @Transactional
    public void delete(Integer id){
        log.info("PeopleService: start method delete(personId); personId is: {}",id);
        repository.deleteById(id);
    }

    @Transactional
    public void update(Integer id, Person updatedPerson){
        log.info("PeopleService: start method update(personId, updatedPerson); personId is: {}; updatedPerson is: {}",id, updatedPerson);
        updatedPerson.setId(id);
        repository.saveAndFlush(updatedPerson);
    }

}
