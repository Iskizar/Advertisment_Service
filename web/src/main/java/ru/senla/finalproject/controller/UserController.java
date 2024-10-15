package ru.senla.finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.senla.finalproject.dto.UserDto;
import ru.senla.finalproject.entities.Comment;
import ru.senla.finalproject.entities.User;
import ru.senla.finalproject.exception.UserException;
import ru.senla.finalproject.service.IService.IUserService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "user_api")
public class UserController {
    IUserService userService;

    @Operation(summary = "Добавление нового пользователя в бд")
    @PostMapping("/registration")
    public String save(@RequestBody User user) {
        try {
            return String.format("Пользователь успешно сохранён \n%s", userService.save(user));

        }catch (UserException e) {
            log.debug("Ошибка при регистрации пользователя");
            throw new UserException(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Поиск пользователя по почте")
    @GetMapping("/get/{email}")
    public UserDto find(@PathVariable String email) {
        try {
            return userService.findByEmail(email);

        }catch (RuntimeException e) {
            log.error("Ошибка при попытке найти пользователя");
            throw new UserException("Произошла ошибка при попытке найти пользователя");
        }

    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Постраничный всех пользователей")
    @GetMapping("/get_all")
    public List<UserDto> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                                 @RequestParam(required = false, defaultValue = "10") int size) {
        try {
            List<UserDto> users = userService.findAll(PageRequest.of(page, size));
            users = userService.sortByRating(users);
            return users;
        }catch (RuntimeException e) {
            log.debug("Ошибка при попытке вывода пользователей");
            throw new UserException("Произошла ошибка при попытке вывода пользователей");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Обновление данных о пользователе")
    @PatchMapping("/update/{email}")
    public String updateUser(@RequestBody User user, @PathVariable String email) {
        try {
            return String.format("Пользователь успешно обновлён \n%s", userService.updateByEmail(email, user));

        }catch (RuntimeException e) {
            log.debug("Ошибка при попытке обновления данных пользователя");
            throw new UserException("Произошла ошибка при попытке обновления данных пользователя");
        }

    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Удаление пользователя по почте")
    @DeleteMapping("/delete/{email}")
    public String deleteUser(@PathVariable String email){
        try {
            return String.format("Пользователь удалён \n%s", userService.deleteByEmail(email));
        }catch (RuntimeException e) {
            log.error("Ошибка при попытке удаления пользователя");
            throw new UserException("Произошла ошибка при попытке удаления пользователя");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Добавление оценки пользователю",
            description = "Среднее всех оценок составляет рейтинг пользователя, " +
                    "влияющий на положение его объявдений в списке"
    )
    @PatchMapping("/add_estimation/{email}")
    public String addEstimation(@PathVariable String email, @RequestBody Integer estimation) {
        try {
            userService.addEstimation(email, estimation);
            return "Оценка успешно добавлена!";
        }catch (RuntimeException e) {
            log.error("Ошибка при попытке добавить оценку");
            throw new UserException("Произошла ошибка при попытке добавить оценку");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Добавление комментария пользователем")
    @PatchMapping("/add_comment")
    public String addComment(@RequestBody Comment comment) {
        try {
            userService.addComment(comment);
            return "Комментарий успешно добавлен";
        }catch (RuntimeException e){
            log.error("Ошибка при попытке добавить комментарий");
            throw new UserException("Произошла ошибка при попытке добавить комментарий");
        }
    }
}
