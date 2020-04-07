package com.example.ec.service;

import com.example.ec.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {
    @Autowired
    UserService userService;

    @Test
    public void signup() {
        Optional<User> user = userService.signUp("dummyUsername", "dummyPassword", "john", "doe");
        Assertions.assertNotEquals("dummyPassword", user.get().getPassword());
        System.out.println("Encoded Password = " + user.get().getPassword());
    }
}
