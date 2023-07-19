package ru.practicum.service.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventWithRequests {
    Long eventId;
    Long confirmedRequests;
}
