package com.maveric.userservice.converter;

import com.maveric.userservice.constant.Gender;
import com.maveric.userservice.dto.UserDto;
import com.maveric.userservice.dto.UserEmailDto;
import com.maveric.userservice.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DtoToModelConverterTest {

    @InjectMocks
    private DtoToModelConverter dtoToModelConverter;

    @Test
    void dtoToUserCreate() {
        UserDto userDto = getUserDto();
        User user = dtoToModelConverter.dtoToUserCreate(userDto);
        assertNotNull(user.getEmail());
        assertSame(user.getEmail(), userDto.getEmail());
    }

    @Test
    void userToDtoCreate() {
        User user = getUser();
        UserDto userDto = dtoToModelConverter.userToDtoCreate(user);
        assertNotNull(userDto.getEmail());
        assertSame(user.getEmail(), userDto.getEmail());
    }

    @Test
    void dtoToUserUpdate() {
        UserDto userDto = getUserDto();
        User user = dtoToModelConverter.dtoToUserUpdate(userDto);
        assertNotNull(user.getEmail());
        assertSame(user.getEmail(), userDto.getEmail());
    }

    @Test
    void userToDtoUpdate() {
        User user = getUser();
        UserDto userDto = dtoToModelConverter.userToDtoUpdate(user);
        assertNotNull(userDto.getEmail());
        assertSame(user.getEmail(), userDto.getEmail());
    }

    @Test
    void userToDtoEmail() {
        User user = getUser();
        UserEmailDto userDto = dtoToModelConverter.userToDtoEmail(user);
        assertNotNull(userDto.getEmail());
        assertSame(user.getEmail(), userDto.getEmail());
    }

    public static User getUser() {
        User user = new User();
        user.setFirstName("Hins");
        user.setMiddleName("D");
        user.setLastName("Jain");
        user.setAddress("Mumbai");
        user.setGender(Gender.MALE);
        user.setEmail("hinsj@maveric-systems.com");
        user.setDateOfBirth(Date.from(Instant.parse("1994-10-27T00:00:00Z")));
        user.setPhoneNumber("9594484384");

        return user;
    }

    public static UserDto getUserDto() {
        UserDto user = new UserDto();
        user.setFirstName("Hins");
        user.setMiddleName("D");
        user.setLastName("Jain");
        user.setAddress("Mumbai");
        user.setGender(Gender.MALE);
        user.setEmail("hinsj@maveric-systems.com");
        user.setDateOfBirth(Date.from(Instant.parse("1994-10-27T00:00:00Z")));
        user.setPhoneNumber("9594484384");

        return user;
    }
}