package org.example.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Event {
    @Positive
    // TODO: id generation
    private long id;

    @NotEmpty
    private String name;

    @Positive
    private int ticketsCount;

    @NotNull
    private EventType eventType;
}
