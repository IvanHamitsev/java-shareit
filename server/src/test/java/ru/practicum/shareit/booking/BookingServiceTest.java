package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestType;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(BookingService.class)

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
/*
@TestPropertySource(properties = {"spring.datasource.url=jdbc:postgresql://localhost:5432/shareit-test",
        "spring.datasource.username=testUser",
        "spring.datasource.password=testpass"})
*/
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:file:./db/shareit-test",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.sql.init.schema-locations=classpath:test-schema.sql"})
@DataJpaTest
class BookingServiceTest {
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ItemRepository itemRepository;

    private UserDto createUser(String name, String email, String login, LocalDate birthday) {
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

    private ItemDto createItem(String name, String description, long ownerId, ItemRequest request) {
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
                .isAvailableForRent(true)
                .build();
        var result = itemRepository.save(item);
        itemDto.setId(result.getId());
        return itemDto;
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

    @Test
    void getAllUserBookings() {
        UserDto userDto = createUser("Имя1", "email1@yandex.ru", "login1", LocalDate.now().minusYears(20));
        UserDto anotherUserDto = createUser("Имя2", "email2@yandex.ru", "login2", LocalDate.now().minusYears(20));
        UserDto ownerDto = createUser("Имя3", "email3@yandex.ru", "login3", LocalDate.now().minusYears(21));
        ItemDto item1Dto = createItem("Item1Name", "Description1", ownerDto.getId(), null);
        ItemDto item2Dto = createItem("Item2Name", "Description2", ownerDto.getId(), null);
        ItemDto item3Dto = createItem("Item3Name", "Description3", ownerDto.getId(), null);
        ItemDto item4Dto = createItem("Item4Name", "Description4", ownerDto.getId(), null);
        BookingDto bookingDto = createBookingDto(item1Dto.getId(), userDto);
        BookingDto booking1Dto = bookingService.createBooking(bookingDto, userDto.getId());
        bookingDto = createBookingDto(item2Dto.getId(), userDto);
        BookingDto booking2Dto = bookingService.createBooking(bookingDto, userDto.getId());
        bookingDto = createBookingDto(item3Dto.getId(), userDto);
        BookingDto booking3Dto = bookingService.createBooking(bookingDto, userDto.getId());
        bookingDto = createBookingDto(item4Dto.getId(), anotherUserDto);
        BookingDto booking4Dto = bookingService.createBooking(bookingDto, anotherUserDto.getId());
        List<BookingDto> resultBookingList = bookingService.getAllUserBookings(userDto.getId(), RequestType.WAITING, 0, 10);

        assertThat(resultBookingList, equalTo(List.of(booking1Dto, booking2Dto, booking3Dto)));

        resultBookingList = bookingService.getAllUserBookings(userDto.getId(), RequestType.WAITING, 1, 1);

        assertThat(resultBookingList, equalTo(List.of(booking2Dto)));
    }

    @Test
    void getAllOwnerBookings() {
        UserDto userDto = createUser("Имя1", "email1@yandex.ru", "login1", LocalDate.now().minusYears(20));
        UserDto anotherUserDto = createUser("Имя2", "email2@yandex.ru", "login2", LocalDate.now().minusYears(20));
        UserDto ownerDto = createUser("Имя3", "email3@yandex.ru", "login3", LocalDate.now().minusYears(21));
        ItemDto item1Dto = createItem("Item1Name", "Description1", ownerDto.getId(), null);
        ItemDto item2Dto = createItem("Item2Name", "Description2", ownerDto.getId(), null);
        ItemDto item3Dto = createItem("Item3Name", "Description3", ownerDto.getId(), null);
        ItemDto item4Dto = createItem("Item4Name", "Description4", anotherUserDto.getId(), null);

        BookingDto bookingDto = createBookingDto(item1Dto.getId(), userDto);
        BookingDto booking1Dto = bookingService.createBooking(bookingDto, userDto.getId());

        bookingDto = createBookingDto(item2Dto.getId(), userDto);
        BookingDto booking2Dto = bookingService.createBooking(bookingDto, userDto.getId());

        bookingDto = createBookingDto(item3Dto.getId(), userDto);
        BookingDto booking3Dto = bookingService.createBooking(bookingDto, userDto.getId());

        bookingDto = createBookingDto(item4Dto.getId(), userDto);
        BookingDto booking4Dto = bookingService.createBooking(bookingDto, userDto.getId());

        List<BookingDto> resultBookingList = bookingService.getAllOwnerBookings(ownerDto.getId(), RequestType.PAST);
        assertThat(resultBookingList, equalTo(List.of(booking1Dto, booking2Dto, booking3Dto)));

        resultBookingList = bookingService.getAllOwnerBookings(ownerDto.getId(), RequestType.ALL);
        assertThat(resultBookingList, equalTo(List.of(booking1Dto, booking2Dto, booking3Dto)));

        resultBookingList = bookingService.getAllOwnerBookings(ownerDto.getId(), RequestType.FUTURE);
        assertThat(resultBookingList, equalTo(List.of()));
    }

    @Test
    void createBooking() {
        UserDto userDto = createUser("Имя1", "email1@yandex.ru", "login1", LocalDate.now().minusYears(20));
        UserDto ownerDto = createUser("Имя2", "email2@yandex.ru", "login2", LocalDate.now().minusYears(21));
        ItemDto itemDto = createItem("Item1Name", "Description1", ownerDto.getId(), null);
        BookingDto bookingDto = createBookingDto(itemDto.getId(), userDto);
        BookingDto result = bookingService.createBooking(bookingDto, userDto.getId());

        assertThat(result, notNullValue());
        assertThat(result.getBooker(), equalTo(userDto));
        assertThat(result.getItemId(), equalTo(itemDto.getId()));
        assertThat(result.getStart(), equalTo(bookingDto.getStart()));
        assertThat(result.getEnd(), equalTo(bookingDto.getEnd()));
    }

    @Test
    void changeBookingStatus() {
        UserDto userDto = createUser("Имя1", "email1@yandex.ru", "login1", LocalDate.now().minusYears(20));
        UserDto ownerDto = createUser("Имя2", "email2@yandex.ru", "login2", LocalDate.now().minusYears(21));
        ItemDto itemDto = createItem("Item1Name", "Description1", ownerDto.getId(), null);
        BookingDto bookingDto = createBookingDto(itemDto.getId(), userDto);
        bookingDto = bookingService.createBooking(bookingDto, userDto.getId());
        BookingDto result = bookingService.changeBookingStatus(ownerDto.getId(), bookingDto.getId(), true);

        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo("APPROVED"));
    }

    @Test
    void getBookingInfo() {
        UserDto userDto = createUser("Имя1", "email1@yandex.ru", "login1", LocalDate.now().minusYears(20));
        UserDto ownerDto = createUser("Имя2", "email2@yandex.ru", "login2", LocalDate.now().minusYears(21));
        ItemDto itemDto = createItem("Item1Name", "Description1", ownerDto.getId(), null);
        BookingDto bookingDto = createBookingDto(itemDto.getId(), userDto);
        BookingDto createdBooking = bookingService.createBooking(bookingDto, userDto.getId());
        BookingDto obtainedBooking = bookingService.getBookingInfo(userDto.getId(), createdBooking.getId());

        assertThat(obtainedBooking, equalTo(createdBooking));
    }
}