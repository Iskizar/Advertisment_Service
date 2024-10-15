package ru.senla.finalproject.dto;

import lombok.Data;
import ru.senla.finalproject.entities.Announcement;
import ru.senla.finalproject.entities.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class AnnouncementDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate boostEndDate;
    private UserDto author;
    private Boolean boosted;
    private Boolean sold;
    private List<CommentDto> commentList = new ArrayList<>();

    public AnnouncementDto(Announcement announcement) {
        this.id = announcement.getId();
        this.title = announcement.getTitle();
        this.description = announcement.getDescription();
        this.boostEndDate = announcement.getBoostEnd();
        this.author = new UserDto(announcement.getAuthor());
        this.boosted = announcement.isBoosted();
        this.sold = announcement.getSold();
        for (Comment comment : announcement.getComments()) {
            commentList.add(new CommentDto(comment));
        }
    }

    @Override
    public String toString() {
        String response = "ID: " + id + "\nTitle: " + title + "\nDescription: " + description
                + "\nAuthor: " + author.getEmail() + "\nBoosted: " + boosted;
        if (boostEndDate.isAfter(LocalDate.now())) {
            response += "\nBoost End Date: " + boostEndDate;
        }
        return response;
    }
}
