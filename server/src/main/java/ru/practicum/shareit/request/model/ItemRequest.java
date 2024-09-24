package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "ITEM_REQUESTS", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @Id
    @Column(name = "ITEM_REQ_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    User user;
    @Column(name = "NAME", nullable = false)
    String name;
    @Column(name = "DESCRIPTION", nullable = false)
    String description;
    @Column(name = "REQUEST_DATE", nullable = false)
    LocalDateTime requestDate;
}
