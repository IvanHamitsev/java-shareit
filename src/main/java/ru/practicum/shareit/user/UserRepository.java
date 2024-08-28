package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAllUsers();

    Optional<User> getById(long userId);

    Optional<User> getByEmail(String email);

    User crate(User user);

    User update(User oldUser, User newUser);

    User delete(long userId);
}
