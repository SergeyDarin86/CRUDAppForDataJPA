package ru.darin.springcourse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darin.springcourse.models.Item;
import ru.darin.springcourse.models.Person;
import ru.darin.springcourse.repositories.ItemsRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemsService {

    private final ItemsRepository itemsRepository;

    @Autowired
    public ItemsService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    public List<Item> findAllByPerson(Person person){
        log.info("ItemService: start method findAllByPerson(person); person is: {}", person);
        return itemsRepository.findAllByPerson(person);
    }

}
