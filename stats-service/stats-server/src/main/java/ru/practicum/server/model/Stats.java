package ru.practicum.server.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "stats", schema = "public")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "app", nullable = false)
    String app;
    @Column(name = "uri", nullable = false)
    String uri;
    @Column(name = "ip", nullable = false)
    String ip;
    @Column(name = "time_stamp", nullable = false)
    LocalDateTime timestamp;
}
