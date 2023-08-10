package com.generic.blog.app.images;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    ImageService imageService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void imageService_UploadImageToFileSystem_ReturnsMultiPartFile() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        File newFile = mock(File.class);

        MultipartFile response = imageService.uploadImageToFileSystem(file, "fileLocation");

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void imageService_GetImageFromFileSystem_ReturnsByteArray() throws IOException {
        MockedStatic<Files> files = Mockito.mockStatic(Files.class);

        when(Files.readAllBytes(Mockito.any(Path.class))).thenAnswer(invo ->
            new ByteArrayOutputStream().toByteArray()
        );

        byte[] response = imageService.getImageFromFileSystem("fileAddress");

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void imageService_ResizeImage_ReturnsMultiPartFile() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        MockedStatic<ImageIO> io = Mockito.mockStatic(ImageIO.class);

        when(ImageIO.read(Mockito.any(InputStream.class))).thenAnswer((invo) ->
            new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB)
        );

        when(ImageIO.write(
                Mockito.any(BufferedImage.class),
                Mockito.any(String.class),
                Mockito.any(ByteArrayOutputStream.class)
        )).thenReturn(true);

        MultipartFile response = imageService.resizeImage(multipartFile,40,40);

        Assertions.assertThat(response).isNotNull();
    }
}