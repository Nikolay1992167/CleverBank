package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.data.user.request.RequestUserDto;
import ru.clevertec.data.user.response.ResponseUserDto;
import ru.clevertec.entity.User;

@Mapper
public interface UserMapper {
    /**
     * Mapping request dto to user
     *
     * @param source current request dto
     * @return user without ID
     */
    @Mapping(target = "id", ignore = true)
    User getUser(RequestUserDto source);

    /**
     * Mapping user to response dto
     *
     * @param source current user
     * @return response dto
     */
    ResponseUserDto getResponseDto(User source);
}
