package ru.senla.finalproject.service.IService;

import org.springframework.data.domain.PageRequest;
import ru.senla.finalproject.entities.Announcement;
import ru.senla.finalproject.entities.User;
import ru.senla.finalproject.dto.AnnouncementDto;


import java.time.LocalDate;
import java.util.List;

public interface IAnnouncementService {
    AnnouncementDto save(Announcement an);
    List<AnnouncementDto> findAll(PageRequest pageRequest);
    AnnouncementDto findById(Long id);
    List<AnnouncementDto> findByTitle(String title, PageRequest pageRequest);
    AnnouncementDto update(Announcement an);
    AnnouncementDto delete(Long id);
    List<AnnouncementDto> findAllSold(User author, PageRequest pageRequest);
    LocalDate boost(Long id, Integer sum);
}
