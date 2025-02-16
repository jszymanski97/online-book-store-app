package mate.project.mapper;

import mate.project.config.MapperConfig;
import mate.project.dto.UserRegistrationRequestDto;
import mate.project.dto.UserResponseDto;
import mate.project.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);
}
