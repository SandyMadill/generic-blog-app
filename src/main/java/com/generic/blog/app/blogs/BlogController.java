package com.generic.blog.app.blogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@CrossOrigin("http://localhost:4200")
public class BlogController {

    private BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("blog")
    private ResponseEntity<Blog> uploadBlog(@RequestBody Blog blog) throws Exception {
        return new ResponseEntity<Blog>(blogService.uploadBlog(blog), HttpStatus.CREATED);
    }

    @GetMapping("blog/{id}")
    private ResponseEntity<Blog> getBlogById(@PathVariable("id") Long id) throws Exception {
        return new ResponseEntity<Blog>(blogService.findBlogById(id),HttpStatus.OK);

    }

    @GetMapping("blog/latest/{page}/{size}")
    private ResponseEntity<Page<Blog>> getBlogsPageLatest(@PathVariable("page") int page, @PathVariable("size") int size) throws Exception {
        return new ResponseEntity<Page<Blog>>(blogService.getBlogsPageLatest(page,size),HttpStatus.OK);
    }

    @GetMapping("blog/latest/{page}/{size}/{search}")
    private ResponseEntity<Page<Blog>> getBlogsPageSearchLatest(@PathVariable("page") int page, @PathVariable("size") int size, @PathVariable String search) throws Exception {
        return new ResponseEntity<Page<Blog>>(blogService.getBlogsPageSearchLatest(page, size, search),HttpStatus.OK);

    }

    @GetMapping("blog/user/{page}/{size}/{id}")
    private ResponseEntity<Page<Blog>> getBlogsPageUser(@PathVariable("page") int page, @PathVariable("size") int size, @PathVariable Long id) throws Exception {
        return new ResponseEntity<Page<Blog>>(blogService.getBlogsPageUser(page, size,id),HttpStatus.OK);
    }

    @GetMapping("blog/user/{page}/{size}/{id}/{search}")
    private ResponseEntity<Page<Blog>> getBlogsPageSearchUser(@PathVariable("page") int page, @PathVariable("size") int size, @PathVariable Long id, @PathVariable("search") String search) throws Exception {
        return new ResponseEntity<Page<Blog>>(blogService.getBlogsPageSearchUser(page, size,search,id),HttpStatus.OK);
    }

    @GetMapping("blog/user/{id}")
    private ResponseEntity<List<Blog>> getAllBlogsForUser(@PathVariable("id") Long id) throws Exception {
        return new ResponseEntity<List<Blog>>(blogService.getAllBlogsForUser(id), HttpStatus.OK);
    }

    @GetMapping("blog/subscriptions/{page}/{size}")
    private ResponseEntity<Page<Blog>> getBlogsPageSubscriptions(@PathVariable("page") int page, @PathVariable("size") int size) throws Exception {
        return new ResponseEntity<Page<Blog>>(blogService.getBlogsPageSubscriptions(page,size),HttpStatus.OK);
    }

    @GetMapping("blog/subscriptions/{page}/{size}/{search}")
    private ResponseEntity<Page<Blog>> getBlogsPageSearchSubscriptions(@PathVariable("page") int page, @PathVariable("size") int size, @PathVariable("search") String search) throws Exception {
        return new ResponseEntity<Page<Blog>>(blogService.getBlogsPageSearchSubscriptions(page,size,search),HttpStatus.OK);
    }
}
