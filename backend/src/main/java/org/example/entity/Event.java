package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Event {
    @NotBlank
    String eventName;

    @Positive
    int ticketsCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    EventType eventType;
}

