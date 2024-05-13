package ru.practicum.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ValidationException;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserMapper;
import ru.practicum.users.entity.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    @Autowired
    public AdminUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<User> result;
        if ((ids == null) || (ids.size() == 0)) {
            result = userRepository.findAll(pageable).toList();
        } else {
            result = userRepository.findByIdIn(ids, pageable);
        }

        return result.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto addUsers(UserDto userDto) {
        validation(userDto);
        try {
            return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
        } catch (RuntimeException e) {
            throw new ConflictException("Ошибка при сохранении пользователя ( " + userDto + " )");
        }
    }

    @Override
    @Transactional
    public void delUsers(Long userId) {
        userRepository.deleteById(userId);
    }

    private void validation(UserDto userDto) {
        if (userDto == null) {
            throw new ValidationException("Передано пустое тельло пользователя.");
        }
        if ((userDto.getEmail() == null) || (userDto.getEmail().isBlank())) {
            throw new ValidationException("Почта не может быть пустым.");
        }
        if ((userDto.getName() == null) || (userDto.getName().isBlank())) {
            throw new ValidationException("Имя не может быть пустым.");
        }
    }
}
