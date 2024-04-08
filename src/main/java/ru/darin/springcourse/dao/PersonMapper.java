package ru.darin.springcourse.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.darin.springcourse.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

// библиотека Spring
// на самом деле свой маппер можно не создавть, а использовать готовый из-под капота new BeanPropertyRowMapper<>(Person.class)
// и указывать в параметрах вместо PersonMapper
public class PersonMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        // Здесь вместо текстовых полей можно указывать индекс поля, по которому происходит маппинг
        // номера полей начинаются с 1

        Person person = new Person();
        person.setPerson_name(resultSet.getString("person_name"));
        person.setSurname(resultSet.getString("surname"));
        person.setEmail(resultSet.getString("email"));
        person.setAge(resultSet.getInt("age"));
        person.setAddress(resultSet.getString("address"));
        person.setId(resultSet.getInt("id"));
        return person;
    }
}
