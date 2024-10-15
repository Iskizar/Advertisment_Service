package ru.senla.finalproject.dto;

import lombok.Data;
import ru.senla.finalproject.entities.Message;

@Data
public class MessageDto {
    private UserDto sender;
    private String content;

    public MessageDto(Message message) {
        this.content = message.getContent();
        this.sender = new UserDto(message.getSender());
    }
}
