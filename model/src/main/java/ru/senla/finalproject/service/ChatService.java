package ru.senla.finalproject.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.finalproject.repository.IRepository.IChatRepository;
import ru.senla.finalproject.repository.IRepository.IMessageRepository;
import ru.senla.finalproject.repository.IRepository.IUserRepository;
import ru.senla.finalproject.entities.Chat;
import ru.senla.finalproject.entities.Message;
import ru.senla.finalproject.entities.User;
import ru.senla.finalproject.dto.ChatDto;
import ru.senla.finalproject.dto.MessageDto;

import ru.senla.finalproject.service.IService.IChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ChatService implements IChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final IChatRepository chatRepository;
    private final IMessageRepository messageRepository;
    private final IUserRepository userRepository;

    @Transactional
    public ChatDto createChat(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + userId1));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + userId2));

        Chat chat = new Chat();
        chat.setUser1(user1);
        chat.setUser2(user2);
        Chat savedChat = chatRepository.save(chat);

        logger.debug("Создан чат между пользователями {} и {}", userId1, userId2);

        return new ChatDto(savedChat);
    }

    public List<ChatDto> getUserChats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + userId));

        return chatRepository.findByUser1(user).stream()
                .map(ChatDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDto sendMessage(Long chatId, Long senderId, String content) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден: " + chatId));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Отправитель не найден: " + senderId));

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content);

        Message savedMessage = messageRepository.save(message);

        logger.debug("Пользователь {} отправил сообщение в чат {}", senderId, chatId);

        return new MessageDto(savedMessage);
    }

    public List<MessageDto> getChatMessages(Long chatId) {
        return messageRepository.findByChatId(chatId).stream()
                .map(MessageDto::new)
                .collect(Collectors.toList());
    }
}
