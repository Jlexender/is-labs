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
    private Long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого
                     // поля должно быть уникальным, Значение этого поля должно генерироваться
                     // автоматически

    @NotEmpty
    private String name; // Поле не может быть null, Строка не может быть пустой

    @NotNull
    private Coordinates coordinates; // Поле не может быть null

    @NotNull
    // TODO: creationDate generation
    private java.time.LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
                                              // автоматически

    @NotNull
    private Person person; // Поле может быть null

    @NotNull
    private Event event; // Поле может быть null

    @Positive
    private float price; // Значение поля должно быть больше 0

    @NotNull
    private TicketType type; // Поле может быть null

    @NotNull
    @Positive
    @Max(100)
    private Long discount; // Поле не может быть null, Значение поля должно быть больше 0, Максимальное
                           // значение поля: 100

    @Positive
    private long number; // Значение поля должно быть больше 0

    @NotNull
    private String comment; // Поле может быть null

    @NotNull
    private Venue venue; // Поле может быть null
}
