package ru.practicum.shareit.booking.model;

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
@Table(name = "BOOKINGS", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @Column(name = "BOOKING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    User user;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    Item item;
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    BookingStatusType status;
    @Column(name = "BOOKING_START", nullable = false)
    LocalDateTime bookingStart;
    @Column(name = "BOOKING_END")
    LocalDateTime bookingEnd;
}
