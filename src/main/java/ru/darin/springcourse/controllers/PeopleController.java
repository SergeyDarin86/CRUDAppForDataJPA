package ru.darin.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.darin.springcourse.models.Person;
import ru.darin.springcourse.services.ItemsService;
import ru.darin.springcourse.services.PeopleService;
import ru.darin.springcourse.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    private final ItemsService itemsService;

    private final PersonValidator personValidator;

    @Autowired  // данную аннотацию можно опустить, т.к. Spring внедрит нашу зависимость и без нее
    public PeopleController(PeopleService peopleService, ItemsService itemsService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.itemsService = itemsService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        // получаем всех людей из service и передаем на отображение в представление
        model.addAttribute("people", peopleService.allPeople());

        peopleService.findPeopleByPersonNameStartingWith("startingWith");
        peopleService.findPeopleByPersonNameStartingWithAndOrderByAge("start");
        peopleService.show("email");
        itemsService.findAllByPerson(peopleService.allPeople().get(0));

        peopleService.test();
        peopleService.testNPlus1();
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        // получим одного человека по его id из service и передадим на отображение в представление
        model.addAttribute("person", peopleService.show(id));
        peopleService.showWithLoad(id);
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.show(id));
        return "people/edit";
    }

    // чтобы Spring смог читать значение скрытого поля (_method) необходимо использовать фильтр
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

}
