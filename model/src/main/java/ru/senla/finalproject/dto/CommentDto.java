package ru.senla.finalproject.dto;

import lombok.Data;
import ru.senla.finalproject.entities.Comment;

@Data
public class CommentDto {
    private String text;
    private String authorUsername;

    public CommentDto(Comment comment) {
        this.text = comment.getText();
        this.authorUsername = comment.getAuthor().getUsername();
    }

    @Override
    public String toString() {
        return "Comment: " + text;
    }
}
