package com.bank.api.controller;

import com.bank.api.dto.UserDto;
import com.bank.api.entity.Role;
import com.bank.api.entity.User;
import com.bank.api.service.UserService;
import com.bank.api.util.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserAdminController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
})
public class UserAdminControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        User user = new User();
        user.setEnabled(true);
        user.setRole(Role.USER);
        user.setUsername("john");
        UserDto user1 = UserDto.fromEntity(user);

        user.setEnabled(true);
        user.setRole(Role.ADMIN);
        user.setUsername("jane");
        UserDto user2 = UserDto.fromEntity(user);

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/admin/users").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("john"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testBlockUser() throws Exception {
        doNothing().when(userService).blockUser(1L);

        mockMvc.perform(patch("/api/admin/user/1/block").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
        verify(userService).blockUser(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testActivateUser() throws Exception {
        doNothing().when(userService).activateUser(1L);

        mockMvc.perform(patch("/api/admin/user/1/activate").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
        verify(userService).activateUser(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUser() throws Exception {
        doNothing().when(userService).createUser(any());

        String jsonBody = """
                {
                    "username": "john",
                    "password": "secret",
                    "role": "USER"
                }
                """;

        mockMvc.perform(post("/api/admin/user").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isOk());

        verify(userService).createUser(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/admin/user/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
        verify(userService).deleteUser(1L);
    }
}
