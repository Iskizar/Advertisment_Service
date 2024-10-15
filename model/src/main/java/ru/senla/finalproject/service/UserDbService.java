package ru.senla.finalproject.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.finalproject.repository.IRepository.IAnnouncementRepository;
import ru.senla.finalproject.repository.IRepository.ICommentRepository;
import ru.senla.finalproject.repository.IRepository.IRoleRepository;
import ru.senla.finalproject.repository.IRepository.IUserRepository;
import ru.senla.finalproject.entities.*;
import ru.senla.finalproject.dto.UserDto;
import ru.senla.finalproject.exception.AnnouncementException;
import ru.senla.finalproject.exception.UserException;
import ru.senla.finalproject.service.IService.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDbService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserDbService.class);

    private IUserRepository userRepository;
    private IAnnouncementRepository announcementRepository;
    private IRoleRepository roleRepository;
    private ICommentRepository commentRepository;
    @Override
    @Transactional
    public UserDto save(User user) {
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
        }
        user.setRoles(List.of(role));
        User savedUser;
        if (userRepository.findByEmail(user.getEmail()) == null) {
            savedUser = userRepository.save(user);
        }
        else {
            throw new UserException("Пользователь уже существует");
        }

        logger.debug("Сохранён новый пользовалеть: {}", user.getEmail());

        return new UserDto(savedUser);
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("Пользователь не найден: " + email);
        }
        return new UserDto(user);
    }

    @Override
    public List<UserDto> findAll(PageRequest pageRequest) {
         Page<User> page = userRepository.findAll(pageRequest);
        return page.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto updateByEmail(String email, User updatedUser) {
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            throw new UserException("Пользователь не найден: " + email);
        }
        updatedUser.setId(existingUser.getId());
        if (updatedUser.getEmail() == null) {
            updatedUser.setEmail(existingUser.getEmail());
        }
        if (updatedUser.getFirstName() == null) {
            updatedUser.setFirstName(existingUser.getFirstName());
        }
        if (updatedUser.getLastName() == null) {
            updatedUser.setLastName(existingUser.getLastName());
        }
        if (updatedUser.getPassword() == null) {
            updatedUser.setPassword(existingUser.getPassword());
        }
        if (updatedUser.getEstimations() == null) {
            updatedUser.setEstimations(existingUser.getEstimations());
        }
        User savedUser = userRepository.save(updatedUser);

        logger.debug("Пользователь обновлён: {}", email);

        return new UserDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto deleteByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("Пользователь не найден: " + email);
        }
        userRepository.delete(user);

        logger.debug("Пользователь удалён: {}", email);

        return new UserDto(user);
    }

    @Override
    @Transactional
    public void addEstimation(String email, Integer estimation) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("Пользователь не найден: " + email);
        }
        user.addEstimation(new Estimation(estimation));
        userRepository.save(user);

        logger.debug("Пользователю добавлена оценка: {}", email);
    }

    @Override
    @Transactional
    public void addComment(Comment comment) {
        User user = userRepository.findByEmail(comment.getAuthor().getUsername());
        Announcement announcement = announcementRepository.findAnById(comment.getAnnouncement().getId());
        if (user == null) {
            throw new UserException("Пользователь не найден");
        }
        if (announcement == null) {
            throw new AnnouncementException("Объявление не найдено");
        }
        comment.setAnnouncement(announcement);
        comment.setAuthor(user);
        commentRepository.save(comment);

        logger.debug("Добавлен комментарий от пользователя: {} на объявление: {}",
                comment.getAuthor().getUsername(), comment.getAnnouncement().getId());
    }

    @Override
    public List<UserDto> sortByRating(List<UserDto> users) {
        return users.stream()
                .sorted((u1, u2) -> Float.compare(u2.getRating(), u1.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPasswordByEmail(String email) {
        String s = userRepository.findByEmail(email).getPassword();
        return userRepository.findByEmail(email).getPassword();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден: " + email);
        }
        return user;
    }
}
