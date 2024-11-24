package ru.practicum.shareit.response.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.response.model.ItemResponse;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemResponseMapperTest {

    @Test
    void mapItemResponseDto() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("userName")
                .login("userLogin")
                .email("user@mail.com")
                .build();
        User user = UserMapper.mapUserDto(userDto);
        ItemDto itemDto = ItemDto.builder()
                .id(2L)
                .name("itemName")
                .build();
        Item item = ItemMapper.mapItemDto(itemDto, user);
        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .id(3L)
                .item(itemDto)
                .text("textOfResponse")
                .responseUser(userDto)
                .build();
        ItemResponse itemResponse = ItemResponseMapper.mapItemResponseDto(itemResponseDto, item);

        assertNotNull(user);
        assertNotNull(item);
        assertNotNull(itemResponse);
    }

    @Test
    void mapItemResponse() {
        User user = User.builder()
                .id(1L)
                .name("userName")
                .login("userLogin")
                .email("user@mail.com")
                .build();
        Item item = Item.builder()
                .id(2L)
                .name("itemName")
                .build();
        ItemResponse itemResponse = ItemResponse.builder()
                .id(3L)
                .name("responseName")
                .item(item)
                .responseUser(user)
                .build();
        ItemResponseDto itemResponseDto = ItemResponseMapper.mapItemResponse(itemResponse);
        ItemDto itemDto = ItemMapper.mapItem(item);

        assertNotNull(itemResponseDto);
        assertNotNull(itemDto);
    }
}