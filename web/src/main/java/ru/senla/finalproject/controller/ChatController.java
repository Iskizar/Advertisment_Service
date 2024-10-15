package ru.senla.finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.senla.finalproject.dto.ChatDto;
import ru.senla.finalproject.dto.MessageDto;
import ru.senla.finalproject.service.ChatService;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/chats")
@Tag(name = "chat_api")
public class ChatController {

    private ChatService chatService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Создание нового чата")
    @PostMapping("/create")
    public ChatDto createChat(@RequestParam Long userId1, @RequestParam Long userId2) {
        return chatService.createChat(userId1, userId2);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Вывод всех чатов пользователя")
    @GetMapping("/user/{userId}")
    public List<ChatDto> getUserChats(@PathVariable Long userId) {
        return chatService.getUserChats(userId);
    }

    @Operation(summary = "Отправление сообщения пользователя в чат")
    @PostMapping("/{chatId}/message")
    public MessageDto sendMessage(@PathVariable Long chatId, @RequestParam Long senderId, @RequestParam String content) {
        return chatService.sendMessage(chatId, senderId, content);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Вывод всех сообщений чата")
    @GetMapping("/{chatId}/messages")
    public List<MessageDto> getChatMessages(@PathVariable Long chatId) {
        return chatService.getChatMessages(chatId);
    }
}
