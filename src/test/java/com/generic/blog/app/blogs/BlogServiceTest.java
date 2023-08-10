package com.generic.blog.app.blogs;

import com.generic.blog.app.auth.AuthService;
import com.generic.blog.app.auth.EnumRole;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BlogServiceTest {
    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private BlogService blogService;

    private UserEntity user;

    private AuthUser authUser;
    private Blog blog;

    @BeforeEach
    public void init(){
        user = UserEntity.builder().username("username").displayName("displayName").active(true).role(EnumRole.USER).build();
        authUser = AuthUser.builder().username("authUser").password("password").displayName("displayName").active(true).role(EnumRole.USER).subscriptions(Set.of(user)).build();
        blog = Blog.builder().title("title").blogText("blogText").build();
    }

    @Test
    public void blogService_GetBlogsPageLatest_ReturnsBlogPage() throws Exception {
        Page<Blog> blogs = Mockito.mock(Page.class);

        when(blogRepository.findAll(Mockito.any(Pageable.class))).thenReturn(blogs);

        Page<Blog> response = blogService.getBlogsPageLatest(0,10);

        Assertions.assertThat(response).isNotNull();

    }

    @Test
    void blogService_uploadBlog_ReturnsBlog() throws Exception {
        when(userService.getCurrentUser()).thenReturn(user);
        when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(blog);

        Blog response = blogService.uploadBlog(blog);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void blogService_findBlogById_ReturnsBlog() throws Exception {
        when(blogRepository.findBlogById(Mockito.any(Long.class))).thenReturn(Optional.of(blog));

        Blog response = blogService.findBlogById(2L);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void blogService_FindBlogByTitle_ReturnsBlog() throws Exception {
        when(blogRepository.findBlogByTitle(Mockito.any(String.class))).thenReturn(Optional.of(blog));

        Blog response = blogService.findBlogByTitle("title");

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void blogService_GetBlogsPageSearchLatest_ReturnsBlogPage() throws Exception {
        Page<Blog> blogs = Mockito.mock(Page.class);

        when(blogRepository.findBlogsByTitleContains(
                Mockito.any(String.class),
                Mockito.any(Pageable.class))
        ).thenReturn(blogs);

        Page<Blog> response = blogService.getBlogsPageSearchLatest(0,10,"search");

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void blogService_GetBlogsPageUser_ReturnsBlogPage() throws Exception {
        Page<Blog> blogs = Mockito.mock(Page.class);

        when(userService.findUserById(Mockito.any(Long.class))).thenReturn(user);

        when(blogRepository.findBlogsByUser(
                Mockito.any(UserEntity.class),
                Mockito.any(Pageable.class))
        ).thenReturn(blogs);

        Page<Blog> response = blogService.getBlogsPageUser(0,10, 0L);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void blogService_GetBlogsPageSearchUser_ReturnsBlogPage() throws Exception {
        Page<Blog> blogs = Mockito.mock(Page.class);

        when(userService.findUserById(Mockito.any(Long.class))).thenReturn(user);

        when(blogRepository.findBlogsByUserAndTitleContains(
                Mockito.any(UserEntity.class),
                Mockito.any(String.class),
                Mockito.any(Pageable.class))
        ).thenReturn(blogs);

        Page<Blog> response = blogService.getBlogsPageSearchUser(0,10,"search",0L);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void blogService_GetAllBlogsForUser_ReturnsBlogList() throws Exception {
        List<Blog> blogs = Mockito.mock(List.class);
        when(userService.findUserById(Mockito.any(Long.class))).thenReturn(user);

        when(blogRepository.findBlogsByUser(
                Mockito.any(UserEntity.class)
        )).thenReturn(blogs);

        List<Blog> response = blogService.getAllBlogsForUser(0L);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void blogService_GetBlogsPageSubscriptions_ReturnsBlogPage() throws Exception {
        Page<Blog> blogs = Mockito.mock(Page.class);

        when(authService.getCurrentUser()).thenReturn(authUser);

        when(blogRepository.findBlogsByUserIn(
                Mockito.any(Set.class),
                Mockito.any(Pageable.class))
        ).thenReturn(blogs);

        Page<Blog> response = blogService.getBlogsPageSubscriptions(0,10);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void blogService_getBlogsPageSearchSubscriptions_ReturnsBlogPage() throws Exception {
        Page<Blog> blogs = Mockito.mock(Page.class);

        when(authService.getCurrentUser()).thenReturn(authUser);

        when(blogRepository.findBlogsByUserInAndTitleContains(
                Mockito.any(Set.class),
                Mockito.any(String.class),
                Mockito.any(Pageable.class))
        ).thenReturn(blogs);

        Page<Blog> response = blogService.getBlogsPageSearchSubscriptions(0,10,"search");

        Assertions.assertThat(response).isNotNull();
    }
}