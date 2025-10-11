package org.example.domain;

public class Venue {
    private long id; // Значение поля должно быть больше 0, Значение этого поля должно быть
                     // уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private int capacity; // Значение поля должно быть больше 0
    private Address address; // Поле не может быть null
}
