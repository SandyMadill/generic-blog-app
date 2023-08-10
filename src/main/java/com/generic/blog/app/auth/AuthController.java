package com.generic.blog.app.auth;

import com.generic.blog.app.auth.models.AuthRequest;
import com.generic.blog.app.auth.models.AuthUser;
import com.generic.blog.app.auth.utils.JwtUtil;
import com.generic.blog.app.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AuthController
{
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtUtil jwtUtil;


    @PostMapping("auth/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            final UserDetails user = authService.loadUserByUsername((request.getUsername()));
            return new ResponseEntity<String>(jwtUtil.generateToken(user), HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("auth/register")
    private ResponseEntity<AuthUser> registerUser(@RequestBody AuthUser authUser) throws Exception {
        return new ResponseEntity<AuthUser>(authService.registerUser(authUser), HttpStatus.CREATED);
    }

    @PutMapping("auth/subscribe")
    private ResponseEntity<UserEntity> subscribeToUser(@RequestBody UserEntity subscription) throws Exception {
        return new ResponseEntity<UserEntity>(authService.subscribeToUser(subscription), HttpStatus.OK);
    }

    @PutMapping("auth/unsubscribe")
    private ResponseEntity<UserEntity> unsubscribeToUser(@RequestBody UserEntity subscription) throws Exception {
        return new ResponseEntity<UserEntity>(authService.unsubscribeToUser(subscription), HttpStatus.OK);
    }

    @GetMapping("auth/subscriptions")
    private ResponseEntity getSubscriptions() throws Exception {
        return  new ResponseEntity<Set<UserEntity>>(this.authService.getCurrentUser().getSubscriptions(), HttpStatus.OK);
    }

}
