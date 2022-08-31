package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.model.User;


import javax.management.relation.Role;
import java.util.List;

public interface UserService {

    void saveUser(User user);

    void updateUser(User user);

    List<User> findAll();

    User getUserById(Long id);

    void deleteById(User user);

    User findByUsername(String username);

    List<Role> listRoles();

    UserDetails loadUserByUsername(String username);

}