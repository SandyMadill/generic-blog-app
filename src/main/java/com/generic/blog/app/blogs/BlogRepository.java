package com.generic.blog.app.blogs;

import com.generic.blog.app.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    public Optional<Blog> findBlogByTitle(String title);

    public Optional<Blog> findBlogById(Long id);

    public Page<Blog> findAllByOrderByPostedAtDesc(Pageable page);

    public Page<Blog>  findBlogsByTitleContains(String search, Pageable page);

    public Page<Blog> findBlogsByUser(UserEntity user, Pageable page);

    public List<Blog> findBlogsByUser(UserEntity user);

    public Page<Blog> findBlogsByUserAndTitleContains(UserEntity user, String title, Pageable page);

    public Page<Blog> findBlogsByUserIn(Set<UserEntity> users, Pageable page);

    public Page<Blog> findBlogsByUserInAndTitleContains(Set<UserEntity> users, String search, Pageable page);

}
