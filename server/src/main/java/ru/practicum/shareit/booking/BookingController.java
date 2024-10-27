package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestType;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody BookingDto bookingDto) {
        BookingDto resultBookingDto = bookingService.createBooking(bookingDto, userId);
        return resultBookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patchBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId,
                                   @RequestParam boolean approved) {
        return bookingService.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return bookingService.getBookingInfo(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getAllUserBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                               @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") Integer size) {
        RequestType state = RequestType.fromString(stateParam);
        return bookingService.getAllUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(defaultValue = "ALL") RequestType state) {
        return bookingService.getAllOwnerBookings(userId, state);
    }
}
