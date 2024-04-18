package ru.darin.springcourse.models;

import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min=2,max = 30, message = "Имя должно содержать от 2 до 30 символов")
    @Column(name = "person_name")
    private String personName;

    @NotEmpty(message = "Фамилия не должна быть пустой")
    @Size(min = 2, max = 30, message = "Фамилия должна содержать от 2 до 30 символов")
    @Column(name = "surname")
    private String surname;

    @NotEmpty(message = "Email не должен быть пустым")
    @Email(message = "Email должен быть валидным")
    @Column(name = "email")
    private String email;

    @Min(value = 0,message = "Возраст должен быть больше 0")
    @Column(name = "age")
    private int age;

    @NotEmpty(message = "Адрес не должен быть пустым")
//    @Pattern(regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{6}", message = "Address should have the next format: Country, City, Postal Code(6 numbers)")
//    @Pattern(regexp = "[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+", message = "The Author should have the format: Surname Name")
    @Column(name = "address")
    private String address;

    //TODO: сделать Spring-валидатор для поля дата (ошибка если формат даты неверный)
    //TODO: попробовать реализовать выбора даты с помощью готового календаря (готовые библиотеки JavaScript)

    @Column(name = "date_of_birth")
    @Temporal(value = TemporalType.DATE)    // должны выбрать тот тип данных, который используется в БД
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Дата рождения не должна быть пустой")
    @Past(message = "Дата рождения должны быть в прошлом")
    private Date dateOfBirth;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(mappedBy = "person")
    @Cascade(value = {
            org.hibernate.annotations.CascadeType.PERSIST,
            org.hibernate.annotations.CascadeType.MERGE})
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Person() {
    }

    public Person(String personName, String surname, String email, int age, String address) {
        this.personName = personName;
        this.surname = surname;
        this.email = email;
        this.age = age;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String person_name) {
        this.personName = person_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return  "personName= " + personName + ", surname= " + surname + ", email= " + email
                + ", age= " + age + ", address= " + address + ", dateOfBirth= " + dateOfBirth;
    }
}
