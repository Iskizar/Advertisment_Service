package ru.senla.finalproject.dto;

import lombok.Data;
import ru.senla.finalproject.entities.Chat;
import ru.senla.finalproject.entities.Message;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatDto {
    private Long id;
    private UserDto participant1;
    private UserDto participant2;
    private List<MessageDto> messageList = new ArrayList<>();

    public ChatDto(Chat chat) {
        this.id = chat.getId();
        this.participant1 = new UserDto(chat.getUser1());
        this.participant2 = new UserDto(chat.getUser2());
        if (chat.getMessages() != null) {
            for (Message message : chat.getMessages()) {
                this.messageList.add(new MessageDto(message));
            }
        }
    }
}
