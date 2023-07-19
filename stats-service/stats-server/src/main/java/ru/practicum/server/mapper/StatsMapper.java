package ru.practicum.server.mapper;

import ru.practicum.dto.CommonConstants;
import ru.practicum.dto.model.EndpointHit;
import ru.practicum.server.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {
    public static Stats dtoToModel(EndpointHit endpointHit) {
        Stats stats = new Stats();
        stats.setApp(endpointHit.getApp());
        stats.setUri(endpointHit.getUri());
        stats.setIp(endpointHit.getIp());
        stats.setTimestamp(LocalDateTime.parse(endpointHit.getTimestamp(), DateTimeFormatter.ofPattern(CommonConstants.DATE_FORMAT)));
        return stats;
    }
}
