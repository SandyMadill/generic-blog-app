package com.generic.blog.app.images;

import com.generic.blog.app.user.UserEntity;
import com.generic.blog.app.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ImageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ImageService imageService;

    @MockBean
    UserService userService;

    byte[] byteArray;
    MultipartFile multipartFile;

    UserEntity user = mock(UserEntity.class);

    @BeforeEach
    void setUp() throws Exception {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(img,"png",byteArrayOutputStream);
        byteArrayOutputStream.flush();

        byteArray = byteArrayOutputStream.toByteArray();
        multipartFile = new MockMultipartFile(("multiPartFile.png"),byteArrayOutputStream.toByteArray());
    }


    @Test
    public void imageController_GetDefaultProfilePicture_ReturnsImagePng() throws Exception {
        when(imageService.getImageFromFileSystem(Mockito.any(String.class))).thenReturn(byteArray);

        ResultActions resultActions = mockMvc.perform(get("/api/img/pfp/default.png"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_PNG));

    }

    @Test
    public void imageController_GetProfilePicture_ReturnsImagePng() throws Exception {
        when(imageService.getImageFromFileSystem(Mockito.any(String.class))).thenReturn(byteArray);

        ResultActions resultActions = mockMvc.perform(get("/api/img/pfp/0/40x40.png"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_PNG));

    }

    @Test
    public  void imageController_Upload_ProfilePicture_ReturnsStatusCreated() throws Exception {
        when(imageService.resizeImage(
                Mockito.any(MultipartFile.class),
                Mockito.any(int.class),
                Mockito.any(int.class)
        )).thenReturn(multipartFile);

        when(imageService.uploadImageToFileSystem(
                Mockito.any(MultipartFile.class),
                Mockito.any(String.class)
        )).thenReturn(multipartFile);

        when(userService.getCurrentUser()).thenReturn(user);

        when(user.getId()).thenReturn(0L);

        ResultActions resultActions = mockMvc.perform(post("/api/img/pfp")
                .contentType(MediaType.IMAGE_PNG)
                .content(multipartFile.getBytes())
        );

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public  void imageController_UploadThumbnail_ReturnsStatusCreated() throws Exception {

        when(imageService.uploadImageToFileSystem(
                Mockito.any(MultipartFile.class),
                Mockito.any(String.class)
        )).thenReturn(multipartFile);

        ResultActions resultActions = mockMvc.perform(post("/api/img/thumbnail/0")
                .contentType(MediaType.IMAGE_PNG)
                .content(multipartFile.getBytes())
        );

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void imageController_GetDefaultBlogThumbnail_ReturnsImagePng() throws Exception {
        when(imageService.getImageFromFileSystem(Mockito.any(String.class))).thenReturn(byteArray);

        ResultActions resultActions = mockMvc.perform(get("/api/img/thumbnail"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_PNG));

    }

    @Test
    public void imageController_GetBlogThumbnail_ReturnsImagePng() throws Exception {
        when(imageService.getImageFromFileSystem(Mockito.any(String.class))).thenReturn(byteArray);

        ResultActions resultActions = mockMvc.perform(get("/api/img/thumbnail/0"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_PNG));

    }

}