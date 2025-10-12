package org.example.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Ticket {
    @NotNull
    @Positive
    // TODO: id generation
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Coordinates coordinates;

    @NotNull
    // TODO: creationDate generation
    private java.time.LocalDate creationDate;

    @NotNull
    private Person person;

    @NotNull
    private Event event;

    @Positive
    private float price;

    @NotNull
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
