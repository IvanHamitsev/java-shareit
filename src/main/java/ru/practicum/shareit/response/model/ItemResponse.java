package ru.practicum.shareit.response.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "ITEM_RESPONSES", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    @Id
    @Column(name = "ITEM_RESP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    User responseUser;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    Item item;
    @Column(name = "NAME", nullable = false)
    String name;
    @Column(name = "DESCRIPTION", nullable = false)
    String description;
    @Column(name = "RESPONSE_DATE", nullable = false)
    LocalDateTime responseDate;
}
