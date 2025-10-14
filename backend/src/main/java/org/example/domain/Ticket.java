package org.example.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Ticket {
    @NotNull
    @Positive
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Coordinates coordinates;

    @NotNull
    private java.time.LocalDate creationDate = java.time.LocalDate.now();

    @NotNull
    private Person person;

    @NotNull
    private Event event;

    @Positive
    private float price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TicketType type;

    @NotNull
    @Positive
    @Max(100)
    private Long discount;

    @Positive
    private long number;

    @NotNull
    private String comment;

    @NotNull
    private Venue venue;
}
