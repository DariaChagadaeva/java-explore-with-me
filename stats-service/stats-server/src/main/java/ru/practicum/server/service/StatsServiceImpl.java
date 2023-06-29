package ru.practicum.server.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.server.exception.DateException;
import ru.practicum.server.repository.StatsRepository;
import ru.practicum.server.mapper.StatsMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsServiceImpl implements StatsService{
    final StatsRepository statsRepository;

    @Override
    @Transactional
    public void addHit(EndpointHit endpointHit) {
        statsRepository.save(StatsMapper.dtoToModel(endpointHit));
        log.info("Information saved {}", endpointHit);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        validateDate(start, end);
        log.info("Statistics by parameters start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        if(uris == null || uris.isEmpty()) {
            if(unique) {
                return statsRepository.getStatsWithUniqueIp(start, end);
            } else {
                return statsRepository.getStats(start, end);
            }
        } else {
            if(unique) {
                return statsRepository.getStatsAndUrisWithUniqueIp(start, end, uris);
            } else {
                return statsRepository.getStatsAndUris(start, end, uris);
            }
        }
    }

    private void validateDate(LocalDateTime start, LocalDateTime end) {
        if(start.isAfter(end)) {
            throw new DateException("Illegal Date");
        }
    }
}
