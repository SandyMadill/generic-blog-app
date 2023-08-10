package com.generic.blog.app.auth;

import com.generic.blog.app.auth.models.AuthUser;
import com.generic.blog.app.user.UserEntity;
import com.generic.blog.app.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findByUsername(username).get();
    }


    public AuthUser loadAuthUserByUsername(String username) throws UsernameNotFoundException {
            return authRepository.findByUsername(username).get();
    }

    public AuthUser registerUser(AuthUser userData) throws Exception {
        AuthUser authUser = userData;
        authUser.setPassword(passwordEncoder.encode(userData.getPassword()));
        authUser.setSubscriptions(Set.of());
        AuthUser toReturn = authRepository.save(authUser);
        return toReturn;
    }

    public AuthUser getCurrentUser() throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return this.loadAuthUserByUsername(((UserDetails) principal).getUsername());
        }
        else {
            throw new Exception("Not Logged in");
        }
    }

    public AuthUser getUserById(Long id) throws Exception {
        return this.authRepository.findById(id).get();
    }

    public UserEntity subscribeToUser(UserEntity subscription) throws Exception {
        AuthUser subscriber = this.getCurrentUser();
        subscriber.getSubscriptions().add(subscription);
        authRepository.save(subscriber);
        return userService.findUserById(subscription.getId());
    }

    public UserEntity unsubscribeToUser(UserEntity subscription) throws Exception {
        AuthUser subscriber = this.getCurrentUser();
        subscriber.getSubscriptions().remove(userService.findUserById(subscription.getId()));
        authRepository.save(subscriber);
        return userService.findUserById(subscription.getId());
    }
}
