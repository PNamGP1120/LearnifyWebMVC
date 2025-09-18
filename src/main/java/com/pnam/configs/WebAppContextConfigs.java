package com.pnam.configs;

import com.pnam.formatters.CategoryFormatter;
import com.pnam.formatters.UserFormatter;
import com.pnam.validator.WebAppValidator;
import com.pnam.validator.UserValidator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new CategoryFormatter());
        registry.addFormatter(new UserFormatter());
    }

    // Bean cho Bean Validation (Hibernate Validator)
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages"); // tên file: messages.properties (không cần đuôi .properties)
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    // Kết hợp Bean Validation với messages.properties
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
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

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear(); // bỏ hết
        converters.add(new MappingJackson2HttpMessageConverter()); // chỉ dùng JSON
    }
}
