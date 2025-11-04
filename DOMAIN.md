```java
public class Ticket {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Person person; //Поле может быть null
    private Event event; //Поле может быть null
    private float price; //Значение поля должно быть больше 0
    private TicketType type; //Поле может быть null
    private Long discount; //Поле не может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 100
    private long number; //Значение поля должно быть больше 0
    private String comment; //Поле может быть null
    private Venue venue; //Поле может быть null
}
public class Coordinates {
    private double x; //Максимальное значение поля: 397
    private Integer y; //Максимальное значение поля: 315, Поле не может быть null
}
public class Person {
    private Color eyeColor; //Поле может быть null
    private Color hairColor; //Поле может быть null
    private Location location; //Поле не может быть null
    private java.util.Date birthday; //Поле может быть null
    private Country nationality; //Поле не может быть null
}
public class Event {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private int ticketsCount; //Значение поля должно быть больше 0
    private EventType eventType; //Поле не может быть null
}
public class Venue {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private int capacity; //Значение поля должно быть больше 0
    private Address address; //Поле не может быть null
}
public class Location {
    private Integer x; //Поле не может быть null
    private long y;
    private long z;
    private String name; //Поле может быть null
}
public class Address {
    private String street; //Поле может быть null
    private Location town; //Поле может быть null
}
public enum TicketType {
    VIP,
    BUDGETARY,
    CHEAP;
}
public enum Color {
    GREEN,
    RED,
    BLUE,
    YELLOW;
}
public enum Country {
    INDIA,
    THAILAND,
    SOUTH_KOREA,
    JAPAN;
}
public enum EventType {
    FOOTBALL,
    BASEBALL,
    THEATRE_PERFORMANCE,
    EXPOSITION;
}
```