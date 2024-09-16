package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatusType;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.response.ResponseRepository;
import ru.practicum.shareit.response.dto.ItemResponseDto;
import ru.practicum.shareit.response.dto.ItemResponseMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ResponseRepository responseRepository;

    public List<ItemDto> getAllUserItems(Long userId) {
        List<Item> itemList = itemRepository.findByOwnerId(userId);
        return itemList.parallelStream()
                .map(ItemMapper::mapItem)
                .collect(Collectors.toList());
    }

    public ItemDto getById(long itemId) {
        LocalDateTime actualTime = LocalDateTime.now();

        List<BookingDto> lastBooking = bookingRepository
                .findByItemIdAndStatusAndBookingEndBeforeOrderByBookingEnd(itemId, BookingStatusType.APPROVED, actualTime)
                .parallelStream()
                .map(BookingMapper::mapBooking)
                .toList();

        List<BookingDto> lastBookingOld = bookingRepository.findByItemIdAndStatus(itemId, BookingStatusType.APPROVED)
                .parallelStream()
                .map(BookingMapper::mapBooking)
                .toList();
        List<BookingDto> nextBooking = bookingRepository.findByItemIdAndStatus(itemId, BookingStatusType.WAITING)
                .parallelStream()
                .map(BookingMapper::mapBooking)
                .toList();
        List<ItemResponseDto> comments = responseRepository.findByItemId(itemId)
                .parallelStream()
                .map(ItemResponseMapper::mapItemResponse)
                .toList();
        return ItemMapper.mapItem(itemRepository.findById(itemId)
                        .orElseThrow(() -> new NotFoundException("Лот не найден itemId = " + itemId)),
                lastBooking.stream().findFirst().orElse(null),
                nextBooking.stream().findFirst().orElse(null),
                comments
        );
    }

    public ItemDto createItem(ItemDto itemDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден хозяин вещи c userId = " + userId));
        Item item = ItemMapper.mapItemDto(itemDto, user);
        return ItemMapper.mapItem(itemRepository.save(item));
    }

    public ItemDto updateItem(ItemDto itemDto, long userId) {
        Item oldItem = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Обновляемый лот не найден itemId = " + itemDto.getId()));
        // изменения может вносить только хозяин
        testOwner(oldItem, userId);
        Item newItem = ItemMapper.mapItemDto(itemDto, oldItem.getOwner());
        patchItem(newItem, oldItem);
        return ItemMapper.mapItem(itemRepository.save(newItem));
    }

    public ItemDto deleteItemById(long userId, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Удаляемый элемент не найден itemId = " + itemId));
        // удаление разрешено только хозяину
        testOwner(item, userId);
        itemRepository.delete(item);
        return ItemMapper.mapItem(item);
    }

    public List<ItemDto> searchItems(long userId, String text) {
        if ((text == null) || (text.isEmpty())) {
            return new ArrayList<>();
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("Неизвестный пользователь c userId = " + userId));
        List<Item> itemList = itemRepository.searchItems(text);
        return itemList.parallelStream()
                .map(ItemMapper::mapItem)
                .toList();
    }

    protected void testOwner(Item item, long ownerId) {
        if (item.getOwner().getId() != ownerId) {
            throw new ForbiddenException("Изменения лота возможны только хозяином, " +
                    "пользователь не хозяин userId = " + item.getOwner().getId());
        }
    }

    public void patchItem(Item newItem, Item oldItem) {
        if (newItem.getName() == null) {
            newItem.setName(oldItem.getName());
        }
        if (newItem.getDescription() == null) {
            newItem.setDescription(oldItem.getDescription());
        }
        if (newItem.getRequest() == null) {
            newItem.setRequest(oldItem.getRequest());
        }
        if (newItem.getIsAvailableForRent() == null) {
            newItem.setIsAvailableForRent(oldItem.getIsAvailableForRent());
        }
        if (newItem.getIsRented() == null) {
            newItem.setIsRented(oldItem.getIsRented());
        }
        if (newItem.getOwner() == null) {
            newItem.setOwner(oldItem.getOwner());
        }
    }
}
