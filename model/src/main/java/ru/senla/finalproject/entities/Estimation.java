package ru.senla.finalproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "estimations")
public class Estimation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int estimation;
    public Estimation(int estimation) {
        this.estimation = estimation;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_estimations",
        joinColumns = @JoinColumn (name = "estimation_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();
}
