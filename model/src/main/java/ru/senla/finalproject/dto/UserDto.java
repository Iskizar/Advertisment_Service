package ru.senla.finalproject.dto;

import lombok.Data;
import ru.senla.finalproject.entities.User;

import java.util.List;

@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private Float rating;
    private List<String> announcementTitles;

    public UserDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.rating = user.getRating();
        this.announcementTitles = user.getAnnouncementsTitles();
    }

    @Override
    public String toString() {
        return "First Name: " + firstName + "\nLast Name: " + lastName
                + "\nEmail: " + email + "\nRating: " + rating
                + "\nAnnouncements: " + announcementTitles;
    }
}
