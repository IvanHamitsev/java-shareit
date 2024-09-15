package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    final BookingService bookingService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody BookingDto bookingDto) {
        BookingDto resultBookingDto = bookingService.createBooking(bookingDto, userId);
        return  resultBookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patchBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping()
    public List<BookingDto> getAllUserBooking(@RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getAllBookings(userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingInfo(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return bookingService.getBookingInfo(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBooking(@RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getAllOwnerBookings(userId);
    }
}
