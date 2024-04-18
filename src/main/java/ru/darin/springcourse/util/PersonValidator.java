package ru.darin.springcourse.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.darin.springcourse.models.Person;
import ru.darin.springcourse.services.PeopleService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Обычно для каждой сущности создается свой валидатор
 * классы для валидации лежат в папке /util
 */

@Component
@Slf4j
public class PersonValidator implements Validator {

    private final PeopleService service;

    @Autowired
    public PersonValidator(PeopleService service) {
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
        log.info("PersonValidator: start method validate(person, errors); person is: {}", person);

        if (service.show(person.getEmail()).isPresent()) {
            if (service.show(person.getEmail()).get().getId() != person.getId()) {
                errors.rejectValue("email", "", "Этот email уже кем-то используется");
            }
        }

//        if (){
//            System.out.println(errors.getFieldError());
////            errors.rejectValue("dateOfBirth","","Format");
//        }

        //
//        if (person.getDateOfBirth() == null)
//            errors.rejectValue("dateOfBirth","","Format");
//        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            df.parse(service.getBirthDayOfPerson(person).toString());
//        } catch (ConversionFailedException e) {
//            errors.rejectValue("dateOfBirth","","Format");
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        //

    }

}
