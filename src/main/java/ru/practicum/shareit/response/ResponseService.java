package ru.practicum.shareit.response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusType;
import ru.practicum.shareit.exception.DataOperationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.response.dto.ItemResponseDto;
import ru.practicum.shareit.response.dto.ItemResponseMapper;
import ru.practicum.shareit.response.model.ItemResponse;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    public ItemResponseDto createResponse(ItemResponseDto itemResponseDto, long userId, long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь для отзыва userId = " + userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найден лот для отзыва itemId = " + userId));
        LocalDateTime actualTime = LocalDateTime.now();
        List<Booking> booking = bookingRepository.findByUserIdAndStatus(userId, BookingStatusType.APPROVED);
        if (booking.isEmpty() || booking.getFirst().getBookingEnd().isAfter(actualTime)) {
            throw new DataOperationException(String.format("Пользователь %d не завершил аренду лота %d и не может оставить отзыв", userId, itemId));
        }
        itemResponseDto.setResponseUser(UserMapper.mapUser(user));
        itemResponseDto.setItem(ItemMapper.mapItem(item));
        itemResponseDto.setCreated(actualTime);
        ItemResponse itemResponse = ItemResponseMapper.mapItemResponseDto(itemResponseDto, item);
        return ItemResponseMapper.mapItemResponse(responseRepository.save(itemResponse));
    }
}
