package ru.geekbrains.cookbook.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.service.FileStorageService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Service("fileStorageService")
public class FileStorageServiceImpl implements FileStorageService {
    private static final Path basePath = Paths.get("C:/data/image/recipe");

    @Override
    public void init() {
        try {
            Files.createDirectory(basePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Path save(MultipartFile multipartFile) {
        try {
            UUID uuid = UUID.randomUUID();
            String name = multipartFile.getOriginalFilename();
            int pointIndex = name.lastIndexOf(".");
            String extension = name.substring(pointIndex);
            name = uuid + extension;
            Path filePath = basePath.resolve(name);
            Files.copy(multipartFile.getInputStream(), filePath);
            return filePath.getFileName();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        Path file = basePath.resolve(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                throw new RuntimeException("Could not read the file.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Stream<Path> load() {
        try {
            return Files.walk(basePath,1)
                    .filter(path -> !path.equals(basePath))
                    .map(basePath::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files.");
        }
    }

}
