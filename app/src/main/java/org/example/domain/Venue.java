package org.example.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Venue {
    @Positive
    // TODO: id generation
    private long id; // Значение поля должно быть больше 0, Значение этого поля должно быть
                     // уникальным, Значение этого поля должно генерироваться автоматически

    @NotEmpty
    private String name; // Поле не может быть null, Строка не может быть пустой

    @Positive
    private int capacity; // Значение поля должно быть больше 0

    @NotNull
    private Address address; // Поле не может быть null
}
