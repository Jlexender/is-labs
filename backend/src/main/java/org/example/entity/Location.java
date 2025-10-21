package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Location {
    @Column(name = "x_location")
    Integer x;

    @Column(name = "y_location")
    long y;

    @Column(name = "z_location")
    long z;

    String locationName;
}

