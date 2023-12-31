package ru.practicum.service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.service.event.dto.EventWithRequests;
import ru.practicum.service.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByIdIn(List<Long> requestIds);

    List<Request> findByEventId(Long eventId);

    Optional<Request> findByEventIdAndRequesterId(Long eventId, Long userId);

    @Query(value = "SELECT new ru.practicum.service.event.dto.EventWithRequests(r.event.id, count(r.id)) " +
            "FROM Request as r " +
            "WHERE r.event.id IN (?1) " +
            "AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event.id")
    List<EventWithRequests> getAllEventIdsConfirmed(List<Long> eventsId);
}
