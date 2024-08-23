package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataOperationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserMemoryRepository implements UserRepository {
    Set<User> users = new HashSet<>();
    private Long userId = 0L;

    @Override
    public List<User> getAllUsers() {
        return users.stream().toList();
    }

    @Override
    public Optional<User> getById(long userId) {

        return users.parallelStream()
                .filter(user -> user.getId() == userId)
                .findFirst();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return users.parallelStream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User crate(User user) {
        // при добавлении нового пользователя, id переданного элемента неактуален, заменить
        user.setId(generateUserId());
        if (users.add(user)) {
            return user;
        } else {
            throw new DataOperationException("Не удалось добавить пользователя. Неуникальный пользователь");
        }
    }

    @Override
    public User update(User oldUser, User newUser) {
        if (users.remove(oldUser) && users.add(newUser)) {
            return newUser;
        } else {
            throw new DataOperationException("Не удалось обновить пользователя");
        }
    }

    @Override
    public User delete(long userId) {
        User user = getById(userId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Не удалось удалить пользователя с id %d, пользователь не найден", userId)));
        users.remove(user);
        return user;
    }

    private Long generateUserId() {
        return ++userId;
    }
}
