package ru.practicum.shareit.booking;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.RequestType;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusType;
import ru.practicum.shareit.exception.DataOperationException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<BookingDto> getAllUserBookings(Long userId, RequestType state, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с Id %d не найден", userId)));
        List<Booking> bookingList = new ArrayList<>();
        Pageable page = PageRequest.of(from, size);
        LocalDateTime time = LocalDateTime.now();
        switch (state) {
            case ALL -> bookingList = bookingRepository.findByUserIdOrderByBookingStart(userId, page);
            case CURRENT -> bookingList = bookingRepository
                    .findByUserIdAndBookingStartBeforeAndBookingEndAfterOrderByBookingStart(userId, time, time, page);
            case PAST -> bookingList = bookingRepository
                    .findByUserIdAndBookingEndBeforeOrderByBookingStart(userId, time, page);
            case FUTURE -> bookingList = bookingRepository
                    .findByUserIdAndBookingStartAfterOrderByBookingStart(userId, time, page);
            case WAITING -> bookingList = bookingRepository
                    .findByUserIdAndStatusOrderByBookingStart(userId, BookingStatusType.WAITING, page);
            case REJECTED -> bookingList = bookingRepository
                    .findByUserIdAndStatusOrderByBookingStart(userId, BookingStatusType.REJECTED, page);
        }
        return bookingList.parallelStream()
                .map(BookingMapper::mapBooking)
                .toList();
    }

    public List<BookingDto> getAllOwnerBookings(Long userId, RequestType state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с Id %d не найден", userId)));
        List<Booking> bookingList = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        switch (state) {
            case ALL -> bookingList = bookingRepository.findByOwnerId(userId);
            case CURRENT -> bookingList = bookingRepository.findByOwnerIdCurrent(userId, time);
            case PAST -> bookingList = bookingRepository.findByOwnerIdPast(userId, time);
            case FUTURE -> bookingList = bookingRepository.findByOwnerIdFuture(userId, time);
        }

        return bookingList.parallelStream()
                .map(BookingMapper::mapBooking)
                .toList();
    }

    public BookingDto createBooking(BookingDto bookingDto, long userId) {
        deepValidate(bookingDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь c userId = " + userId));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Не найдена вещь Id = " + bookingDto.getItemId()));
        // проверить, что вещь доступна для бронирования
        if (item.getIsAvailableForRent()
                && bookingRepository.findItemRentsInTime(item.getId(), bookingDto.getStart(), bookingDto.getEnd()).isEmpty()) {
            bookingDto.setBooker(UserMapper.mapUser(user));
            Booking booking = BookingMapper.mapBookingDto(bookingDto, item);
            booking.setStatus(BookingStatusType.WAITING);
            return BookingMapper.mapBooking(bookingRepository.save(booking));
        } else {
            throw new DataOperationException("Не может быть забронирован лот с Id = " + item.getId());
        }
    }

    public BookingDto changeBookingStatus(long userId, long bookingId, boolean newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найдено бранирование с id = " + bookingId));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ForbiddenException("Изменить статус бронирования может только хозяин лота");
        }
        // Повторно изменить статус нельзя, допустимо изменить только статус WAITING
        if (booking.getStatus() != BookingStatusType.WAITING) {
            throw new ForbiddenException(String
                    .format("Изменить статус бронирования %s нельзя, изменение возможно только один раз", booking.getStatus()));
        }
        if (newStatus) {
            booking.setStatus(BookingStatusType.APPROVED);
        } else {
            booking.setStatus(BookingStatusType.REJECTED);
        }
        return BookingMapper.mapBooking(bookingRepository.save(booking));
    }

    public BookingDto getBookingInfo(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Бронирования с указанным Id %d не найдено", bookingId)));
        if (booking.getItem().getOwner().getId() != userId && booking.getUser().getId() != userId) {
            throw new DataOperationException("Информация о бронировании предоставляется только владельцу лота и бронирующему");
        }
        return BookingMapper.mapBooking(booking);
    }

    protected void deepValidate(BookingDto bookingDto) {
        // Дополнительная валидация сущности по сложным связям полей
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new DataOperationException("Время начала и окончания бронирования совпадают " + bookingDto.getStart());
        }
    }
}
