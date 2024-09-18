package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusType;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByBookingStart(long userId);

    List<Booking> findByUserIdAndBookingStartBeforeAndBookingEndAfterOrderByBookingStart(long userId,
                                                                                         LocalDateTime time1,
                                                                                         LocalDateTime time2);

    List<Booking> findByUserIdAndBookingEndBeforeOrderByBookingStart(long userId, LocalDateTime time);

    List<Booking> findByUserIdAndBookingStartAfterOrderByBookingStart(long userId, LocalDateTime time);

    List<Booking> findByUserIdAndStatusOrderByBookingStart(long userId, BookingStatusType status);

    List<Booking> findByItemIdAndStatusAndBookingEndBeforeOrderByBookingEnd(long itemId,
                                                                            BookingStatusType status,
                                                                            LocalDateTime time);

    List<Booking> findByItemIdAndBookingStartAfterOrderByBookingStart(long itemId, LocalDateTime time);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.item i " +
            "JOIN i.owner u " +
            "WHERE u.id = ?1 " +
            "ORDER BY b.bookingStart")
    List<Booking> findByOwnerId(long userId);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.item i " +
            "JOIN i.owner u " +
            "WHERE u.id = ?1 AND " +
            "b.bookingStart < ?2 AND " +
            "b.bookingEnd > ?2 " +
            "ORDER BY b.bookingStart")
    List<Booking> findByOwnerIdCurrent(long userId, LocalDateTime time);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.item i " +
            "JOIN i.owner u " +
            "WHERE u.id = ?1 AND " +
            "b.bookingEnd < ?2 " +
            "ORDER BY b.bookingStart")
    List<Booking> findByOwnerIdPast(long userId, LocalDateTime time);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.item i " +
            "JOIN i.owner u " +
            "WHERE u.id = ?1 AND " +
            "b.bookingStart > ?2 " +
            "ORDER BY b.bookingStart")
    List<Booking> findByOwnerIdFuture(long userId, LocalDateTime time);

    // Для проверки пересечения двух отрезков используется свойство,
    // что любые типы пересечения обязательно имеют один из двух признаков:
    // 1) Начало первого отрезка внутри второго отрезка
    // 2) Начало второго отрезка внутри первого отрезка
    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.id = ?1 AND " +
            "((b.bookingStart <= ?2 AND b.bookingEnd >= ?2) OR " +
            "(b.bookingStart >= ?2 AND b.bookingStart <= ?3))")
    List<Booking> findItemRentsInTime(long itemId, LocalDateTime startTime, LocalDateTime endTime);
}
