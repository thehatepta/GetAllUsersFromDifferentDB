package org.testTask.contollers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testTask.Services.Interfaces.UsersServiceInterface;
import org.testTask.databases.entity.User;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UsersServiceInterface usersService;
    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        // Setup mock service response
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
        List<User> users = List.of(new User("name1", "username1", "surname1"));
        when(usersService.findAllUsers()).thenReturn(users);
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Perform GET request and verify the response
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name1"));
    }
}

