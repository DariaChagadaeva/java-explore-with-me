package ru.practicum.service.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.exception.EntityNotFoundException;
import ru.practicum.service.user.dto.NewUserRequest;
import ru.practicum.service.user.dto.UserDto;
import ru.practicum.service.user.mapper.UserMapper;
import ru.practicum.service.user.model.User;
import ru.practicum.service.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto addUser(NewUserRequest newUserRequest) {
        log.info("Method addUser invoke");
        UserDto user = userMapper.fromModelToDto(userRepository.save(userMapper.fromDtoToModel(newUserRequest)));
        log.info("New user added {}", user);
        return user;
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        log.info("Method getUsers invoke");
        Pageable page = PageRequest.of(from / size, size);
        log.info("Get information about users");
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(page).getContent().stream()
                    .map(userMapper::fromModelToDto).collect(Collectors.toList());
        } else {
            return userRepository.findAllByIdIn(ids, page).getContent().stream()
                    .map(userMapper::fromModelToDto).collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Method deleteUser invoke");
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        log.info("User {} deleted", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User findUserById(Long userId) {
        log.info("Get user id = " + userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
