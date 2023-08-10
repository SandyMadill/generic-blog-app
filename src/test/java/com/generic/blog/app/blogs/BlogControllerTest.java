package com.generic.blog.app.blogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.blog.app.auth.EnumRole;
import com.generic.blog.app.auth.models.AuthUser;
import com.generic.blog.app.user.UserEntity;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = BlogController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class BlogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity user;
    private AuthUser authUser;

    private Blog blog;

    @BeforeEach
    public void setUp(){
        user = UserEntity.builder().username("username").displayName("displayName").active(true).role(EnumRole.USER).build();
        authUser = AuthUser.builder().username("authUser").password("password").displayName("displayName").active(true).role(EnumRole.USER).subscriptions(Set.of()).build();
        blog = Blog.builder().title("title").blogText("blogText").user(user).build();
    }


    @Test
    public void blogController_UploadBlog_ReturnCreated() throws Exception {
        given(blogService.uploadBlog(ArgumentMatchers.any())).willAnswer((invo -> invo.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/api/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blog)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(blog.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.blogText", CoreMatchers.is(blog.getBlogText())));
    }

    @Test
    public void blogController_GetBlogById_ReturnBlog() throws Exception {
        when(blogService.findBlogById(Mockito.any(Long.class))).thenReturn(blog);

        ResultActions response = mockMvc.perform(get("/api/blog/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(blog.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.blogText", CoreMatchers.is(blog.getBlogText())));
    }

    @Test
    public void blogController_GetBlogsPageLatest_ReturnsBlogsPage() throws Exception {
        Page<Blog> blogs = new PageImpl<>(List.of(blog), PageRequest.of(0,10), 0);
        when(blogService.getBlogsPageLatest(
                Mockito.anyInt(),
                Mockito.anyInt()
        )).thenReturn(blogs);

        ResultActions response = mockMvc.perform(get("/api/blog/latest/0/10")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize",CoreMatchers.is(blogs.getSize())));
    }

    @Test
    public void blogController_GetBlogsPageSearchLatest_ReturnsBlogsPage() throws Exception {
        Page<Blog> blogs = new PageImpl<>(List.of(blog), PageRequest.of(0,10), 0);
        when(blogService.getBlogsPageSearchLatest(
                Mockito.any(int.class),
                Mockito.any(int.class),
                Mockito.any(String.class)
        )).thenReturn(blogs);

        ResultActions response = mockMvc.perform(get("/api/blog/latest/0/10/search")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize",CoreMatchers.is(blogs.getSize())));
    }

    @Test
    public void blogController_GetBlogsPageUser_ReturnsBlogsPage() throws Exception {
        Page<Blog> blogs = new PageImpl<>(List.of(blog), PageRequest.of(0,10), 0);
        when(blogService.getBlogsPageUser(
                Mockito.any(int.class),
                Mockito.any(int.class),
                Mockito.any(Long.class)
        )).thenReturn(blogs);

        ResultActions response = mockMvc.perform(get("/api/blog/user/0/10/0")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize",CoreMatchers.is(blogs.getSize())));
    }

    @Test
    public void blogController_GetBlogsPageSearchUser_ReturnsBlogsPage() throws Exception {
        Page<Blog> blogs = new PageImpl<>(List.of(blog), PageRequest.of(0,10), 0);
        when(blogService.getBlogsPageSearchUser(
                Mockito.any(int.class),
                Mockito.any(int.class),
                Mockito.any(String.class),
                Mockito.any(Long.class)
        )).thenReturn(blogs);

        ResultActions response = mockMvc.perform(get("/api/blog/user/0/10/0/search")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize",CoreMatchers.is(blogs.getSize())));
    }

    @Test
    public void blogController_GetAllBlogsForUser_ReturnsBlogList() throws Exception {
        List<Blog> blogs = List.of(blog);

        when(blogService.getAllBlogsForUser(Mockito.any(Long.class))).thenReturn(blogs);

        ResultActions resultActions = mockMvc.perform(get("/api/blog/user/0"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()" , CoreMatchers.is(blogs.size())));
    }

    @Test
    public void blogController_GetBlogsPageSubscriptions_ReturnsBlogsPage() throws Exception {
        Page<Blog> blogs = new PageImpl<>(List.of(blog), PageRequest.of(0,10), 0);
        when(blogService.getBlogsPageSubscriptions(
                Mockito.anyInt(),
                Mockito.anyInt()
        )).thenReturn(blogs);

        ResultActions response = mockMvc.perform(get("/api/blog/subscriptions/0/10")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize",CoreMatchers.is(blogs.getSize())));
    }

    @Test
    public void blogController_GetBlogsPageSearchSubscriptions_ReturnsBlogsPage() throws Exception {
        Page<Blog> blogs = new PageImpl<>(List.of(blog), PageRequest.of(0,10), 0);
        when(blogService.getBlogsPageSearchSubscriptions(
                Mockito.any(int.class),
                Mockito.any(int.class),
                Mockito.any(String.class)
        )).thenReturn(blogs);

        ResultActions response = mockMvc.perform(get("/api/blog/subscriptions/0/10/search")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize",CoreMatchers.is(blogs.getSize())));
    }
}