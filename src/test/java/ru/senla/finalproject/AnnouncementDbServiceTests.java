package ru.senla.finalproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.senla.finalproject.dto.AnnouncementDto;
import ru.senla.finalproject.entities.Announcement;
import ru.senla.finalproject.entities.Estimation;
import ru.senla.finalproject.entities.User;
import ru.senla.finalproject.repository.IRepository.IAnnouncementRepository;
import ru.senla.finalproject.repository.IRepository.IUserRepository;
import ru.senla.finalproject.service.IService.IAnnouncementService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class AnnouncementDbServiceTests {

    @Mock
    private IAnnouncementRepository announcementRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private IAnnouncementService announcementDbService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ValidAnnouncement_ShouldReturnSavedAnnouncement() {
        Announcement announcement = new Announcement();
        User user = new User();
        user.setEmail("test@example.com");
        announcement.setAuthor(user);

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        Mockito.when(announcementRepository.save(announcement)).thenReturn(announcement);

        AnnouncementDto result = announcementDbService.save(announcement);

        assertNotNull(result);
        Mockito.verify(announcementRepository, Mockito.times(1)).save(announcement);
    }

    @Test
    void save_InvalidUser_ShouldThrowException() {
        Announcement announcement = new Announcement();
        User user = new User();
        user.setEmail("invalid@example.com");
        announcement.setAuthor(user);

        Mockito.when(userRepository.findByEmail("invalid@example.com")).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            announcementDbService.save(announcement);
        });

        assertEquals("Пользователь не найден", exception.getMessage());
        Mockito.verify(announcementRepository, Mockito.never()).save(ArgumentMatchers.any(Announcement.class));
    }

    @Test
    void findAll_WithPageRequest_ShouldReturnSortedAnnouncements() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Announcement announcement1 = new Announcement();
        Announcement announcement2 = new Announcement();
        User user1 = new User();
        User user2 = new User();
        user1.addEstimation(new Estimation(5));
        user2.addEstimation(new Estimation(10));
        announcement1.setAuthor(user1);
        announcement2.setAuthor(user2);
        Page<Announcement> page = new PageImpl<>(Arrays.asList(announcement1, announcement2));

        Mockito.when(announcementRepository.findAll(pageRequest)).thenReturn(page);

        List<AnnouncementDto> result = announcementDbService.findAll(pageRequest);

        assertEquals(2, result.size());
        assertEquals(10, result.get(0).getAuthor().getRating());
        assertEquals(5, result.get(1).getAuthor().getRating());
    }

    @Test
    void findAllSold_ValidUser_ShouldReturnAnnouncements() {
        User user = new User(1L, "Ivan", "Ivanov", "ivan_ivanov@mail.ru", "123");
        List<Announcement> announcements = new ArrayList<>();
        Announcement announcement = new Announcement();
        announcement.setAuthor(user);
        announcement.setId(1L);
        announcement.setSold(Boolean.TRUE);
        announcements.add(announcement);
        PageRequest pageRequest = PageRequest.of(0, 10);

        Mockito.when(userRepository.findByEmail("ivan_ivanov@mail.ru")).thenReturn(user);
        Mockito.when(announcementRepository.findAllBySoldAndAuthor(true, user)).thenReturn(announcements);

        List<AnnouncementDto> result = announcementDbService.findAllSold(user, pageRequest);

        assertEquals(1, result.size());
        Mockito.verify(announcementRepository, Mockito.times(1)).findAllBySoldAndAuthor(true, user);
    }

    @Test
    void findById_ValidId_ShouldReturnAnnouncement() {
        Announcement announcement = new Announcement();
        announcement.setId(1L);
        User user = new User(1L, "Ivan", "Ivanov", "ivan_ivanov@mail.ru", "123");
        announcement.setAuthor(user);
        Mockito.when(announcementRepository.findAnById(1L)).thenReturn(announcement);

        AnnouncementDto result = announcementDbService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void update_ValidAnnouncement_ShouldReturnUpdatedAnnouncement() {
        Announcement announcement = new Announcement();
        announcement.setId(1L);
        Announcement existingAnnouncement = new Announcement();
        existingAnnouncement.setId(1L);
        User user = new User();
        existingAnnouncement.setAuthor(user);

        Mockito.when(announcementRepository.findAnById(1L)).thenReturn(existingAnnouncement);
        Mockito.when(announcementRepository.save(announcement)).thenReturn(announcement);

        AnnouncementDto result = announcementDbService.update(announcement);

        assertNotNull(result);
        Mockito.verify(announcementRepository, Mockito.times(1)).save(announcement);
    }

    @Test
    void delete_ValidId_ShouldDeleteAnnouncement() {
        Announcement announcement = new Announcement();
        announcement.setId(1L);
        User user = new User(1L, "Ivan", "Ivanov", "ivan_ivanov@mail.ru", "123");
        announcement.setAuthor(user);
        Mockito.when(announcementRepository.findAnById(1L)).thenReturn(announcement);
        Mockito.doNothing().when(announcementRepository).delete(announcement);

        AnnouncementDto result = announcementDbService.delete(1L);

        assertNotNull(result);
        Mockito.verify(announcementRepository, Mockito.times(1)).delete(announcement);
    }

    @Test
    void boost_ValidId_ShouldBoostAnnouncement() {
        Announcement announcement = new Announcement();
        announcement.setId(1L);

        Mockito.when(announcementRepository.findAnById(1L)).thenReturn(announcement);
        Mockito.when(announcementRepository.save(announcement)).thenReturn(announcement);

        LocalDate result = announcementDbService.boost(1L, 300);

        assertNotNull(result);
        assertEquals(LocalDate.now().plusDays(3), result);
        Mockito.verify(announcementRepository, Mockito.times(1)).save(announcement);
    }
}
