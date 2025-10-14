package org.example.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Person {
    @NotNull
    private Color eyeColor;

    @NotNull
    private Color hairColor;

    @NotNull
    private Location location;

    @NotNull
    private java.util.Date birthday;

    @NotNull
    private Country nationality;
}
