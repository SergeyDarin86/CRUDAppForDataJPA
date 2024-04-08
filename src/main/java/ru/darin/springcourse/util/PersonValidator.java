package ru.darin.springcourse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.darin.springcourse.dao.PersonDAO;
import ru.darin.springcourse.models.Person;

/**
 * Обычно для каждой сущности создается свой валидатор
 * классы для валидации лежат в папке /util
 */

@Component
public class PersonValidator implements Validator {

    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
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

        // здесь нам нужно посмотреть, есть ли человек с таким же email в БД

        // добавил дополнительное условие для того, чтобы при редактировании страницы пользователя не выходило
        // сообщение об ошибке "Такой email уже используется" - даже если его не трогать
        // получалась ситуация, что Validator смотрел в базу и видел, что email уже есть
        // (хотя он относится к редактируемому на данный момент человеку)

        if (personDAO.show(person.getEmail()).isPresent()) {
            if (personDAO.show(person.getEmail()).get().getId() != person.getId()) {
                errors.rejectValue("email", "", "This email already is used by someone");
            }
        }


    }


}
