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

    @Value("C:/Image")
    private String mainFolderPath;

    private ImageService() {}

    public Path saveImage(MultipartFile imageFile, String folder) {
        if (imageFile == null) {
            throw new IllegalArgumentException("Image file can not be null!");
        }
        String imageFolder = mainFolderPath + '/' + folder;
        createDirectories(Paths.get(imageFolder));
        Path savePath = Paths.get(imageFolder, imageFile.getOriginalFilename());
        saveFile(imageFile, Paths.get(savePath.toString()));

        return savePath;
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
