package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "ITEMS", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "NAME", nullable = false)
    String name;
    @Column(name = "DESCRIPTION", nullable = false)
    String description;
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    User owner;
    // если вещь добавлена по запросу
    @ManyToOne
    @JoinColumn(name = "REQUEST_ID")
    ItemRequest request;
    @Column(name = "IS_AVAILABLE_FOR_RENT", nullable = false)
    Boolean isAvailableForRent;
    @Column(name = "IS_RENTED", nullable = false)
    Boolean isRented;
}
