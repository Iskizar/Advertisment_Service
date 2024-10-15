package ru.senla.finalproject.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "announcements")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDate boostEnd = LocalDate.of(1,1,1);
    private Boolean sold = Boolean.FALSE;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @OneToMany(mappedBy = "announcement")
    private List<Comment> comments = new ArrayList<>();

    public Boolean isBoosted(){
        return boostEnd.isAfter(LocalDate.now());
    }


}
