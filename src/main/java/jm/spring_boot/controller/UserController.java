package jm.spring_boot.controller;

import jm.spring_boot.models.Role;
import jm.spring_boot.models.User;
import jm.spring_boot.service.RoleService;
import jm.spring_boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminPage(Model model) {
        model.addAttribute("user", getUser().getUsername());
        model.addAttribute("users", userService.getAll());
        return "admin";
    }

    @GetMapping("/user")
    public String userPage(Model model) {
        model.addAttribute("user", getUser());
        return "user";
    }

    @PutMapping("/user/{id}")
    public String changeUser(@ModelAttribute("user") User user) {
        user.setRoles(getUser().getRoles());
        userService.save(user);
        return "user";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("list", new ArrayList<Long>());
        model.addAttribute("allRoles", roleService.getAll());
        return "add";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add(@ModelAttribute("user") @Valid User user,
                      BindingResult bindingResult,
                      @RequestParam("list") List<Long> list) {
        if (bindingResult.hasErrors()) {
            return "redirect:/add";
        }
        user.setRoles(addRolesUser(list));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (id != 1) {
            userService.deleteById(id);
        }
        return "redirect:/admin";
    }

    @GetMapping("{id}/change")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String change(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allRoles", roleService.getAll());
        model.addAttribute("list", new ArrayList<Long>());
        return "change";
    }

    @PutMapping("/change/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam("list") List<Long> list) {
        if (bindingResult.hasErrors()) {
            return "redirect:/" + user.getId() + "/change";
        }
        user.setRoles(addRolesUser(list));
        userService.addUser(user);
        return "redirect:/admin";
    }


    private Set<Role> addRolesUser(List<Long> list) {
        Set<Role> set = new HashSet<>();
        for (Long i : list) {
            set.add(roleService.getById(i));
        }
        return set;
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
    }
}
