package ru.darin.springcourse.controllers;

//import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.darin.springcourse.dao.PersonDAO;
import ru.darin.springcourse.models.Person;
import ru.darin.springcourse.services.PersonService;
import ru.darin.springcourse.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    private final PersonValidator personValidator;

//    private final PersonService personService;

    @Autowired  // данную аннотацию можно опустить, т.к. Spring внедрит нашу зависимость и без нее
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
//        return personService.getAllPeople(model);

        // получим всех людей из DAO и передадим на отображение в представление
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        // получим одного человека по его id из DAO и передадим на отображение в представление
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    // при переходе по этому адресу будет создан пустой объект класса Person
    // соответственно в model будет добавдяться этот объкт person
//    @GetMapping("/new")
//    public String newPerson(Model model){
//        model.addAttribute("person", new Person());
//        return "people/new";
//    }

    /**
     * предыдущий метод с использованием @MA
     *
     * @param person
     * @return
     */
    @GetMapping("/new")
    public String newPerson(@ModelAttribute Person person) {
        return "people/new";
    }

    // здесь ModelAttribute:
    // 1) создает объект,
    // 2) записывает значения в поля этого объекта,
    // 3) передает в модель
    // после того как пользователь будет добавлен в базу, мы с помощью redirect(особый механизм)
    // сделаем переход на другую страницу (/people)
//    @PostMapping()
//    public String create(@ModelAttribute("person") Person person){
//        personDAO.save(person);
//        return "redirect:/people";
//    }

    // аннотация Valid проверяет валидность значений, которые указаны над полями в нашей сущности
    // если есть ошибка, то она помещается в bindingResult (этот объект всегда должен идти за тем классом,
    // в котором мы поставили аннотацию @Valid)
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        personValidator.validate(person,bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    // чтобы Spring смог читать значение скрытого поля (_method) необходимо использовать фильтр
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
//        return personService.update(person,bindingResult,id);
        System.out.println(person.getId() + " " + person.getEmail() + " || From controller");
        personValidator.validate(person,bindingResult);

        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }

}
