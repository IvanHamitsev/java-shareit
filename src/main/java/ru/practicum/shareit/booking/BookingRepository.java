package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(long userId);

    List<Booking> findByItemId(long itemId);

    List<Booking> findByUserIdAndStatus(long userId, BookingStatusType status);

    List<Booking> findByItemIdAndStatus(long userId, BookingStatusType status);
    List<Booking> findByItemIdAndStatusAndBookingEndBefore(long userId, BookingStatusType status, LocalDateTime time);
}
