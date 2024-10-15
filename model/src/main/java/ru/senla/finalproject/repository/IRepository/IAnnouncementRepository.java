package ru.senla.finalproject.repository.IRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.senla.finalproject.entities.Announcement;
import ru.senla.finalproject.entities.User;

import java.util.List;

@Repository
public interface IAnnouncementRepository extends JpaRepository<Announcement, Long> {
    Announcement findAnById(Long id);
    List<Announcement> findByAuthorId(Long author_id);
    List<Announcement> findAllBySoldAndAuthor(Boolean isSold, User author);
    Page<Announcement> findAll(Pageable pageable);
}
