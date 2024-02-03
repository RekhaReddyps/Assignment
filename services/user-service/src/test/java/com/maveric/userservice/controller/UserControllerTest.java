package com.maveric.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.userservice.constant.Gender;
import com.maveric.userservice.dto.UserDto;
import com.maveric.userservice.dto.UserEmailDto;
import com.maveric.userservice.exception.EmailDuplicateException;
import com.maveric.userservice.exception.UserNotFoundException;
import com.maveric.userservice.feignclient.FeignUserService;
import com.maveric.userservice.model.User;
import com.maveric.userservice.repository.UserRepository;
import com.maveric.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private UserService userService;

    @MockBean
    FeignUserService feignUserService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_V1_USERS = "/api/v1/users";
    private static final String API_V1_USERS_EMAIL = "/api/v1/users/getUserByEmail";

    @Test
    void createUser() throws Exception{
        when(userService.createUser(any(UserDto.class))).thenReturn(getUserDto());
        mockMvc.perform(post(API_V1_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUser()))).andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorWhenCreateUserDetailsAreWrong() throws Exception{
        UserDto user = new UserDto();
        user.setId("1l");
        user.setFirstName(null);
        user.setMiddleName("D");
        user.setLastName("Jain");
        user.setAddress("Mumbai");
        user.setGender(Gender.MALE);
        user.setEmail("hinsj@maveric-systems.com");
        user.setPassword("Pass@word1");
        user.setDateOfBirth(Date.from(Instant.parse("1994-10-27T00:00:00Z")));
        user.setPhoneNumber("9594484384");

        mockMvc.perform(post(API_V1_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(any(UserDto.class), anyString())).thenReturn(getUserDto());
        mockMvc.perform(put(API_V1_USERS + "/" +"1L")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUser())).header("userid", "1L"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void errorWithDifferentUserIdForUpdateUser() throws Exception {
        when(userService.updateUser(any(UserDto.class), anyString())).thenReturn(getUserDto());
        mockMvc.perform(put(API_V1_USERS + "/" +"1L")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUser())).header("userid", "2L"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorWhenUpdateUserDetailsAreWrong() throws Exception{
        UserDto user = new UserDto();
        user.setId("1l");
        user.setFirstName(null);
        user.setMiddleName("D");
        user.setLastName("Jain");
        user.setAddress("Mumbai");
        user.setGender(Gender.MALE);
        user.setEmail("hinsj@maveric-systems.com");
        user.setDateOfBirth(Date.from(Instant.parse("1994-10-27T00:00:00Z")));
        user.setPhoneNumber("9594484384");

        mockMvc.perform(put(API_V1_USERS + "/" +"1L")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers(anyInt(), anyInt())).thenReturn(Arrays.asList(getUserDto(), getUserDto()));
        mockMvc.perform(get(API_V1_USERS + "?page=0&pageSize=2"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getUserById() throws Exception{
        when(userService.getUserById(anyString())).thenReturn(getUserDto());
        mockMvc.perform(get(API_V1_USERS + "/" + "1L").header("userid", "1L"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnErrorWhenWrongUserIdForGetUserByID() throws Exception{
        when(userService.getUserById(anyString())).thenThrow(new UserNotFoundException("User Not found"));
        mockMvc.perform(get(API_V1_USERS+"/" + "2L").header("userid", "1L"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getUserByEmail() throws Exception{
        when(userService.getUserByEmail(any())).thenReturn(getUserEmailDto());
        mockMvc.perform(get(API_V1_USERS_EMAIL + "/hinsj@maveric-systems.com"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnErrorWhenWWrongEmailForGetUserByEmail() throws Exception{
        when(userService.getUserByEmail(anyString())).thenThrow(new EmailDuplicateException("User not found with email"));
        mockMvc.perform(get(API_V1_USERS_EMAIL+"/hinsj@gmail.com"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void deleteUser() throws Exception{
        mockMvc.perform(delete(API_V1_USERS + "/" + "1L").header("userid", "1L"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldThrowErrorFordeleteUserWhenWrongUserId() throws Exception{
        mockMvc.perform(delete(API_V1_USERS + "/" + "1L").header("userid", "2L"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    public static User getUser() {
        User user = new User();
        user.setId("1l");
        user.setFirstName("Hins");
        user.setMiddleName("D");
        user.setLastName("Jain");
        user.setAddress("Mumbai");
        user.setGender(Gender.MALE);
        user.setEmail("hinsj@maveric-systems.com");
        user.setPassword("Pass@word1");
        user.setDateOfBirth(Date.from(Instant.parse("1994-10-27T00:00:00Z")));
        user.setPhoneNumber("9594484384");

        return user;
    }

    public static UserDto getUserDto() {
        UserDto user = new UserDto();
        user.setId("1l");
        user.setFirstName("Hins");
        user.setMiddleName("D");
        user.setLastName("Jain");
        user.setAddress("Mumbai");
        user.setGender(Gender.MALE);
        user.setEmail("hinsj@maveric-systems.com");
        user.setPassword("Pass@word1");
        user.setDateOfBirth(Date.from(Instant.parse("1994-10-27T00:00:00Z")));
        user.setPhoneNumber("9594484384");

        return user;
    }

    public static UserEmailDto getUserEmailDto() {
        UserEmailDto user = new UserEmailDto();
        user.setId("1l");
        user.setFirstName("Hins");
        user.setMiddleName("D");
        user.setLastName("Jain");
        user.setAddress("Mumbai");
        user.setGender(Gender.MALE);
        user.setEmail("hinsj@maveric-systems.com");
        user.setPassword("Pass@word1");
        user.setDateOfBirth(Date.from(Instant.parse("1994-10-27T00:00:00Z")));
        user.setPhoneNumber("9594484384");

        return user;
    }
}