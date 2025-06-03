package eshop.controller;

import eshop.domain.User;
import eshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserResponseControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void createUserTest() throws Exception {

        User user = new User();
        user.setUsername("user456");
        user.setRealName("Test User");
        user.setEmail("testuser123@gmail.com");
        user.setPassword("testpass");

        Mockito.when(userService.registerUser(Mockito.any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .with(user("anyUser"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"username\": \"user456\",\n" +
                                "  \"realName\": \"Test User\",\n" +
                                "  \"email\": \"testuser123@gmail.com\",\n" +
                                "  \"password\": \"testpass\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

}
