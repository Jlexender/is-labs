package org.example.domain;

import lombok.Data;

@Data
public class Location {
    private Integer x; // Поле не может быть null
    private long y;
    private long z;
    private String name; // Поле может быть null
}
