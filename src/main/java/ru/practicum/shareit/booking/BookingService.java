package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<BookingDto> getAllBookings(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с Id %d не найден", userId)));
        List<Booking> bookingList = bookingRepository.findByUserId(userId);
        return bookingList.parallelStream()
                .map(BookingMapper::mapBooking)
                .toList();
    }

    public List<BookingDto> getAllOwnerBookings(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с Id %d не найден", userId)));
        //List<Booking> bookingList = bookingRepository.findByOwnerId(userId);
        List<Booking> bookingList = bookingRepository.findByUserId(userId);
        return bookingList.parallelStream()
                .map(BookingMapper::mapBooking)
                .toList();
    }

    public BookingDto createBooking(BookingDto bookingDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь c userId = " + userId));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Не найдена вещь Id = " + bookingDto.getItemId()));
        deepValidate(bookingDto);
        // проверить, что вещь доступна для бронирования
        if (item.getIsAvailableForRent() && !item.getIsRented()) {
            bookingDto.setBooker(UserMapper.mapUser(user));
            Booking booking = BookingMapper.mapBookingDto(bookingDto, item);
            booking.setStatus(BookingStatusType.WAITING);
            BookingDto resultBookingDto = BookingMapper.mapBooking(bookingRepository.save(booking));
            item.setIsRented(true);
            itemRepository.save(item);
            return resultBookingDto;
        } else {
            throw new DataOperationException("Лот не может быть забранирован Id = " + item.getId());
        }

    }

    public BookingDto changeBookingStatus(long userId, long bookingId, boolean newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найдено бранирование с id = " + bookingId));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ForbiddenException("Изменить статус бронирования может только хозяин лота");
        }
        if (newStatus) {
            booking.setStatus(BookingStatusType.APPROVED);
        } else {
            booking.setStatus(BookingStatusType.REJECTED);
        }
        return BookingMapper.mapBooking(bookingRepository.save(booking));
    }

    public BookingDto getBookingInfo(long userId, long bookingId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataOperationException("Не найден пользователь, запросивший информацию id = " + userId));
        return BookingMapper.mapBooking(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирования с указанным Id %d не найдено", bookingId))));
    }

    protected void deepValidate(BookingDto bookingDto) {
        // Дополнительная валидация сущности по сложным связям полей
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new DataOperationException("Время начала и окончания бронирования совпадают " + bookingDto.getStart());
        }

    }
}
