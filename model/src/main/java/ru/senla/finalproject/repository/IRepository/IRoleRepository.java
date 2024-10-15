package ru.senla.finalproject.repository.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.senla.finalproject.entities.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
     Role findByName(String name);
}
