package eshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ApiSecurityMvcTest {

    private static final String PRODUCT = """
            {"name": "Test product", "description": "desc", "price": 99.90, "availableAmount": 5}
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void catalogueIsPublic() throws Exception {
        mockMvc.perform(get("/api/products")).andExpect(status().isOk());
        mockMvc.perform(get("/api/categories")).andExpect(status().isOk());
        mockMvc.perform(get("/api/checkout/options"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryMethods", hasSize(3)));
    }

    @Test
    void anonymousUserCannotOrderOrSeeAccount() throws Exception {
        mockMvc.perform(get("/api/orders")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/admin/stats")).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(PRODUCT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidTokenIsRejected() throws Exception {
        mockMvc.perform(get("/api/orders").header("Authorization", "Bearer not-a-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "customer", roles = "USER")
    void customerCannotUseAdministration() throws Exception {
        mockMvc.perform(get("/api/admin/stats")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/admin/users")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(PRODUCT))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/categories/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "boss", roles = "ADMIN")
    void adminManagesCatalogue() throws Exception {
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(PRODUCT))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test product"))
                .andExpect(jsonPath("$.active").value(true));
        mockMvc.perform(get("/api/admin/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productCount").value(1));
    }

    @Test
    @WithMockUser(username = "boss", roles = "ADMIN")
    void invalidProductIsRejectedWithFieldErrors() throws Exception {
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"price\": -5, \"availableAmount\": 1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.price").exists());
    }

    @Test
    void registeredUserSignsInAndUsesToken() throws Exception {
        String registration = """
                {"username": "newbie", "realName": "New User", "email": "newbie@example.com", "password": "secret1"}
                """;
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(registration))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.role").value("USER"));
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(registration))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"newbie\", \"password\": \"wrong\"}"))
                .andExpect(status().isUnauthorized());

        String response = mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"newbie\", \"password\": \"secret1\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String token = objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newbie"));
        mockMvc.perform(get("/api/admin/stats").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
