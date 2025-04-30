package com.radiuk.securityexample.config;

import com.radiuk.securityexample.dto.UserRegistrationDTO;
import com.radiuk.securityexample.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapperConfig {
    UserMapperConfig INSTANCE = Mappers.getMapper(UserMapperConfig.class);

    UserRegistrationDTO userToUserRegistrationDTO(User user);
    User UserRegistrationDTOToUser(UserRegistrationDTO userRegistrationDTO);
}
