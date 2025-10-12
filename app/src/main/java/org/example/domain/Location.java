package org.example.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Location {
    @NotNull
    private Integer x; // Поле не может быть null

    private long y;

    private long z;

    @NotEmpty
    private String name; // Поле может быть null
}
