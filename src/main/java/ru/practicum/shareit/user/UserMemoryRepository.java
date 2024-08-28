package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataOperationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
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
                .filter(user -> {
                    if (user.getEmail() != null) {
                        return user.getEmail().equals(email);
                    } else {
                        return false;
                    }
                })
                .findFirst();
    }

    @Override
    public User crate(User user) {
        testValid(user);
        // при добавлении нового пользователя, id переданного элемента неактуален, заменить
        user.setId(generateUserId());
        if (users.add(user)) {
            return user;
        } else {
            rollbackUserId();
            throw new DataOperationException(String
                    .format("Не удалось добавить пользователя. Неуникальный пользователь userId = %d", user.getId()));
        }
    }

    @Override
    public User update(User oldUser, User newUser) {
        // судя по тестам, переданный пользователь должен дополнить, а не заменить исходного
        patchUser(newUser, oldUser);
        if (users.remove(oldUser) && users.add(newUser)) {
            return newUser;
        } else {
            throw new DataOperationException(String
                    .format("Не удалось обновить пользователя userId = %d", oldUser.getId()));
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

    protected void testValid(User user) {
        if (users.parallelStream()
                .anyMatch(u ->
                        (u.getName().equals(user.getName())) || (u.getEmail().equals(user.getEmail()))
                )) {
            throw new ValidationException(String.format("Неуникальный пользователь userId = %d", user.getId()));
        }

    }

    public void patchUser(User newUser, User oldUser) {
        if (newUser.getName() == null) {
            newUser.setName(oldUser.getName());
        }
        if (newUser.getEmail() == null) {
            newUser.setEmail(oldUser.getEmail());
        }
        if (newUser.getBirthday() == null) {
            newUser.setBirthday(oldUser.getBirthday());
        }
        if (newUser.getLogin() == null) {
            newUser.setLogin(oldUser.getLogin());
        }
    }

    private Long generateUserId() {
        return ++userId;
    }

    private void rollbackUserId() {
        userId--;
    }
}
