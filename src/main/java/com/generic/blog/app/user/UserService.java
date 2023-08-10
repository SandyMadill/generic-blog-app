package com.generic.blog.app.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserEntity findUserById(Long id) throws Exception{
        return userRepository.findById(id).get();
    }

    public UserEntity findUserByUsername(String username) throws Exception{
        return userRepository.findByUsername(username).get();
    }

    public Page<UserEntity> getUsersPage(int page, int size) throws Exception{
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    public Page<UserEntity> getUserPageUsernameSearch(int page, int size, String search) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findUserEntitiesByUsernameContains(search,pageable);
    }



    public UserEntity getCurrentUser() throws Exception{
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return this.userRepository.findByUsername(((UserDetails) principal).getUsername()).get();
        }
        else {
            throw new Exception("Not Logged in");
        }
    }

}
