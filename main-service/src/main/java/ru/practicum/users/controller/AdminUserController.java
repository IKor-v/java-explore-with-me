package ru.practicum.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.AdminUserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @Autowired
    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        List<UserDto> result = adminUserService.getUsers(ids, from, size);
        log.info("Запрос всех пользователей.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<UserDto> addNewUser(@Valid @RequestBody UserDto userDto) {
        UserDto result = adminUserService.addUsers(userDto);
        log.info("Добавил пользователя: {}", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delUser(@PathVariable Long userId) {
        adminUserService.delUsers(userId);
        log.info("Удалил пользователя с id = {}", userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
