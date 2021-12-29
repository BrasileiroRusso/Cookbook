package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.auth.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByName(String roleName);
}
