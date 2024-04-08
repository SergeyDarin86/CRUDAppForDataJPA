package ru.darin.springcourse.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.darin.springcourse.models.Person;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "16s11w86d";

    public List<Person> index() {
        log.info("Hi from Sl4fj");
        // ROW-маппер - это такой объект, который отображает строки из таблицы в нашей сущности
        // каждую строку из таблицы он переведет в объект класса Person
        // мы создали свой ROW-маппер, но можно было этого и не делать
        return jdbcTemplate.query("Select * from Person", new PersonMapper());
    }

    public Person show(int id) {
        log.info("Hello from Slf4j");
        //
        //jdbcTemplate использует по умолчанию preparedStatement, который в свою очередь предотвращает sql-инъекции
        //

        // можно также использовать такой порядок аргументов
//        jdbcTemplate.query("Select * from Person where id=?",new PersonMapper(),id);

        return jdbcTemplate.query("Select * from Person where id=?", new BeanPropertyRowMapper<>(Person.class), new Object[]{id})
                .stream().findAny().orElse(null);
    }

    // перегружаем наш метод show()
    public Optional<Person> show(String email) {
        return jdbcTemplate.query("Select * From Person where email=?", new BeanPropertyRowMapper<>(Person.class), new Object[]{email})
                .stream().findAny();
    }

    public void save(Person person) {
        // если посмотреть на реализацию метода update, то можно увидеть, что там импользуются Varargs
        // это значит, что мы можем принимать любое количество аргументов в качестве второго аргумента (после sql-запроса)
        // и эти аргументы будут рассматриваться как массив
        jdbcTemplate.update("INSERT INTO Person(person_name, surname, email, age, address) VALUES (?,?,?,?,?)",
                person.getPerson_name(), person.getSurname(), person.getEmail(), person.getAge(), person.getAddress());
    }

    public void update(int id, Person updatedPerson) {
        System.out.println(id + " id || " + updatedPerson.getId() + " <-- updatedPerson");
        jdbcTemplate.update("UPDATE Person SET person_name=?, surname=?, email=?, age=?, address=? WHERE id=?",
                updatedPerson.getPerson_name(), updatedPerson.getSurname(), updatedPerson.getEmail(), updatedPerson.getAge(), updatedPerson.getAddress(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    ////////////////////////////
    ///// Тестируем производительность пакетной вставки
    ////////////////////////////

    public void testMultipleUpdate() {
        List<Person> people = create1000People();

        long start = System.currentTimeMillis();

        for (Person person : people) {
//            jdbcTemplate.update("INSERT INTO Person VALUES (?,?,?,?,?)",
//                    person.getId(), person.getPerson_name(), person.getSurname(), person.getEmail(), person.getAge());
            save(person);
        }

        long finish = System.currentTimeMillis();

        System.out.println("Time for multiply batching is: " + (finish - start));
    }

    public void testBatchUpdate() {

        long start = System.currentTimeMillis();

        //BatchPreparedStatementSetter - это анонимный класс/ интерфейс, который мы можем реализовать прямо здесь

        List<Person> people = create1000People();
        jdbcTemplate.batchUpdate("INSERT INTO Person(person_name,surname,email,age) VALUES (?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setString(1, people.get(i).getPerson_name());
                        preparedStatement.setString(2, people.get(i).getSurname());
                        preparedStatement.setString(3, people.get(i).getEmail());
                        preparedStatement.setInt(4, people.get(i).getAge());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });

        long finish = System.currentTimeMillis();
        System.out.println("Time for Batch updating is: " + (finish - start));

    }


    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i, "Name " + i, "Surname", "swd" + i + "@mail.ru", 30, i + " - Avenue Street"));
        }
        return people;
    }


}
