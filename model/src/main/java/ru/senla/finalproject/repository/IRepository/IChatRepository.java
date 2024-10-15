package ru.senla.finalproject.repository.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.senla.finalproject.entities.Chat;
import ru.senla.finalproject.entities.User;

import java.util.List;

@Repository
public interface IChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUser1(User user1);
}
