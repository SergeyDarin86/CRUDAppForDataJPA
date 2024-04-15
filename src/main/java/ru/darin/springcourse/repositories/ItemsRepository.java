package ru.darin.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.darin.springcourse.models.Item;
import ru.darin.springcourse.models.Person;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Item,Integer> {
    List<Item> findAllByPerson(Person person);
}
