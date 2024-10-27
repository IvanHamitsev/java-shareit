package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.MiniItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;

    public ItemRequestDto createRequest(long userId, ItemRequestDto requestDto) {
        ItemRequest request = ItemRequestMapper.mapItemRequestDto(requestDto,
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Ошибка поиска пользователя в базе")));
        if (null == request.getCreated()) {
            log.debug("Date manually added");
            request.setCreated(LocalDateTime.now());
        }
        log.debug("Request created");
        return ItemRequestMapper.mapItemRequest(requestRepository.save(request));
    }

    public List<ItemRequestDto> getAllUserRequests(long userId) {
        return requestRepository.findByUserIdOrderByCreated(userId).parallelStream()
                .map(request -> {
                    List<MiniItemDto> miniItemDtoList = itemRepository.findByRequestId(request.getId()).parallelStream()
                            .map(ItemRequestMapper::mapItem)
                            .toList();
                    return ItemRequestMapper.mapItemRequest(request, miniItemDtoList);
                })
                .toList();
    }

    public ItemRequestDto getRequestById(long requestId) {
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос %d не найден", requestId)));
        List<MiniItemDto> miniItemDtoList = itemRepository.findByRequestId(request.getId()).parallelStream()
                .map(ItemRequestMapper::mapItem)
                .toList();
        return ItemRequestMapper.mapItemRequest(request, miniItemDtoList);
    }

    public List<ItemRequestDto> getAllRequests() {
        return requestRepository.findAllByOrderByCreated().parallelStream()
                .map(ItemRequestMapper::mapItemRequest)
                .toList();
    }
}
