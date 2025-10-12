package org.example.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Event {
    @Positive
    @Id
    private long id;

    @NotEmpty
    private String name;

    @Positive
    private int ticketsCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EventType eventType;
}
