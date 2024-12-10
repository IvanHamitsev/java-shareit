package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(ItemRequestService.class)

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
class ItemRequestServiceTest {

    @Autowired
    private final ItemRequestService itemRequestService;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RequestRepository requestRepository;

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

    private ItemRequestDto createItemRequest(String name, long userId) {
        //LocalDateTime creationTime = LocalDateTime.now();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .name(name)
                .description(name + "description")
                //.created(creationTime)
                .created(null)
                .build();
        return itemRequestService.createRequest(userId, itemRequestDto);
    }

    @Test
    void getUserRequests() {
        UserDto user1Dto = createUser("Имя1", "email1@yandex.ru", "login1", LocalDate.now().minusYears(20));
        UserDto user2Dto = createUser("Имя2", "email2@yandex.ru", "login2", LocalDate.now().minusYears(15));
        UserDto user3Dto = createUser("Имя3", "email3@yandex.ru", "login3", LocalDate.now().minusYears(10));
        ItemRequestDto itemRequest1Dto = createItemRequest("nameOfRequest1", user1Dto.getId());
        ItemRequestDto itemRequest2Dto = createItemRequest("nameOfRequest2", user1Dto.getId());
        ItemRequestDto itemRequest3Dto = createItemRequest("nameOfRequest3", user2Dto.getId());

        ItemRequestDto result = itemRequestService.getRequestById(itemRequest1Dto.getId());
        assertThat(result.getId(), equalTo(itemRequest1Dto.getId()));
        assertThat(result.getName(), equalTo(itemRequest1Dto.getName()));
        assertThat(result.getDescription(), equalTo(itemRequest1Dto.getDescription()));

        List<ItemRequestDto> listOfUserRequests = itemRequestService.getAllUserRequests(user1Dto.getId());
        assertThat(listOfUserRequests.size(), equalTo(2));
        assertThat(listOfUserRequests.get(0).getId(), equalTo(itemRequest1Dto.getId()));
        assertThat(listOfUserRequests.get(1).getId(), equalTo(itemRequest2Dto.getId()));

        listOfUserRequests = itemRequestService.getAllUserRequests(user2Dto.getId());
        assertThat(listOfUserRequests.size(), equalTo(1));
        assertThat(listOfUserRequests.get(0).getId(), equalTo(itemRequest3Dto.getId()));

        listOfUserRequests = itemRequestService.getAllUserRequests(user3Dto.getId());
        assertThat(listOfUserRequests.size(), equalTo(0));

        listOfUserRequests = itemRequestService.getAllRequests();
        assertThat(listOfUserRequests.size(), equalTo(3));
    }
}