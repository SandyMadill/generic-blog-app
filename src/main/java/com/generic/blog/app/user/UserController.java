package com.generic.blog.app.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class UserController {

    private final UserService userService;

    @GetMapping("user/{id}")
    private ResponseEntity<UserEntity> getUserById(@PathVariable(name = "id") Long id) throws Exception {
        return new ResponseEntity<UserEntity>(userService.findUserById(id), HttpStatus.OK);
    }



    @GetMapping(value = "user/logged-in")
    private ResponseEntity<UserEntity> getLoggedInUser() throws Exception {
        return new ResponseEntity<UserEntity>(userService.getCurrentUser(), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{page}/{size}")
    private ResponseEntity<Page<UserEntity>> getUsersPage(@PathVariable("page") int page, @PathVariable("size") int size) throws Exception {
        return new ResponseEntity<Page<UserEntity>>(userService.getUsersPage(page,size), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{page}/{size}/{search}")
    private ResponseEntity<Page<UserEntity>> getUsersPage(@PathVariable("page") int page, @PathVariable("size") int size, @PathVariable("search") String search) throws Exception {
        return new ResponseEntity<Page<UserEntity>>(userService.getUserPageUsernameSearch(page,size,search), HttpStatus.OK);
    }
}
