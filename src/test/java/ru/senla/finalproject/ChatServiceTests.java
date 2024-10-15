//package ru.senla.finalproject;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import ru.senla.finalproject.dto.ChatDto;
//import ru.senla.finalproject.dto.MessageDto;
//import ru.senla.finalproject.entities.Chat;
//import ru.senla.finalproject.entities.Message;
//import ru.senla.finalproject.entities.User;
//import ru.senla.finalproject.repository.IRepository.IChatRepository;
//import ru.senla.finalproject.repository.IRepository.IMessageRepository;
//import ru.senla.finalproject.repository.IRepository.IUserRepository;
//import ru.senla.finalproject.service.ChatService;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class ChatServiceTests {
//
//    @Mock
//    private IChatRepository chatRepository;
//
//    @Mock
//    private IMessageRepository messageRepository;
//
//    @Mock
//    private IUserRepository userRepository;
//
//    @InjectMocks
//    private ChatService chatService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createChat_ValidUsers_ShouldCreateChat() {
//        User user1 = new User(1L, "Ivan", "Ivanov", "ivan_ivanov@mail.ru", "123");
//        User user2 = new User(2L, "Boris", "Borisov", "boris_borisov@mail.ru", "123");
//
//        Chat chat = new Chat();
//        chat.setUser1(user1);
//        chat.setUser2(user2);
//
//        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
//        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
//        Mockito.when(chatRepository.save(ArgumentMatchers.any(Chat.class))).thenReturn(chat);
//
//        ChatDto result = chatService.createChat(1L, 2L);
//
//        assertNotNull(result);
//        Mockito.verify(chatRepository, Mockito.times(1)).save(ArgumentMatchers.any(Chat.class));
//    }
//
//    @Test
//    void createChat_InvalidUser_ShouldThrowException() {
//        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            chatService.createChat(1L, 2L);
//        });
//
//        assertEquals("Пользователь не найден: 1", exception.getMessage());
//        Mockito.verify(chatRepository, Mockito.never()).save(ArgumentMatchers.any(Chat.class));
//    }
//
//    @Test
//    void getUserChats_ValidUser_ShouldReturnChats() {
//        User user1 = new User(1L, "Ivan", "Ivanov", "ivan_ivanov@mail.ru", "123");
//        User user2 = new User(2L, "Boris", "Borisov", "boris_borisov@mail.ru", "123");
//
//        List<Chat> chats = Collections.singletonList(new Chat());
//        chats.getFirst().setUser1(user1);
//        chats.getFirst().setUser2(user2);
//        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
//        Mockito.when(chatRepository.findByUser1(user1)).thenReturn(chats);
//
//        List<ChatDto> result = chatService.getUserChats(1L);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        Mockito.verify(chatRepository, Mockito.times(1)).findByUser1(user1);
//    }
//
//    @Test
//    void sendMessage_ValidChatAndUser_ShouldSendMessage() {
//        Chat chat = new Chat();
//        chat.setId(1L);
//        User user1 = new User(1L, "Ivan", "Ivanov", "ivan_ivanov@mail.ru", "123");
//        User user2 = new User(2L, "Boris", "Borisov", "boris_borisov@mail.ru", "123");
//        chat.setUser1(user1);
//        chat.setUser2(user2);
//        Message message = new Message();
//        message.setContent("Hello");
//        message.setSender(user1);
//        Mockito.when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
//        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
//        Mockito.when(messageRepository.save(ArgumentMatchers.any(Message.class))).thenReturn(message);
//
//        MessageDto result = chatService.sendMessage(1L, 1L, "Hello");
//
//        assertNotNull(result);
//        assertEquals("Hello", result.getContent());
//        Mockito.verify(messageRepository, Mockito.times(1)).save(ArgumentMatchers.any(Message.class));
//    }
//
//    @Test
//    void sendMessage_InvalidChat_ShouldThrowException() {
//        Mockito.when(chatRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            chatService.sendMessage(1L, 1L, "Hello");
//        });
//
//        assertEquals("Чат не найден: 1", exception.getMessage());
//        Mockito.verify(messageRepository, Mockito.never()).save(ArgumentMatchers.any(Message.class));
//    }
//
//    @Test
//    void getChatMessages_ValidChat_ShouldReturnMessages() {
//        Chat chat = new Chat();
//        chat.setId(1L);
//        User user1 = new User(1L, "Ivan", "Ivanov", "ivan_ivanov@mail.ru", "123");
//        User user2 = new User(2L, "Boris", "Borisov", "boris_borisov@mail.ru", "123");
//        chat.setUser1(user1);
//        chat.setUser2(user2);
//        List<Message> messages = Collections.singletonList(new Message());
//        messages.getFirst().setSender(user1);
//        messages.getFirst().setChat(chat);
//        Mockito.when(messageRepository.findByChatId(1L)).thenReturn(messages);
//
//        List<MessageDto> result = chatService.getChatMessages(1L);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        Mockito.verify(messageRepository, Mockito.times(1)).findByChatId(1L);
//    }
//}
