package ru.geekbrains.cookbook.service;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;


public interface FileStorageService {
    String save(MultipartFile multipartFile);
    byte[] download(String key);
}
