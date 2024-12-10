package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    final UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.warn("Get request for createUser");
        UserDto resultUserDto = userService.createUser(userDto);
        return resultUserDto;
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable long id) {
        userDto.setId(id);
        log.warn("Get request for updateUser");
        return userService.updateUser(userDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.warn("Get request for getAllUsers");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        log.warn("Get request for getUserById");
        return userService.getUserById(id);
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable long userId) {
        log.warn("Get request for deleteUser using userId");
        return userService.deleteUserById(userId);
    }
}
