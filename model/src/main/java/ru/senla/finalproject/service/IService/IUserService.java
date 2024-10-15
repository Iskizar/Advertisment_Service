package ru.senla.finalproject.service.IService;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.senla.finalproject.entities.Comment;
import ru.senla.finalproject.entities.User;
import ru.senla.finalproject.dto.UserDto;

import java.util.List;

public interface IUserService extends UserDetailsService {
     UserDto save(User user);
     UserDto findByEmail(String email);
     List<UserDto> findAll(PageRequest pageRequest);
     UserDto updateByEmail(String email, User user);
     UserDto deleteByEmail(String email);
     void addEstimation(String email, Integer estimation);
     void addComment(Comment comment);
     List<UserDto> sortByRating(List<UserDto> users);

    String getPasswordByEmail(String email);
}
