package org.example.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    @Enumerated(EnumType.STRING)
    Color eyeColor;

    @Enumerated(EnumType.STRING)
    Color hairColor;

    @Embedded
    Location location;

    Date birthday;

    @Enumerated(EnumType.STRING)
    Country nationality;
}

