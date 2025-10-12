package org.example.domain;

import lombok.Data;

@Data
public class Ticket {
    private Long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого
                     // поля должно быть уникальным, Значение этого поля должно генерироваться
                     // автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private java.time.LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
                                              // автоматически
    private Person person; // Поле может быть null
    private Event event; // Поле может быть null
    private float price; // Значение поля должно быть больше 0
    private TicketType type; // Поле может быть null
    private Long discount; // Поле не может быть null, Значение поля должно быть больше 0, Максимальное
                           // значение поля: 100
    private long number; // Значение поля должно быть больше 0
    private String comment; // Поле может быть null
    private Venue venue; // Поле может быть null
}
