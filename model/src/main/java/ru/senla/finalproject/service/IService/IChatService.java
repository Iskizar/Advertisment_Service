package ru.senla.finalproject.service.IService;

import ru.senla.finalproject.dto.ChatDto;
import ru.senla.finalproject.dto.MessageDto;


import java.util.List;

public interface IChatService {
    ChatDto createChat(Long userId1, Long userId2);
    List<ChatDto> getUserChats(Long userId);
    MessageDto sendMessage(Long chatId, Long senderId, String content);
    List<MessageDto> getChatMessages(Long chatId);
}
