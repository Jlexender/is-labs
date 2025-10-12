package org.example.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Address {
    @NotNull
    private String street; // Поле может быть null

    @NotNull
    private Location town; // Поле может быть null
}
