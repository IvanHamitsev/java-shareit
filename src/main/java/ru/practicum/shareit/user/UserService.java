package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;
    final ItemRepository itemRepository;

    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.getAllUsers();
        return userList.stream()
                .map(UserMapper::mapUser)
                .toList();
    }

    public UserDto getUserById(long userId) {
        return UserMapper.mapUser(userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id %d не найден ", userId))));
    }

    public UserDto createUser(UserDto userDto) {
        deepValidate(userDto);
        return UserMapper.mapUser(userRepository.crate(UserMapper.mapUserDto(userDto)));
    }

    public UserDto updateUser(UserDto newUser) {
        deepValidate(newUser);
        User oldUser = userRepository.getById(newUser.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Обновление невозможно, переданный id %d пользователя не найден", newUser.getId())));

        return UserMapper.mapUser(userRepository.update(oldUser, UserMapper.mapUserDto(newUser)));
    }

    public UserDto deleteUserById(long userId) {
        return UserMapper.mapUser(userRepository.delete(userId));
    }

    protected void deepValidate(UserDto userDto) {
        Optional<User> user = userRepository.getByEmail(userDto.getEmail());
        // плохо если:
        // пользователь по email найден, при этом
        // у DTO объекта id = 0 ИЛИ у DTO объекта id != id найденного пользователя

        if ((user.isPresent()) && ((userDto.getId() == 0) || (user.get().getId() != userDto.getId()))) {
            throw new ValidationException("Адрес почты пользователя не уникален");
        }
    }
}
