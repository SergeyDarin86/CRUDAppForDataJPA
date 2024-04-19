package ru.darin.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.darin.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findPeopleByEmail(String email);

    Optional<Person> findFirstByPersonNameStartingWith(String startingWith);

    List<Person> findPeopleByPersonNameStartingWithOrderByAge(String personName);

    @Transactional
    @Query(value = "select * from person left outer join item i on person.id = i.person_id", nativeQuery = true)
    List<Person> findAllWithLeftJoin();

}
