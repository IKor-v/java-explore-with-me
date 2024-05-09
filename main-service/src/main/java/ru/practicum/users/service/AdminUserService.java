package ru.practicum.users.service;

import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto addUsers(UserDto userDto);

    void delUsers(Long userId);
}
