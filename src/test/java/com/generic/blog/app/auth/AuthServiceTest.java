package com.generic.blog.app.auth;

import com.generic.blog.app.auth.models.AuthUser;
import com.generic.blog.app.user.UserEntity;
import com.generic.blog.app.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Set<UserEntity> subscriptions;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    private AuthUser authUser;


    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        subscriptions = mock(Set.class);

        authUser = AuthUser.builder()
                .username("authUser")
                .password("password")
                .active(true)
                .displayName("displayName")
                .role(EnumRole.USER)
                .subscriptions(subscriptions)
                .build();

        userEntity = UserEntity.builder()
                .username("userEntity")
                .active(true)
                .displayName("displayName")
                .role(EnumRole.USER)
                .build();
    }

    @Test
    void authService_LoadUserByUsername_ReturnUserDetails() {
        when(authRepository.findByUsername(Mockito.any(String.class)))
                .thenReturn(Optional.of(authUser));

        UserDetails response = authService.loadUserByUsername("username");

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void authService_LoadAuthUserByUsername_ReturnsAuthUser() {
        when(authRepository.findByUsername(Mockito.any(String.class)))
                .thenReturn(Optional.of(authUser));

        UserDetails response = authService.loadUserByUsername("username");

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void authService_GetCurrentUser_ReturnsUser() throws Exception{
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(authUser);
        when(authRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(authUser));

        AuthUser response = authService.getCurrentUser();

        Assertions.assertThat(response).isNotNull();

    }

    @Test
    void authService_RegisterUser_ReturnsAuthUser() throws Exception {
        when(passwordEncoder.encode(Mockito.any(String.class)))
                .thenReturn("password");

        when(authRepository.save(Mockito.any(AuthUser.class)))
                .thenReturn(authUser);

        AuthUser response = authService.registerUser(authUser);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void authService_GetUserById_ReturnsAuthUser() throws Exception {
        when(authRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(authUser));

        AuthUser response = authService.getUserById(0L);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void authService_SubscribeToUser_ReturnsUserEntity() throws Exception{
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);


        when(userService.findUserById(Mockito.any())).thenReturn(userEntity);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(authUser);
        when(authRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(authUser));

        when(authRepository.save(Mockito.any(AuthUser.class))).thenReturn(authUser);
        when(subscriptions.add(Mockito.any(UserEntity.class))).thenReturn(true);

        UserEntity response = authService.subscribeToUser(userEntity);

        Assertions.assertThat(response).isNotNull();

    }

    @Test
    void authService_UnsubscribeToUser_ReturnsUserEntity() throws Exception{
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(userService.findUserById(Mockito.any())).thenReturn(userEntity);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(authUser);
        when(authRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(authUser));

        when(authRepository.save(Mockito.any(AuthUser.class))).thenReturn(authUser);
        when(subscriptions.remove(Mockito.any(UserEntity.class))).thenReturn(true);

        UserEntity response = authService.unsubscribeToUser(userEntity);

        Assertions.assertThat(response).isNotNull();

    }
}