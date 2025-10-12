package org.example.domain;

import lombok.Data;

@Data
public class Address {
    private String street; // Поле может быть null
    private Location town; // Поле может быть null
}
