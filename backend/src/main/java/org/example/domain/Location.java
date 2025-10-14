package org.example.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Location {
    @NotNull
    private Integer x;

    private long y;

    private long z;

    @NotEmpty
    private String name;
}
