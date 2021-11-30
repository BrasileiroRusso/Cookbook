package ru.geekbrains.cookbook.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.stream.Stream;


public interface FileStorageService {
    void init();
    Path save(MultipartFile multipartFile);
    Resource load(String filename);
    Stream<Path> load();
}
