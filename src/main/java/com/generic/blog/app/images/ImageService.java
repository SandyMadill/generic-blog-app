package com.generic.blog.app.images;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Service
public class ImageService {
    private final String RESOURCES_PATH="./src/main/resources/img/";

    public byte[] getImageFromFileSystem(String fileAddress) throws IOException {
        return Files.readAllBytes(new File(RESOURCES_PATH+fileAddress).toPath());
    }
    public MultipartFile uploadImageToFileSystem(MultipartFile file, String fileLocation) throws IOException {
        File newFile = new File(RESOURCES_PATH+fileLocation+file.getName());
        newFile.getParentFile().mkdirs();
        file.transferTo(newFile);
        return file;
    }


    public MultipartFile resizeImage(MultipartFile image, int width, int height) throws IOException {
        BufferedImage bufferedProfilePicture = ImageIO.read(image.getInputStream());
        BufferedImage resizedImage = new BufferedImage(width,height,bufferedProfilePicture.TYPE_INT_RGB);

        //  resize the image
        final Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(bufferedProfilePicture,0,0, width,height, null);
        graphics2D.dispose();

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage,"png",byteArrayOutputStream);
        byteArrayOutputStream.flush();

        return new MockMultipartFile((width+"x"+height+".png"),byteArrayOutputStream.toByteArray());

    }
}
