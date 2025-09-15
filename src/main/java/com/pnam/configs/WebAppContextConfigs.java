package com.pnam.configs;

import com.pnam.validator.WebAppValidator;
import com.pnam.validator.UserValidator;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.pnam.controllers",
    "com.pnam.repositories",
    "com.pnam.services",
    "com.pnam.validator"
})
public class WebAppContextConfigs implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    // Bean cho Bean Validation (Hibernate Validator)
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    // Bean cho WebAppValidator (gộp Bean Validation + Spring Validator)
    @Bean
    public WebAppValidator webAppValidator(UserValidator userValidator) {
        Set<Validator> springValidators = new HashSet<>();
        springValidators.add(userValidator);

        WebAppValidator v = new WebAppValidator();
        v.setSpringValidators(springValidators);
        return v;
    }
}
