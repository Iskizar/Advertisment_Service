package ru.senla.finalproject.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.finalproject.repository.IRepository.IAnnouncementRepository;
import ru.senla.finalproject.repository.IRepository.IUserRepository;
import ru.senla.finalproject.entities.Announcement;
import ru.senla.finalproject.entities.User;
import ru.senla.finalproject.dto.AnnouncementDto;
import ru.senla.finalproject.service.IService.IAnnouncementService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
public class AnnouncementDbService implements IAnnouncementService {
    private final IAnnouncementRepository announcementRepository;
    private final IUserRepository userRepository;

    @Transactional
    public AnnouncementDto save(Announcement announcement) {
        User user = userRepository.findByEmail(announcement.getAuthor().getEmail());
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        announcement.setAuthor(user);
        return new AnnouncementDto(announcementRepository.save(announcement));
    }

    public List<AnnouncementDto> findAll(PageRequest pageRequest) {
        Page<Announcement> announcements =  announcementRepository.findAll(pageRequest);

        // Сортировка по наличию буста и рейтингу автора
        List<Announcement> sortedAnnouncements = announcements.stream()
                .sorted(Comparator.comparing(Announcement::isBoosted).reversed()
                        .thenComparing(a -> a.getAuthor().getRating(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return sortedAnnouncements.stream()
                .map(AnnouncementDto::new)
                .collect(Collectors.toList());
    }
    public List<AnnouncementDto> findAll() {
        List<Announcement> announcements =  announcementRepository.findAll();

        List<Announcement> sortedAnnouncements = announcements.stream()
                .sorted(Comparator.comparing(Announcement::isBoosted).reversed()
                        .thenComparing(a -> a.getAuthor().getRating(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return sortedAnnouncements.stream()
                .map(AnnouncementDto::new)
                .collect(Collectors.toList());
    }

    public List<AnnouncementDto> findAllSold(User author, PageRequest pageRequest) {
        List<Announcement> announcements = announcementRepository.findAllBySoldAndAuthor(true, userRepository.findByEmail(author.getEmail()));
        int min = pageRequest.getPageNumber() * pageRequest.getPageSize();
        int max = min + pageRequest.getPageSize();
        return announcements.stream()
                .map(AnnouncementDto::new)
                .collect(Collectors.toList()).subList(min, Math.min(max, announcements.size()));
    }

    @Override
    public AnnouncementDto findById(Long id) {
        return new AnnouncementDto(announcementRepository.findAnById(id));
    }

    public List<AnnouncementDto> findByTitle(String title, PageRequest pageRequest) {
        String formattedTitle = title.replaceAll("[+.^:,\"']", "").toLowerCase();
        List<AnnouncementDto> announcementDtos = findAll().stream()
                .peek(a -> a.setTitle(a.getTitle().replaceAll("[+.^:,\"']", "").toLowerCase()))
                .collect(Collectors.toList());

        List<AnnouncementDto> exactMatches = announcementDtos.stream()
                .filter(a -> a.getTitle().equals(formattedTitle))
                .collect(Collectors.toList());

        announcementDtos.removeAll(exactMatches);
        List<AnnouncementDto> similarMatches = sortBySimilarity(announcementDtos, formattedTitle);

        exactMatches.addAll(similarMatches);
        int start = (int)pageRequest.getOffset();
        int end = (int) pageRequest.getOffset() + pageRequest.getPageSize();
        return exactMatches.subList( start,
                Math.min(end, announcementDtos.size())-1);
    }

    private List<AnnouncementDto> sortBySimilarity(List<AnnouncementDto> announcements, String checkTitle) {
        Map<AnnouncementDto, Double> similarityResult = new HashMap<>();

        for (AnnouncementDto announcement : announcements) {
            double similarity = titleSimilarity(announcement.getTitle(), checkTitle);
            if (similarity > 0.1) {
                similarityResult.put(announcement, similarity);
            }
        }


        return similarityResult.entrySet().stream()
                .sorted(Map.Entry.<AnnouncementDto, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Double titleSimilarity(String title, String checkTitle) {
        List<String> titleWords = List.of(title.split(" "));
        List<String> checkTitleWords = List.of(checkTitle.split(" "));
        long commonWords = checkTitleWords.stream()
                .filter(titleWords::contains)
                .count();

        return commonWords / (double) (titleWords.size() + checkTitleWords.size());
    }

    @Transactional
    public AnnouncementDto update(Announcement announcement) {
        Announcement existingAnnouncement = announcementRepository.findAnById(announcement.getId());
        announcement.setAuthor(existingAnnouncement.getAuthor());
        announcement.setId(existingAnnouncement.getId());

        if (announcement.getTitle() == null) {
            announcement.setTitle(existingAnnouncement.getTitle());
        }
        if (announcement.getDescription() == null) {
            announcement.setDescription(existingAnnouncement.getDescription());
        }
        if (announcement.getBoostEnd() == null) {
            announcement.setBoostEnd(existingAnnouncement.getBoostEnd());
        }
        return new AnnouncementDto(announcementRepository.save(announcement));
    }

    @Transactional
    public AnnouncementDto delete(Long id) {
        Announcement announcement = announcementRepository.findAnById(id);
        announcementRepository.delete(announcement);
        return new AnnouncementDto(announcement);
    }

    @Override
    @Transactional
    public LocalDate boost(Long id, Integer sum) {
        Announcement announcement = announcementRepository.findAnById(id);
        announcement.setBoostEnd(LocalDate.now().plusDays(sum / 100));
        announcementRepository.save(announcement);
        return announcement.getBoostEnd();
    }
}
