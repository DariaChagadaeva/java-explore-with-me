package ru.practicum.service.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.service.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Table(name = "compilations", schema = "public")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    Boolean pinned;
    @Column(nullable = false)
    String title;
    @ManyToMany
    @JoinTable(name = "compilations_events",
            joinColumns = {@JoinColumn(name = "compilation_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")})
    List<Event> events;
}
