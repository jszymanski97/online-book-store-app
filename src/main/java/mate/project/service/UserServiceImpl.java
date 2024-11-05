package mate.project.service;

import lombok.RequiredArgsConstructor;
import mate.project.dto.UserRegistrationRequestDto;
import mate.project.dto.UserResponseDto;
import mate.project.exception.RegistrationException;
import mate.project.mapper.UserMapper;
import mate.project.model.User;
import mate.project.repository.user.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()) != null) {
            throw new RegistrationException("User with email: " + requestDto.getEmail()
                    + " already exists");
        }
        User model = userMapper.toModel(requestDto);
        try {
            User savedUser = userRepository.save(model);
            return userMapper.toUserResponseDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new RegistrationException("User with email: " + requestDto.getEmail()
                    + " already exists");
        }
    }
}
