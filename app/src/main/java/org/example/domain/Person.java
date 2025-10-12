package org.example.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Person {
    @NotNull
    private Color eyeColor; // Поле может быть null

    @NotNull
    private Color hairColor; // Поле может быть null

    @NotNull
    private Location location; // Поле не может быть null

    @NotNull
    private java.util.Date birthday; // Поле может быть null

    @NotNull
    private Country nationality; // Поле не может быть null
}
