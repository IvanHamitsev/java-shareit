package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Простые методы уже есть в стандартном JpaRepository
    //List<User> findAll();
    //Optional<User> findById(long userId);
    //User crate(User user);
    //User update(User oldUser, User newUser);
    //User delete(long userId);

    Optional<User> findByEmailIgnoreCase(String email);
}
