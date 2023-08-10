package com.generic.blog.app.blogs;

import com.generic.blog.app.auth.AuthService;
import com.generic.blog.app.user.UserEntity;
import com.generic.blog.app.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final UserService userService;

    private final BlogRepository blogRepository;

    private final AuthService authService;

    public Blog uploadBlog(Blog blogData) throws Exception{
        Blog blogEntity = blogData;
        blogEntity.setUser(userService.getCurrentUser());
        return this.blogRepository.save(blogEntity);
    }

    public Blog findBlogById(Long id) throws Exception{
        return this.blogRepository.findBlogById(id).get();
    }

    public Blog findBlogByTitle(String title) throws Exception{
        return this.blogRepository.findBlogByTitle(title).get();
    }

    public Page<Blog> getBlogsPageLatest(int page, int size) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        return this.blogRepository.findAll(pageable);
    }

    public Page<Blog> getBlogsPageSearchLatest(int page, int size, String search) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        return this.blogRepository.findBlogsByTitleContains(search, pageable);
    }

    public Page<Blog> getBlogsPageUser(int page, int size, Long id) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        UserEntity user = userService.findUserById(id);
        return blogRepository.findBlogsByUser(user, pageable);
    }

    public Page<Blog> getBlogsPageSearchUser(int page, int size, String search, Long id) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        UserEntity user = userService.findUserById(id);
        return blogRepository.findBlogsByUserAndTitleContains(user, search, pageable);
    }

    public List<Blog> getAllBlogsForUser(Long id) throws Exception {
        UserEntity user = userService.findUserById(id);
        return blogRepository.findBlogsByUser(user);
    }

    public Page<Blog> getBlogsPageSubscriptions(int page, int size) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        Set<UserEntity> subscriptions = authService.getCurrentUser().getSubscriptions();
        return this.blogRepository.findBlogsByUserIn(subscriptions,pageable);
    }

    public Page<Blog> getBlogsPageSearchSubscriptions(int page, int size, String search) throws Exception{
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        Set<UserEntity> subscriptions = authService.getCurrentUser().getSubscriptions();
        return this.blogRepository.findBlogsByUserInAndTitleContains(subscriptions, search, pageable);
    }

}
