package org.example.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Address {
    @NotNull
    private String street; 

    @NotNull
    private Location town;
}
