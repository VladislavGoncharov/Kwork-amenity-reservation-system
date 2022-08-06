package com.amenity_reservation_system.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "user")
public class User {
    private static final String SEQ_NAME = "user_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    private String fullName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private OffsetDateTime dateCreated;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private OffsetDateTime lastUpdated;
    private boolean checkIn;
    private String email;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Reservation> reservations = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        checkIn = false;
        dateCreated = OffsetDateTime.now();
        lastUpdated = dateCreated;
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdated = OffsetDateTime.now();
    }

    public User(String fullName, String username, String passwordHash) {
        this.fullName = fullName;
        this.username = username;
        this.password = passwordHash;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }
}
