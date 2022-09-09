package ru.kata.spring.boot_security.demo.configs;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserServiceImpl userService;

    // SuccessHandler это обработчик успешной аутентификации
    // UserDetails - минимальная информация о пользователях (логин, пароль и тд)

    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserServiceImpl userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//  защита от CSRF-атак(вроде подставного сайта где злоумышленник его использует и заставляет
                // от имени пользователя отправлять пароли, деньги со счёта на счёт и т.п
                .authorizeRequests()
                .antMatchers("/", "/login/**").permitAll() //авторизацуем запрос
                .antMatchers("/user").hasAnyRole("ADMIN", "USER") //прописываем доступ для юрл /user/**
                .antMatchers("/admin").hasRole("ADMIN") //прописываем доступ для юрл /admin/**
                .anyRequest().authenticated() // все запросы должны быть авторизованы и аутентифицированы
                .and()
                .formLogin().successHandler(successUserHandler).permitAll() // задаю форму для ввода логина-пароля, по дефолту это "/login" // доступно всем
                .and()
                .logout()
                .logoutSuccessUrl("/login") // настройка логаута
                .and()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

}