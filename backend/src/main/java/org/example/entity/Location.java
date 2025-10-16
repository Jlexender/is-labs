package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Location {
    @NotNull
    @Column(name = "x_location", nullable = false)
    Integer x;

    @NotNull
    @Column(name = "y_location", nullable = false)
    long y;

    @NotNull
    @Column(name = "z_location", nullable = false)
    long z;

    String locationName; // nullable
}

