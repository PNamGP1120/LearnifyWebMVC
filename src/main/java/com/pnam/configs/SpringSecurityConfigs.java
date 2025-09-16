package com.pnam.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.pnam.controllers",
    "com.pnam.repositories",
    "com.pnam.services"
})
public class SpringSecurityConfigs {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                // Public pages
                .requestMatchers("/home", "/register", "/login", "/error/**").permitAll()
                // Static resources
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // API frontend ReactJS
                .requestMatchers("/api/auth/**").permitAll()
                // API login
                .requestMatchers("/api/**").authenticated()
                // Admin panel
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                // Instructor course
                .requestMatchers(HttpMethod.POST, "/api/courses/**")
                .hasAnyAuthority("INSTRUCTOR", "ADMIN")
                // Request cần login
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/doLogin") 
                .defaultSuccessUrl("/admin/stats", true)
                .failureUrl("/login?error=true")
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout") 
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                );

        return http.build();
    }

    // Cho Thymeleaf dùng @PreAuthorize
    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    // Cloudinary config (sửa thông tin thật khi deploy)
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dqpu49bbo",
                "api_key", "743773348627895",
                "api_secret", "EF7elKsibuI8JEBqfMNZYYWUYvo",
                "secure", true
        ));
    }

    @Bean
    @Order(0)
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000")); // ReactJS
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
