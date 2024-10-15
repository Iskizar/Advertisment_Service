package ru.senla.finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.senla.finalproject.dto.AnnouncementDto;
import ru.senla.finalproject.entities.Announcement;
import ru.senla.finalproject.entities.User;
import ru.senla.finalproject.exception.AnnouncementException;
import ru.senla.finalproject.service.AnnouncementDbService;
import ru.senla.finalproject.service.IService.IAnnouncementService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/announcement")
@AllArgsConstructor
@Tag(name = "announcement_api")
public class AnnouncementController {
    private final AnnouncementDbService announcementDbService;
    private final IAnnouncementService announcementService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Добавление объяления в бд")
    @PostMapping("/save")
    public String save(@RequestBody Announcement announcement) {
        try {
            return String.format("Объявление сохранено успешно:\n%s", announcementService.save(announcement));
        }catch (RuntimeException e) {
            log.debug("Ошибка при сохранении объявления");
            throw new AnnouncementException("Произошла ошибка при сохранении объявления");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Поиск объявление по id")
    @GetMapping("/get/{id}")
    public AnnouncementDto getById(@PathVariable Long id) {
        try {
            return announcementService.findById(id);

        }catch (RuntimeException e) {
            log.debug("Ошибка при попытке найти объявление");
            throw new AnnouncementException("Произошла ошибка при попытке найти объявление");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Постраничный вывод всех объявлений")
    @GetMapping("/find_all")
    public List<AnnouncementDto> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            return announcementService.findAll(PageRequest.of(page, size));

        }catch (RuntimeException e) {
            log.debug("Ошибка при поиске объявлений");
            throw new AnnouncementException("Произошла ошибка при поиске объявлений");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Постраничный вывод всех закрытых (проданных) объявленией")
    @GetMapping("/find_user_sold")
    public List<AnnouncementDto> findAllUserSold(@RequestBody User email,
                                                 @RequestParam(required = false, defaultValue = "0") int page,
                                                 @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            return announcementService.findAllSold(email, PageRequest.of(page, size));

        }catch (RuntimeException e) {
            log.debug("Ошибка при поиске проданных объявлений");
            throw new AnnouncementException("Произошла ошибка при поиске проданных объявлений");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Поиск объявлений по названию",
            description = "Постраничный вывод объявлений, похожих на введённое пользователем название"
            )
    @GetMapping("/find")
    public List<AnnouncementDto> findByTitle(@RequestBody String title,
                                             @RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            return announcementService.findByTitle(title, PageRequest.of(page, size));

        }catch (RuntimeException e) {
            log.debug("Ошибка при поиске объявлений по запросу");
            throw new AnnouncementException(String.format("Произошла ошибка при поиске объявлений по запросу \"%s\"", title));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Обновление данных объявления")
    @PatchMapping("/update")
    public String update(@RequestBody Announcement announcement) {
        try {
            return String.format("Объявление успешно обновлено: %s", announcementService.update(announcement));

        }catch (RuntimeException e) {
            log.debug("Ошибка при попытке обновить объявление");
            throw new AnnouncementException("Произошла ошибка при попытке обновить объявление");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/delete")
    public String delete(@RequestParam Integer id) {
        try {
            return String.format("Удаление прошло успешно: %s", announcementService.delete(Long.valueOf(id))) ;

        }catch (RuntimeException e) {
            log.debug("Ошибка при попытке удалить объявление");
            throw new AnnouncementException("Произошла ошибка при попытке удалить объявление");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Буст объявления",
            description = "Буст объявления влияет на его положение в списке при выводе пользователю"
    )
    @PatchMapping("/boost/{id}")
    public String boost(@PathVariable Long id ,@RequestBody Integer sum) {
        try {
            return String.format("Успех! Объяление будет в топе до %s", announcementDbService.boost(id, sum).toString());
        }catch (RuntimeException e) {
            log.debug("Ошибка при попытке буста объявления");
            throw new AnnouncementException("Произошла ошибка при попытке буста объявления");
        }
    }
}
