package ru.darin.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.darin.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findPeopleByEmail(String email);

    Optional<Person> findFirstByPersonNameStartingWith(String startingWith);

    List<Person> findPeopleByPersonNameStartingWithOrderByAge(String personName);
}
