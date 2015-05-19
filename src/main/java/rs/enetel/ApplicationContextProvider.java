package rs.enetel;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        context = ctx;
    }
}

