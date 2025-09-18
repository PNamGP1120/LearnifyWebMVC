package com.pnam.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pnam.filters.JwtFilter;
import com.pnam.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@EnableMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {
    "com.pnam.controllers",
    "com.pnam.repositories",
    "com.pnam.services"
})
public class SpringSecurityConfigs {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // React FE
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

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
    public JwtFilter jwtFilter(UserService userService) {
        return new JwtFilter(userService);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http.securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler((req, res, e) -> res.sendError(HttpServletResponse.SC_FORBIDDEN))
                )
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/payments/vnpay-return", "/api/payments/vnpay-ipn").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categories/**")
                .permitAll()

                // STUDENT
                .requestMatchers("/api/payments/**").hasAuthority("STUDENT")
                .requestMatchers("/api/student/**").hasAuthority("STUDENT")
                .requestMatchers("/api/cart/**", "/api/wishlist/**", "/api/rating/**").hasAuthority("STUDENT")
                .requestMatchers(HttpMethod.POST, "/api/enrollments/**").hasAuthority("STUDENT")
                .requestMatchers(HttpMethod.GET, "/api/enrollments/my/**").hasAuthority("STUDENT")
                .requestMatchers("/api/stats/student/**").hasAuthority("STUDENT")
                // COURSES
                .requestMatchers(HttpMethod.GET, "/api/courses/**").hasAnyAuthority("STUDENT", "INSTRUCTOR", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/courses/**").hasAnyAuthority("INSTRUCTOR", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasAnyAuthority("INSTRUCTOR", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasAnyAuthority("INSTRUCTOR", "ADMIN")
                // INSTRUCTOR + PENDING_INSTRUCTOR
                .requestMatchers("/api/instructor/**").hasAnyAuthority("INSTRUCTOR", "PENDING_INSTRUCTOR")
                .requestMatchers("/api/enrollments/course/**").hasAuthority("INSTRUCTOR")
                .requestMatchers("/api/stats/instructor/**").hasAuthority("INSTRUCTOR")
                // ADMIN
                .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/api/stats/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain adminSecurity(HttpSecurity http) throws Exception {
        http.securityMatcher("/admin/**", "/login", "/logout", "/")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/home", "/register", "/error/**", "/login").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .loginPage("/login")
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
}
