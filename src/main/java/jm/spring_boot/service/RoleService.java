package jm.spring_boot.service;

import jm.spring_boot.models.Role;

import java.util.List;

public interface RoleService {
    Role addRole(Role role);

    Role getByName(String name);

    Role getById(Long id);

    List<Role> getAll();
}
