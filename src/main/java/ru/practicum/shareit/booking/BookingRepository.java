package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(long userId);

    List<Booking> findByItemId(long itemId);

    List<Booking> findByUserIdAndStatus(long userId, BookingStatusType status);

    List<Booking> findByItemIdAndStatus(long itemId, BookingStatusType status);

    List<Booking> findByItemIdAndStatusAndBookingEndBeforeOrderByBookingEnd(long itemId, BookingStatusType status, LocalDateTime time);

    @Query("""
            SELECT DISTINCT b FROM Booking b 
            JOIN b.item i 
            JOIN i.owner u
            WHERE u.id = ?1
            """)
    List<Booking> findByOwnerId(long userId);
}
