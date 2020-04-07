package com.example.ec.web;

import com.example.ec.domain.Role;
import com.example.ec.domain.User;
import com.example.ec.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.GET;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtRequestHelper jwtRequestHelper;

    @MockBean
    private UserService service;

    private LoginDto signupDto = new LoginDto("larry", "1234", "larry", "miller");
    private User user = new User(signupDto.getUsername(), signupDto.getPassword(),
            signupDto.getFirstName(), signupDto.getLastName(), new Role());

    @Test
    public void signin() {
        restTemplate.postForEntity("/users/signin", new LoginDto("admin", "myPass"), Void.class);
        verify(this.service).signIn("admin","myPass");
    }

    @Test
    public void signup(){
        when(service.signUp(signupDto.getUsername(), signupDto.getPassword(), signupDto.getFirstName(),
                signupDto.getLastName())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = restTemplate.exchange("/users/signup",
                POST,
                new HttpEntity(signupDto, jwtRequestHelper.withRole("ROLE_ADMIN")),
                User.class);

        Assertions.assertEquals(201, response.getStatusCode().value());
        Assertions.assertEquals(user.getUsername(), response.getBody().getUsername());
        Assertions.assertEquals(user.getFirstName(), response.getBody().getFirstName());
        Assertions.assertEquals(user.getLastName(), response.getBody().getLastName());
        Assertions.assertEquals(user.getRoles().size(), response.getBody().getRoles().size());
    }

    @Test
    public void signupUnauthorized(){

        ResponseEntity<User> response = restTemplate.exchange("/users/signup",
                POST,
                new HttpEntity(signupDto, jwtRequestHelper.withRole("ROLE_CSR")),
                User.class);

        Assertions.assertEquals(403, response.getStatusCode().value());
    }

    @Test
    public void getAllUsers() {
        when(service.getAll()).thenReturn(Arrays.asList(user));

        ResponseEntity<List<User>> response = restTemplate.exchange("/users",
                GET,
                new HttpEntity(jwtRequestHelper.withRole("ROLE_ADMIN")),
                new ParameterizedTypeReference<List<User>>() {});

        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(user.getUsername(), response.getBody().get(0).getUsername());
    }
}
