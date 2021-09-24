package jm.spring_boot.config;

import jm.spring_boot.models.Role;
import jm.spring_boot.models.User;
import jm.spring_boot.service.RoleService;
import jm.spring_boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
    private RoleService roleService;
    private UserService userService;

    @Autowired
    public DataLoader(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataBase() {

        Role adminRole = roleService.addRole(new Role("ROLE_ADMIN"));
        Role userRole = roleService.addRole(new Role("ROLE_USER"));

        User user = new User();
        user.setUsername("ADMIN");
        user.setPassword("ADMIN");
        user.addRole(adminRole);
        user.addRole(userRole);

        User user2 = new User();
        user2.setUsername("USER");
        user2.setPassword("USER");
        user2.addRole(userRole);

        userService.addUser(user);
        userService.addUser(user2);

    }
}
