package org.example.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // > 0 auto

    @Column(nullable = false)
    @NotBlank
    String name; // not null, not empty

    @Embedded
    @NotNull
    Coordinates coordinates; // not null

    @Column(nullable = false)
    LocalDate creationDate = LocalDate.now(); // auto

    // Optional complex fields (person, event, venue) omitted for MVP

    @Column(nullable = false)
    @Positive
    float price; // > 0

    @Enumerated(EnumType.STRING)
    TicketType type; // nullable

    @Column(nullable = false)
    @NotNull
    @Positive
    @Max(100)
    Long discount; // not null, >0, <=100

    @Column(nullable = false)
    @Positive
    long number; // > 0

    String comment; // nullable
}

