package ru.darin.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.darin.springcourse.dao.PersonDAO;
import ru.darin.springcourse.models.Person;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PersonDAO personDAO;

    @Autowired
    public AdminController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String adminPage(Model model, @ModelAttribute("person")Person person){
        model.addAttribute("people", personDAO.index());
        return "adminPage1";
    }

    @PatchMapping("/add")
    public String makeAdmin(@ModelAttribute("person") Person person){
        // здесь будет приходить только одно значение из выпадающего списка, которое мы назначили
        // в adminPage. Остальные поля для этого person будут равны null (Обратить внимание!!!)
        System.out.println(person.getId() + " <-- id from Person for admin");
        return "redirect:/people";
    }







}
