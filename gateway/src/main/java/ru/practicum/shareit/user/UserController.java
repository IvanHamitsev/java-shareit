package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto user) {
        log.warn("Create user {}", user.getName());
        return userClient.createUser(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto,
                                             @PathVariable(name = "id") long updatedUserId) {
        userDto.setId(updatedUserId);
        log.warn("Update user {} with id = {}", userDto.getName(), userDto.getId());
        return userClient.updateUser(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.warn("Get All users");
        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        log.warn("Get user by id = {}", id);
        return userClient.getUserById(id);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "userId") long deletedUserId) {
        log.warn("Delete user {}", deletedUserId);
        return userClient.deleteUserById(deletedUserId);
    }
}
