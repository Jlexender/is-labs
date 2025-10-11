package org.example.domain;

public class Event {
    private long id; // Значение поля должно быть больше 0, Значение этого поля должно быть
                     // уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private int ticketsCount; // Значение поля должно быть больше 0
    private EventType eventType; // Поле не может быть null
}
