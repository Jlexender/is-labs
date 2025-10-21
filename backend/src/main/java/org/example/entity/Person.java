package org.example.entity;

import java.util.Date;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Person {
    @Enumerated(EnumType.STRING)
    Color eyeColor;

    @Enumerated(EnumType.STRING)
    Color hairColor;

    @Embedded
    @NotNull
    Location location;

    Date birthday;

    @NotNull
    @Enumerated(EnumType.STRING)
    Country nationality;
}

