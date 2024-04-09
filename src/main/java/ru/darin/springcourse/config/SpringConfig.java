package ru.darin.springcourse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan("ru.darin.springcourse")
@EnableWebMvc
// при компиляции файл database.properties также попадает в папку target/classes/ru
@PropertySource("classpath:hibernate.properties")

//Теперь все наши транзакции начинаются и заканчиваются Spring'ом
// мы говорим spring, что доверяем ему управление нашими транзакциями
@EnableTransactionManagement
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    private final Environment environment;

    @Autowired  // данную аннотацию мы можем и не указывать. Spring внедрит application и без нее
    public SpringConfig(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine());
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        return thymeleafViewResolver;
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
        dataSource.setDriverClassName(environment.getRequiredProperty("hibernate.driver_class"));
        dataSource.setUrl(environment.getRequiredProperty("hibernate.connection.url"));
        dataSource.setUsername(environment.getRequiredProperty("hibernate.connection.username"));
        dataSource.setPassword(environment.getRequiredProperty("hibernate.connection.password"));
        return dataSource;
    }

    // Используем Hibernate вместо JDBCTemplate

    // Данный метод не является бином. Он используется для того, чтобы взять данные из нашего файла конфигурации hibernate.properties
    // далее мы просто возвращаем эти свойства и передаем в нашу фабрику
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.connection.characterEncoding",environment.getRequiredProperty("hibernate.connection.characterEncoding"));
        properties.put("hibernate.connection.CharSet",environment.getRequiredProperty("hibernate.connection.CharSet"));
        properties.put("hibernate.connection.useUnicode",environment.getRequiredProperty("hibernate.connection.useUnicode"));

        return properties;
    }

    // создаем фабрику для работы с сущностями (делает это Spring)
    // указываем пакет, где есть наши сущности, помеченные аннотацией @Entity
    // передаем наш dataSource() и фабрика знает к какой БД обращаться
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("ru.darin.springcourse.models");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    // данный бин говорит Spring, как работать с транзакциями
    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());

        return transactionManager;
    }

}
