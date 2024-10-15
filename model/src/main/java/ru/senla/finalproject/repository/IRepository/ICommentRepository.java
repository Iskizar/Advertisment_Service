package ru.senla.finalproject.repository.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.senla.finalproject.entities.Comment;


@Repository
public interface ICommentRepository  extends JpaRepository<Comment, Long> {
}
