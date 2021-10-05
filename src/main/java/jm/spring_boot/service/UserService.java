package jm.spring_boot.service;


import jm.spring_boot.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User addUser(User user);

    List<User> getAll();

    void update(User user);

    void deleteById(Long id);

    User findById(Long id);
}
