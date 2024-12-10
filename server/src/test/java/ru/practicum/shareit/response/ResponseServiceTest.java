package ru.practicum.shareit.response;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusType;
import ru.practicum.shareit.exception.DataOperationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.response.dto.ItemResponseDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(ResponseService.class)

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
/*
@TestPropertySource(properties = {"spring.datasource.url=jdbc:postgresql://localhost:5432/shareit-test",
        "spring.datasource.username=testUser",
        "spring.datasource.password=testpass"})
*/
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:shareit-test",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.sql.init.schema-locations=classpath:test-schema.sql"})
@DataJpaTest
class ResponseServiceTest {
    @Autowired
    ResponseService responseService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    BookingRepository bookingRepository;

    private UserDto createUserDto(String name, String email, String login, LocalDate birthday) {
        UserDto userDto = UserDto.builder()
                .name(name)
                .email(email)
                .login(login)
                .birthday(birthday)
                .build();
        User user = User.builder()
                .name(name)
                .email(email)
                .login(login)
                .birthday(birthday)
                .build();
        var result = userRepository.save(user);
        userDto.setId(result.getId());
        return userDto;
    }

    private User createUser(String name, String email, String login, LocalDate birthday) {
        User user = User.builder()
                .name(name)
                .email(email)
                .login(login)
                .birthday(birthday)
                .build();
        return userRepository.save(user);
    }

    private ItemDto createItemDto(String name, String description, long ownerId, ItemRequest request, boolean available) {
        ItemDto itemDto = ItemDto.builder()
                .name(name)
                .description(description)
                .available(true)
                .build();
        User owner = userRepository.findById(ownerId).orElseThrow();
        Item item = Item.builder()
                .name(name)
                .description(description)
                .owner(owner)
                .request(request)
                .isAvailableForRent(available)
                .build();
        var result = itemRepository.save(item);
        itemDto.setId(result.getId());
        return itemDto;
    }

    private Item createItem(String name, String description, long ownerId, ItemRequest request, boolean available) {
        User owner = userRepository.findById(ownerId).orElseThrow();
        Item item = Item.builder()
                .name(name)
                .description(description)
                .owner(owner)
                .request(request)
                .isAvailableForRent(available)
                .build();
        return itemRepository.save(item);
    }

    private BookingDto createBookingDto(long itemId, UserDto booker) {
        LocalDateTime now = LocalDateTime.now();
        return BookingDto.builder()
                .itemId(itemId)
                .booker(booker)
                .start(now.minusDays(3))
                .end(now.minusDays(2))
                .build();
    }

    private Booking createBooking(Item item, User booker) {
        LocalDateTime now = LocalDateTime.now();
        return Booking.builder()
                .item(item)
                .user(booker)
                .status(BookingStatusType.APPROVED)
                .bookingStart(now.minusDays(3))
                .bookingEnd(now.minusDays(2))
                .build();
    }

    private ItemResponseDto createItemResponseDto(UserDto userDto, ItemDto itemDto, LocalDateTime createTime) {
        return ItemResponseDto.builder()
                .id(1L)
                .responseUser(userDto)
                .item(itemDto)
                .created(createTime)
                .name("responseName")
                .text("responseText")
                .build();
    }

    @Test
    void createResponse() {
        LocalDateTime nowTime = LocalDateTime.now();
        User user = createUser("Имя1", "email1@yandex.ru", "login1", LocalDate.now().minusYears(20));
        User anotherUser = createUser("Имя2", "email2@yandex.ru", "login2", LocalDate.now().minusYears(20));
        User owner = createUser("Имя3", "email3@yandex.ru", "login3", LocalDate.now().minusYears(21));
        Item item1 = createItem("Item1Name", "Description1", owner.getId(), null, true);
        Item item2 = createItem("Item2Name", "Description2", owner.getId(), null, true);
        Booking booking = createBooking(item1, user);
        Booking booking1 = bookingRepository.save(booking);

        ItemResponseDto itemResponseDto = createItemResponseDto(
                UserMapper.mapUser(user),
                ItemMapper.mapItem(item1),
                nowTime);

        ItemResponseDto responseDto = responseService.createResponse(
                itemResponseDto,
                user.getId(),
                item1.getId());
        assertNotNull(responseDto);

        assertThrows(DataOperationException.class, () -> responseService.createResponse(
                itemResponseDto,
                anotherUser.getId(),
                item1.getId()));
    }
}