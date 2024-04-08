package ru.darin.springcourse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@ComponentScan("ru.darin.springcourse")
@EnableWebMvc
// при компиляции файл database.properties также попадает в папку target/classes/ru
@PropertySource("classpath:database.properties")
//@PropertySources(value = {@PropertySource(""),@PropertySource("")})
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    private final Environment environment;

    @Autowired  // данную аннотацию мы можем и не указывать. Spring внедрит application и без нее
    public SpringConfig(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    // как и в файле xml-конфигурации мы задаем параметры для нашего представления
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8"); //TODO: здесь устанавливается кодировка для отображения русских символов
        return templateResolver;
    }

    // также настраиваем наши представления в данном бине
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    // здесь мы задаем свой шаблонизатор (в нашем случае Thymeleaf)
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8"); //TODO: здесь также устанавливается кодировка для отображения русских символов
        registry.viewResolver(resolver);
    }

    // чтобы использовать JDBCTemplate (вместо JDBC API), необходимо создать бин
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/first_db");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("16s11w86d");

//        dataSource.setDriverClassName(environment.getProperty("driver")); // можно было бы сделать таким образом
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("driver"))); // используем этот способ, т.к. Driver может быть null и мы по ошибке сможем это понять
        dataSource.setUrl(environment.getProperty("url"));
        dataSource.setUsername(environment.getProperty("user_name"));
        dataSource.setPassword(environment.getProperty("password"));

        //
//        environment.getProperty("logging.level.org.springframework");
//        environment.getProperty("logging.file.name");
//        environment.getProperty("logging.file.path");
        //
//        dataSource.setLogWriter("");
        return dataSource;
    }

    // внедряем наш источник данных в jdbcTemplate
    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
}
