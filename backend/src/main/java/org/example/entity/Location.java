package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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

