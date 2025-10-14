package org.example.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Address {
    @NotEmpty
    private String street; 

    @NotNull
    private Location town;
}
