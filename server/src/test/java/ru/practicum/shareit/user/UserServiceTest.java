package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(UserService.class)

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
class UserServiceTest {

    @Autowired
    private final UserService userService;

    private UserDto createUserDto(String name, String email, String login, LocalDate birthday) {
        return UserDto.builder()
                .name(name)
                .email(email)
                .login(login)
                .birthday(birthday)
                .build();
    }

    @Test
    void testCreateUpdateGetDeleteUsers() {
        UserDto user1Dto = createUserDto("Имя1", "email1@yandex.ru", null, LocalDate.now().minusYears(20));
        UserDto user2Dto = createUserDto("Имя2", "email2@yandex.ru", "login2", LocalDate.now().minusYears(15));
        UserDto user3Dto = createUserDto("Имя3", "email2@yandex.ru", "login3", LocalDate.now().minusYears(10));

        UserDto obtainedUser = userService.createUser(user1Dto);
        assertThat(obtainedUser.getLogin(), notNullValue());

        user1Dto.setId(obtainedUser.getId());
        assertThat(obtainedUser, notNullValue());
        assertThat(obtainedUser.getId(), greaterThan(0L));
        assertThat(obtainedUser.getName(), equalTo(user1Dto.getName()));

        obtainedUser.setName("anotherUserName");

        obtainedUser = userService.updateUser(obtainedUser);
        assertThat(obtainedUser, notNullValue());
        assertThat(obtainedUser.getId(), equalTo(user1Dto.getId()));
        assertThat(obtainedUser.getName(), not(user1Dto.getName()));

        obtainedUser = userService.getUserById(user1Dto.getId());
        assertThat(obtainedUser, notNullValue());
        assertThat(obtainedUser.getId(), equalTo(user1Dto.getId()));
        assertThat(obtainedUser.getName(), not(user1Dto.getName()));

        List<UserDto> usersList = userService.getAllUsers();
        assertThat(usersList.size(), equalTo(1));
        assertThat(usersList.getFirst().getId(), equalTo(user1Dto.getId()));

        obtainedUser = userService.createUser(user2Dto);
        user2Dto.setId(obtainedUser.getId());

        assertThrows(ValidationException.class, () -> userService.createUser(user3Dto));
        user3Dto.setEmail("email3@yandex.ru");
        obtainedUser = userService.createUser(user3Dto);
        user3Dto.setId(obtainedUser.getId());

        usersList = userService.getAllUsers();
        assertThat(usersList.size(), equalTo(3));
        assertThat(usersList.get(0).getId(), equalTo(user1Dto.getId()));
        assertThat(usersList.get(1).getId(), equalTo(user2Dto.getId()));
        assertThat(usersList.get(2).getId(), equalTo(user3Dto.getId()));

        userService.deleteUserById(user1Dto.getId());

        usersList = userService.getAllUsers();
        assertThat(usersList.size(), equalTo(2));
        assertThat(usersList.get(0).getId(), equalTo(user2Dto.getId()));
        assertThat(usersList.get(1).getId(), equalTo(user3Dto.getId()));
    }
}