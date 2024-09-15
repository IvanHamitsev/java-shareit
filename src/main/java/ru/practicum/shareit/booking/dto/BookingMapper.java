package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusType;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Objects;

public class BookingMapper {
    public static Booking mapBookingDto(BookingDto bookingDto, Item item) {
        Booking booking = Booking.builder()
                .id(bookingDto.getId())
                .item(item)
                .user(UserMapper.mapUserDto(bookingDto.getBooker()))
                .bookingStart(bookingDto.getStart())
                .bookingEnd(bookingDto.getEnd())
                .build();
        if (bookingDto.getStatus() != null) {
            booking.setStatus(BookingStatusType.fromString(bookingDto.getStatus()));
        }
        return booking;
    }

    public static BookingDto mapBooking(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .item(ItemMapper.mapItem(booking.getItem()))
                .booker(UserMapper.mapUser(booking.getUser()))
                .status(booking.getStatus().toString())
                .start(booking.getBookingStart())
                .end(booking.getBookingEnd())
                .build();
    }
}
