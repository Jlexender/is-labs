package org.example.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Coordinates {
    @Max(397)
    double x;

    @NotNull
    @Max(315)
    Integer y;
}
