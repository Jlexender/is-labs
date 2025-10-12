package org.example.domain;

import lombok.Data;

@Data
public class Person {
    private Color eyeColor; // Поле может быть null
    private Color hairColor; // Поле может быть null
    private Location location; // Поле не может быть null
    private java.util.Date birthday; // Поле может быть null
    private Country nationality; // Поле не может быть null
}
