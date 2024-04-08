package ru.darin.springcourse.config;

import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class MySpringMVCDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    // здесь мы подставляем наш класс SpringConfig, где задана наша конфигурация
    // теперь spring знает, где лежит наша конфигурация
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    // эквивалент параметру из файла web.xml
    // означает, что все запросы от пользователя мы отправляем на DispatcherServlet
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // методы для работы фильтра при Patch и Delete запросах
    // данный метод запускается при старте Spring-приложения
    // при использовании SpringBoot эта конфигурация будет занимать всего одну строку
    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }

    // здесь в приватном методе мы добавляем фильтр к нашему приложению
    // этот фильтр как раз и смотрит на значение скрытого метода (_method) - фильр уже встроен в Spring
    // и уже отправляет на нужный метод контроллера
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null ,true, "/*");
    }

}
