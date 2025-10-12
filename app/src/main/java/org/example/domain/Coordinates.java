package org.example.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Coordinates {
    @Max(397)
    private double x; // Максимальное значение поля: 397

    @Max(315)
    @NotNull
    private Integer y; // Максимальное значение поля: 315, Поле не может быть null
}
