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
    Long id;

    @NotBlank
    String name;

    @Embedded
    @NotNull
    Coordinates coordinates;

    @Column(nullable = false)
    LocalDate creationDate = LocalDate.now();

    @Embedded
    Person person;

    @Embedded
    Event event;

    @Column(nullable = false)
    @Positive
    float price;

    @Enumerated(EnumType.STRING)
    TicketType type;

    @Column(nullable = false)
    @NotNull
    @Positive
    @Max(100)
    Long discount;

    @Column(nullable = false)
    @Positive
    long number;

    String comment;

    @Embedded
    Venue venue;
}

