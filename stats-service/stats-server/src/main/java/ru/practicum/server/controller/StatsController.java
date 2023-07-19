package ru.practicum.server.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CommonConstants;
import ru.practicum.dto.model.EndpointHit;
import ru.practicum.dto.model.ViewStats;
import ru.practicum.server.exception.DateException;
import ru.practicum.server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsController {
    final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody @Valid EndpointHit endpointHit) {
        statsService.addHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam (value = "start") @DateTimeFormat(pattern = CommonConstants.DATE_FORMAT) LocalDateTime start,
                                    @RequestParam (value = "end") @DateTimeFormat(pattern = CommonConstants.DATE_FORMAT) LocalDateTime end,
                                    @RequestParam (value = "uris", required = false) List<String> uris,
                                    @RequestParam (value = "unique",required = false, defaultValue = "false") Boolean unique) {
        if (start.isAfter(end)) {
            throw new DateException("Illegal Date");
        }
        return statsService.getStats(start, end, uris, unique);
    }
}
