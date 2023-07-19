package ru.practicum.service.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.service.request.dto.RequestStatusAction;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {
    @NotEmpty
    List<Long> requestIds;
    @NotNull
    RequestStatusAction status;
}
