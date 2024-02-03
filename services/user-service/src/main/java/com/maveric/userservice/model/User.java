package com.maveric.userservice.model;

import com.maveric.userservice.constant.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name= "user")

public class User {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String firstName;

    private String middleName;

    private String lastName;

    //@Column(nullable = false)
    private String email;

    //@Column(nullable = false)
    private String phoneNumber;

    //@Column(nullable = false)
    private String address;

    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String password;

    @Column(updatable = false)
    private Date createdAt = new Date();

    private Date updatedAt = new Date();
}
