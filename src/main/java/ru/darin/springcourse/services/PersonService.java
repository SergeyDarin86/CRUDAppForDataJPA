package ru.darin.springcourse.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import ru.darin.springcourse.dao.PersonDAO;
import ru.darin.springcourse.models.Person;

import javax.validation.Valid;

//TODO: можно сделать контроллер плоский и вынести всю логику в сервис
@Component
@Service
public class PersonService {

//    jdbc:postgresql://localhost:5432/first_db
    private final PersonDAO personDAO;

    public PersonService(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    public String getAllPeople(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    public String update(Person person, BindingResult bindingResult, int id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person);
        return "redirect:/people";
    }

}
