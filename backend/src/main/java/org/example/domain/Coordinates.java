package org.example.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Coordinates {
    @Max(397)
    private double x;

    @Max(315)
    @NotNull
    private Integer y;
}
