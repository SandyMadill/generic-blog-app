package com.generic.blog.app.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.blog.app.auth.models.AuthRequest;
import com.generic.blog.app.auth.models.AuthUser;
import com.generic.blog.app.auth.utils.JwtUtil;
import com.generic.blog.app.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;


    private AuthUser authUser;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() throws Exception {

        authUser = AuthUser.builder()
                .username("authUser")
                .password("password")
                .active(true)
                .displayName("displayName")
                .role(EnumRole.USER)
                .subscriptions(Set.of())
                .build();

        userEntity = UserEntity.builder()
                .username("userEntity")
                .active(true)
                .displayName("displayName")
                .role(EnumRole.USER)
                .build();

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willAnswer(invo ->{
            UsernamePasswordAuthenticationToken request = invo.getArgument(0);

            if( objectMapper.writeValueAsString(AuthRequest.builder()
                            .username(request.getName())
                            .password(request.getCredentials().toString())
                            .build()
                    )
                    .equals(objectMapper.writeValueAsString(AuthRequest.builder()
                            .username(authUser.getUsername())
                            .password(authUser.getPassword())
                            .build()
                    ))
            ){
                return new TestingAuthenticationToken(request.getPrincipal(),request.getCredentials());
            }
            else
            {
                return new BadCredentialsException("Username or password incorrect");
            }
        });
    }

    @Test
    public void authController_CorrectLogin_ReturnsToken() throws Exception {
        when(authService.loadUserByUsername(Mockito.anyString())).thenReturn(authUser);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("Token");

        AuthRequest authRequest = AuthRequest.builder()
                .username(authUser.getUsername())
                .password(authUser.getPassword())
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/auth/login", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)));

        mockMvc.perform(post("/api/auth/login", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        );

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void authController_WrongPasswordLogin_ReturnsBadCredentialsException() throws Exception {
        AuthRequest authRequest = AuthRequest.builder()
                .username(authUser.getUsername())
                .password("Wrong_Password")
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        );

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void authController_SubscribeToUser_ReturnsUserEntitySet() throws Exception {
        when(authService.subscribeToUser(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        ResultActions resultActions = mockMvc.perform(put("/api/auth/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEntity)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void authController_getSubscriptions_ReturnsUserEntity() throws Exception {
        this.authUser.setSubscriptions(Set.of(userEntity));
        when(authService.getCurrentUser()).thenReturn(authUser);

        ResultActions resultActions = mockMvc.perform((get("/api/auth/subscriptions")));

        resultActions.andExpect((MockMvcResultMatchers.status().isOk()));
    }

    @Test
    public void authController_UnsubscribeToUser_ReturnsUserEntity() throws Exception {
        when(authService.unsubscribeToUser(Mockito.any(UserEntity.class))).thenReturn(userEntity);

        ResultActions resultActions = mockMvc.perform(put("/api/auth/unsubscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEntity)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}