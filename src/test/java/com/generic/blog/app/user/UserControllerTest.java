package com.generic.blog.app.user;

import com.generic.blog.app.auth.EnumRole;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    UserEntity userEntity;


    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .username("authUser")
                .active(true)
                .displayName("displayName")
                .role(EnumRole.USER)
                .build();
    }

    @Test
    public void UserController_GetUserById_ReturnsUserEntity() throws Exception{
        when(userService.findUserById(Mockito.any(Long.class))).thenReturn(userEntity);

        ResultActions resultActions = mockMvc.perform(get("/api/user/0"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(userEntity.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName", CoreMatchers.is(userEntity.getDisplayName())));
    }

    @Test
    public void userController_GetLoggedInUser_ReturnsUserEntity() throws Exception{
        when(userService.getCurrentUser()).thenReturn(userEntity);

        ResultActions resultActions = mockMvc.perform(get("/api/user/logged-in"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(userEntity.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName", CoreMatchers.is(userEntity.getDisplayName())));
    }

    @Test
    public void userController_GetUsersPage_ReturnsUserEntityPage() throws Exception{
        Page<UserEntity> users = new PageImpl<UserEntity>(List.of(userEntity));

        when(userService.getUsersPage(Mockito.anyInt(),Mockito.anyInt())).thenReturn(users);

        ResultActions resultActions = mockMvc.perform(get("/api/user/0/10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size",CoreMatchers.is(users.getSize())));
    }

    @Test
    public void userController_GetUsersPageSearch_ReturnsUserEntityPage() throws Exception{
        Page<UserEntity> users = new PageImpl<UserEntity>(List.of(userEntity));

        when(userService.getUserPageUsernameSearch(Mockito.anyInt(),Mockito.anyInt(), Mockito.anyString())).thenReturn(users);

        ResultActions resultActions = mockMvc.perform(get("/api/user/0/10/search"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size",CoreMatchers.is(users.getSize())));
    }
}