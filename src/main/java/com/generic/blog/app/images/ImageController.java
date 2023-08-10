package com.generic.blog.app.images;

import com.generic.blog.app.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.NoSuchFileException;

@RequiredArgsConstructor
@RequestMapping("/api/")
@RestController
@CrossOrigin("http://localhost:4200")
public class ImageController {
    private final ImageService imageService;
    private final UserService userService;

    @GetMapping("img/pfp/{file-name}")
    private ResponseEntity getDefaultProfilePicture(@PathVariable("file-name") String fileName) throws Exception{
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf("image/png"));
        return new ResponseEntity<>(imageService.getImageFromFileSystem("default/user-pfp/"+fileName), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("img/pfp/{id}/{file-name}")
    private ResponseEntity getProfilePicture(@PathVariable("id") Long id, @PathVariable("file-name") String fileName) throws Exception {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.valueOf("image/png"));
            return new ResponseEntity<>(imageService.getImageFromFileSystem("user-pfp/"+id+"/"+fileName), httpHeaders, HttpStatus.OK);
        }
        catch(NoSuchFileException e)
        {
            return this.getDefaultProfilePicture(fileName);
        }
    }

    @PostMapping("img/pfp")
    private ResponseEntity<String> uploadProfilePicture(@RequestParam("thumbnail.png") MultipartFile profilePicture) throws Exception{
            MultipartFile smallImage = imageService.resizeImage(profilePicture,40,40);
            MultipartFile bigImage = imageService.resizeImage(profilePicture,400,400);

            Long id = userService.getCurrentUser().getId();

            imageService.uploadImageToFileSystem(smallImage,("/user-pfp/" + id + "/"));
            imageService.uploadImageToFileSystem(bigImage,("/user-pfp/" + id + "/"));

            return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @PostMapping("img/thumbnail/{id}")
    private ResponseEntity<String> uploadThumbnail(@RequestParam("thumbnail.png") MultipartFile thumbnail, @PathVariable("id") Long id) throws Exception{
        MultipartFile newThumbnail = new MockMultipartFile("thumbnail.png",thumbnail.getInputStream());
        imageService.uploadImageToFileSystem(newThumbnail,("/thumbnail/" + id + "/"));
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @GetMapping("img/thumbnail/{id}")
    private ResponseEntity<?> getBlogThumbnail(@PathVariable("id") Long id) throws Exception {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.valueOf("image/png"));
            return new ResponseEntity<>(imageService.getImageFromFileSystem("thumbnail/" + id + "/thumbnail.png"), httpHeaders, HttpStatus.OK);
        }
        catch(NoSuchFileException e)
        {
            return getDefaultBlogThumbnail();
        }
    }

    @GetMapping("img/thumbnail")
    private ResponseEntity<?> getDefaultBlogThumbnail() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf("image/png"));
        return new ResponseEntity<>(imageService.getImageFromFileSystem("default/thumbnail/thumbnail.png"), httpHeaders, HttpStatus.OK);
    }
}
