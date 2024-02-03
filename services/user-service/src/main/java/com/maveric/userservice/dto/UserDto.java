package com.maveric.userservice.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.maveric.userservice.constant.Gender;
import com.maveric.userservice.constraints.BirthDateValidator;
import com.maveric.userservice.constraints.GenderValidator;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private String id;

    @NotEmpty(message = "First name cannot be blank")
    private String firstName;
    private String middleName;

    @NotEmpty(message = "Last name cannot be blank")
    private String lastName;

    @Email(regexp = "[a-z0-9._%+-]+@[a-zA-Z]+-[a-zA-Z]+\\.[a-zA-Z]+",
            message = "Must be a well formed email address")
    @NotEmpty(message = "Email cannot be blank")
    private String email;

    @NotEmpty
    @Size(min = 10, max = 10, message = "Phone Number cannot be blank")
    private String phoneNumber;

    @NotEmpty(message = "Address cannot be blank")
    private String address;

    @NotNull
    @BirthDateValidator(message = "The user must be greater then 18 years")
    @Past(message = "The date of birth must be in the past")
    private Date dateOfBirth;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain atleast one upper case, one lower case, one number and one special character")
    private String password;

    @Enumerated(EnumType.STRING)
    @GenderValidator(anyOfTheseGender = {Gender.MALE, Gender.FEMALE})
    private Gender gender;
}
