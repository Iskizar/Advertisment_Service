//package ru.senla.finalproject;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import ru.senla.finalproject.dto.UserDto;
//import ru.senla.finalproject.entities.Announcement;
//import ru.senla.finalproject.entities.Comment;
//import ru.senla.finalproject.entities.Role;
//import ru.senla.finalproject.entities.User;
//import ru.senla.finalproject.exception.AnnouncementException;
//import ru.senla.finalproject.exception.UserException;
//import ru.senla.finalproject.repository.IRepository.IAnnouncementRepository;
//import ru.senla.finalproject.repository.IRepository.ICommentRepository;
//import ru.senla.finalproject.repository.IRepository.IRoleRepository;
//import ru.senla.finalproject.repository.IRepository.IUserRepository;
//import ru.senla.finalproject.service.UserDbService;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UserDbServiceTests {
//
//    @Mock
//    private IUserRepository userRepository;
//
//    @Mock
//    private IAnnouncementRepository announcementRepository;
//
//    @Mock
//    private IRoleRepository roleRepository;
//
//    @Mock
//    private ICommentRepository commentRepository;
//
//    @InjectMocks
//    private UserDbService userDbService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void save_ValidUser_ShouldSaveUser() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        Role role = new Role();
//        role.setName("ROLE_USER");
//
//        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
//        Mockito.when(userRepository.save(user)).thenReturn(user);
//
//        UserDto result = userDbService.save(user);
//
//        assertNotNull(result);
//        Mockito.verify(userRepository, Mockito.times(1)).save(user);
//    }
//
//    @Test
//    void save_RoleNotFound_ShouldCreateAndSaveRole() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        Role role = new Role();
//        role.setName("ROLE_USER");
//
//        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(null);
//        Mockito.when(roleRepository.save(ArgumentMatchers.any(Role.class))).thenReturn(role);
//        Mockito.when(userRepository.save(user)).thenReturn(user);
//
//        UserDto result = userDbService.save(user);
//
//        assertNotNull(result);
//        Mockito.verify(roleRepository, Mockito.times(1)).save(ArgumentMatchers.any(Role.class));
//        Mockito.verify(userRepository, Mockito.times(1)).save(user);
//    }
//
//    @Test
//    void findByEmail_ValidEmail_ShouldReturnUser() {
//        User user = new User();
//        user.setEmail("test@example.com");
//
//        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(user);
//
//        UserDto result = userDbService.findByEmail("test@example.com");
//
//        assertNotNull(result);
//        assertEquals("test@example.com", result.getEmail());
//    }
//
//    @Test
//    void findByEmail_InvalidEmail_ShouldThrowException() {
//        Mockito.when(userRepository.findByEmail("invalid@example.com")).thenReturn(null);
//
//        Exception exception = assertThrows(UserException.class, () -> {
//            userDbService.findByEmail("invalid@example.com");
//        });
//
//        assertEquals("Пользователь не найден: invalid@example.com", exception.getMessage());
//    }
//
//    @Test
//    void findAll_WithPageRequest_ShouldReturnUsers() {
//        PageRequest pageRequest = PageRequest.of(0, 10);
//        User user = new User();
//        Page<User> page = new PageImpl<>(Collections.singletonList(user));
//
//        Mockito.when(userRepository.findAll(pageRequest)).thenReturn(page);
//
//        List<UserDto> result = userDbService.findAll(pageRequest);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void updateByEmail_ValidUser_ShouldUpdateUser() {
//        User existingUser = new User();
//        existingUser.setEmail("test@example.com");
//        User updatedUser = new User();
//        updatedUser.setEmail("new@example.com");
//
//        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(existingUser);
//        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);
//
//        UserDto result = userDbService.updateByEmail("test@example.com", updatedUser);
//
//        assertNotNull(result);
//        Mockito.verify(userRepository, Mockito.times(1)).save(updatedUser);
//    }
//
//    @Test
//    void updateByEmail_InvalidUser_ShouldThrowException() {
//        User updatedUser = new User();
//        updatedUser.setEmail("new@example.com");
//
//        Mockito.when(userRepository.findByEmail("invalid@example.com")).thenReturn(null);
//
//        Exception exception = assertThrows(UserException.class, () -> {
//            userDbService.updateByEmail("invalid@example.com", updatedUser);
//        });
//
//        assertEquals("Пользователь не найден: invalid@example.com", exception.getMessage());
//    }
//
//    @Test
//    void deleteByEmail_ValidUser_ShouldDeleteUser() {
//        User user = new User();
//        user.setEmail("test@example.com");
//
//        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(user);
//        Mockito.doNothing().when(userRepository).delete(user);
//
//        UserDto result = userDbService.deleteByEmail("test@example.com");
//
//        assertNotNull(result);
//        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
//    }
//
//    @Test
//    void deleteByEmail_InvalidUser_ShouldThrowException() {
//        Mockito.when(userRepository.findByEmail("invalid@example.com")).thenReturn(null);
//
//        Exception exception = assertThrows(UserException.class, () -> {
//            userDbService.deleteByEmail("invalid@example.com");
//        });
//
//        assertEquals("Пользователь не найден: invalid@example.com", exception.getMessage());
//    }
//
//    @Test
//    void addEstimation_ValidUser_ShouldAddEstimation() {
//        User user = new User();
//        user.setEmail("test@example.com");
//
//        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(user);
//        Mockito.when(userRepository.save(user)).thenReturn(user);
//
//        userDbService.addEstimation("test@example.com", 5);
//
//        Mockito.verify(userRepository, Mockito.times(1)).save(user);
//    }
//
//    @Test
//    void addEstimation_InvalidUser_ShouldThrowException() {
//        Mockito.when(userRepository.findByEmail("invalid@example.com")).thenReturn(null);
//
//        Exception exception = assertThrows(UserException.class, () -> {
//            userDbService.addEstimation("invalid@example.com", 5);
//        });
//
//        assertEquals("Пользователь не найден: invalid@example.com", exception.getMessage());
//    }
//
//    @Test
//    void addComment_ValidUserAndAnnouncement_ShouldAddComment() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        Announcement announcement = new Announcement();
//        Comment comment = new Comment();
//        comment.setAuthor(user);
//        comment.setAnnouncement(announcement);
//
//        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(user);
//        Mockito.when(announcementRepository.findAnById(announcement.getId())).thenReturn(announcement);
//        Mockito.when(commentRepository.save(comment)).thenReturn(comment);
//
//        userDbService.addComment(comment);
//
//        Mockito.verify(commentRepository, Mockito.times(1)).save(comment);
//    }
//
//    @Test
//    void addComment_InvalidUser_ShouldThrowException() {
//        Announcement announcement = new Announcement();
//        Comment comment = new Comment();
//        comment.setAnnouncement(announcement);
//        User user = new User(1L, "Ivan", "Ivanov", "ivan_ivanov@mail.ru", "123");
//        comment.setAuthor(user);
//        announcement.setAuthor(user);
//        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
//
//        Exception exception = assertThrows(UserException.class, () -> {
//            userDbService.addComment(comment);
//        });
//
//        assertEquals("Пользователь не найден", exception.getMessage());
//    }
//
//    @Test
//    void addComment_InvalidAnnouncement_ShouldThrowException() {
//        User user = new User(1L, "Ivan", "Ivanov", "ivan_ivanov@mail.ru", "123");
//        Announcement announcement = new Announcement();
//        announcement.setId(1L);
//        announcement.setAuthor(user);
//        Comment comment = new Comment();
//        comment.setAuthor(user);
//        comment.setId(1L);
//        comment.setAnnouncement(announcement);
//        announcement.setComments(Collections.singletonList(comment));
//        Mockito.when(userRepository.findByEmail("ivan_ivanov@mail.ru")).thenReturn(user);
//        Mockito.when(announcementRepository.findAnById(ArgumentMatchers.anyLong())).thenReturn(null);
//
//        Exception exception = assertThrows(AnnouncementException.class, () -> {
//            userDbService.addComment(comment);
//        });
//
//        assertEquals("Объявление не найдено", exception.getMessage());
//    }
//}
