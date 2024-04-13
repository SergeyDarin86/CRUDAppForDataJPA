package ru.darin.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darin.springcourse.models.Person;
import ru.darin.springcourse.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PeopleRepository repository;

    @Autowired
    public PersonService(PeopleRepository repository) {
        this.repository = repository;
    }

    public List<Person> allPeople(){
        return repository.findAll();
    }

    public Person show(Integer id){
        return repository.findById(id).orElse(null);
    }

    public Optional<Person> show(String email){
        return repository.findPeopleByEmail(email);
    }

    @Transactional
    public void save(Person person){
        repository.saveAndFlush(person);
    }

    @Transactional
    public void delete(Integer id){
        repository.deleteById(id);
    }

    @Transactional
    public void update(Integer id, Person updatedPerson){
        updatedPerson.setId(id);
        repository.saveAndFlush(updatedPerson);
    }

}
