package mate.project.service;

import mate.project.dto.UserRegistrationRequestDto;
import mate.project.dto.UserResponseDto;
import mate.project.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
