package com.generic.blog.app.user;

import com.generic.blog.app.auth.EnumRole;
import com.generic.blog.app.auth.models.AuthUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    UserEntity userEntity;

    AuthUser authUser;



    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .username("authUser")
                .active(true)
                .displayName("displayName")
                .role(EnumRole.USER)
                .build();

        authUser = AuthUser.builder()
                .username("authUser")
                .password("password")
                .active(true)
                .displayName("displayName")
                .role(EnumRole.USER)
                .build();
    }

    @Test
    void userService_FindUserById_ReturnsUserEntity() throws Exception{
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(userEntity));

        UserEntity response = userService.findUserById(0L);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void userService_FindUserByUsername_ReturnsUserEntity() throws Exception{
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(userEntity));

        UserEntity response = userService.findUserByUsername("username");

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void userService_GetCurrentUser_ReturnsUserEntity() throws Exception{
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(authUser);
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(userEntity));

        UserEntity response = userService.getCurrentUser();

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void setUserService_GetUserPage_ReturnsUserPage() throws Exception{
        Page<UserEntity> users = mock(Page.class);
        when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(users);

        Page<UserEntity> response = userService.getUsersPage(0,10);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void setUserService_GetUserPageSearch_ReturnsUserPage() throws Exception{
        Page<UserEntity> users = mock(Page.class);
        when(userRepository.findUserEntitiesByUsernameContains(Mockito.any(String.class),Mockito.any(Pageable.class))).thenReturn(users);

        Page<UserEntity> response = userService.getUserPageUsernameSearch(0,10,"search");

        Assertions.assertThat(response).isNotNull();
    }
}