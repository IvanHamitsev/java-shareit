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
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserMapper::mapUser)
                .toList();
    }

    public UserDto getUserById(long userId) {
        return UserMapper.mapUser(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id %d не найден ", userId))));
    }

    public UserDto createUser(UserDto userDto) {
        deepValidate(userDto);
        fixUser(userDto);
        return UserMapper.mapUser(userRepository.save(UserMapper.mapUserDto(userDto)));
    }

    public UserDto updateUser(UserDto newUserDto) {
        // при обновлении, email контролируется только если он заполнен
        if (null != newUserDto.getEmail()) {
            deepValidate(newUserDto);
        }
        User oldUser = userRepository.findById(newUserDto.getId())
                .orElseThrow(() -> new NotFoundException(String
                        .format("Обновление невозможно, переданный id %d пользователя не найден", newUserDto.getId())));

        User newUser = UserMapper.mapUserDto(newUserDto);
        patchUser(newUser, oldUser);
        //return UserMapper.mapUser(userRepository.update(oldUser, UserMapper.mapUserDto(newUser)));
        return UserMapper.mapUser(userRepository.save(newUser));
    }

    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }

    protected void deepValidate(UserDto userDto) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(userDto.getEmail());
        // плохо если:
        // пользователь по email найден, при этом
        // у DTO объекта id = 0 ИЛИ у DTO объекта id != id найденного пользователя
        if ((user.isPresent()) && ((userDto.getId() == 0) || (user.get().getId() != userDto.getId()))) {
            throw new ValidationException(String
                    .format("Адрес почты %s пользователя userId = %d не уникален", userDto.getEmail(), userDto.getId()));
        }
        // если не заполнен
    }

    // необходимые действия над userDto, чтобы он имел минимально необходимые данные
    protected void fixUser(UserDto userDto) {
        // если логин пустой, заменить его на адрес почты - он уникален
        if (userDto.getLogin() == null) {
            userDto.setLogin(userDto.getEmail());
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
}
