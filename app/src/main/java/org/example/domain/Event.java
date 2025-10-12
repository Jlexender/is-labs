package org.example.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Event {
    @Positive
    // TODO: id generation
    private long id; // Значение поля должно быть больше 0, Значение этого поля должно быть
                     // уникальным, Значение этого поля должно генерироваться автоматически

    @NotEmpty
    private String name; // Поле не может быть null, Строка не может быть пустой

    @Positive
    private int ticketsCount; // Значение поля должно быть больше 0

    @NotNull
    private EventType eventType; // Поле не может быть null
}
