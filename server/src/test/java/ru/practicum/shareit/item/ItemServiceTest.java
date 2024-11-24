package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(ItemService.class)

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
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    UserRepository userRepository;

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

    private ItemDto createItemDto(String name) {
        return  ItemDto.builder()
                .name(name)
                .description(name + "description")
                .available(true)
                .build();
    }

    @Test
    void createItem() {
        UserDto userDto = createUser("Имя1", "email1@yandex.ru", "login1", LocalDate.now().minusYears(20));
        ItemDto itemDto = createItemDto("itemName");
        ItemDto obtainedItem = itemService.createItem(itemDto, userDto.getId());

        assertThat(obtainedItem, notNullValue());
        assertThat(obtainedItem.getId(), greaterThan(0L));
        assertThat(obtainedItem.getName(), equalTo(itemDto.getName()));
    }

    @Test
    void getAllUserItems() {
        UserDto user1Dto = createUser("Имя1", "email1@yandex.ru", "login1", LocalDate.now().minusYears(20));
        UserDto user2Dto = createUser("Имя2", "email2@yandex.ru", "login2", LocalDate.now().minusYears(15));
        UserDto user3Dto = createUser("Имя3", "email3@yandex.ru", "login3", LocalDate.now().minusYears(10));
        ItemDto item1Dto = createItemDto("item1Name");
        ItemDto item2Dto = createItemDto("item2Name");
        ItemDto item3Dto = createItemDto("item3Name");
        item1Dto = itemService.createItem(item1Dto, user1Dto.getId());
        item2Dto = itemService.createItem(item2Dto, user1Dto.getId());
        item3Dto = itemService.createItem(item3Dto, user2Dto.getId());

        List<ItemDto> obtainedItems = itemService.getAllUserItems(user1Dto.getId());
        assertThat(obtainedItems.size(), equalTo(2));
        assertThat(obtainedItems.get(0).getId(), equalTo(item1Dto.getId()));
        assertThat(obtainedItems.get(0).getName(), equalTo(item1Dto.getName()));
        assertThat(obtainedItems.get(1).getId(), equalTo(item2Dto.getId()));
        assertThat(obtainedItems.get(1).getName(), equalTo(item2Dto.getName()));

        obtainedItems = itemService.getAllUserItems(user2Dto.getId());
        assertThat(obtainedItems.size(), equalTo(1));
        assertThat(obtainedItems.getFirst().getId(), equalTo(item3Dto.getId()));
        assertThat(obtainedItems.getFirst().getName(), equalTo(item3Dto.getName()));

        obtainedItems = itemService.getAllUserItems(user3Dto.getId());
        assertThat(obtainedItems.size(), equalTo(0));
    }
}