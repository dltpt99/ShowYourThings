package anu.capstone.showyourthings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
public class ShowYourThingsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShowYourThingsApplication.class, args);
    }

    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/classes/templates");
        resolver.setSuffix(".html");
        return resolver;
    }

}
