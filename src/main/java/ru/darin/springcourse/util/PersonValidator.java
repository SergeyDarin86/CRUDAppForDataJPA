package ru.darin.springcourse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.darin.springcourse.models.Person;
import ru.darin.springcourse.services.PersonService;

/**
 * Обычно для каждой сущности создается свой валидатор
 * классы для валидации лежат в папке /util
 */

@Component
public class PersonValidator implements Validator {

    private final PersonService service;

    @Autowired
    public PersonValidator(PersonService service) {
        this.service = service;
    }

    // здесь нам необходимо дать понять Spring, к какому классу относится наша валидация
    // на объектах какого класса можно использовать данную валидацию
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        System.out.println((person == null) + " - person is null");

        if (service.show(person.getEmail()).isPresent()) {
            if (service.show(person.getEmail()).get().getId() != person.getId()) {
                errors.rejectValue("email", "", "This email already is used by someone");
            }
        }

    }

}
