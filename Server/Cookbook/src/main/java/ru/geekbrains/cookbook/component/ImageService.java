package ru.geekbrains.cookbook.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageService {
    @Value("C:")
    private String mainFolderPath;

    @Value("data/image")
    private String imagesFolder;

    private ImageService() {}

    public String saveImage(MultipartFile imageFile, String folder) {
        if (imageFile == null) {
            throw new IllegalArgumentException("Image file can not be null!");
        }
        String imagePath = imagesFolder + '/' + folder + '/' + imageFile.getOriginalFilename();
        String imageFolder = mainFolderPath + '/' + imagesFolder + '/' + folder;
        createDirectories(Paths.get(imageFolder));
        Path savePath = Paths.get(imageFolder, imageFile.getOriginalFilename());
        saveFile(imageFile, Paths.get(savePath.toString()));

        return imagePath;
    }

    private void createDirectories(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(MultipartFile file, Path path) {
        try {
            file.transferTo(path);
        } catch (IOException e) {
            throw new RuntimeException("Can't save file by path=" + path);
        }
    }
}
