package ru.geekbrains.cookbook.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.domain.file.UploadedFile;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import ru.geekbrains.cookbook.repository.UploadedFileLinkRepository;
import ru.geekbrains.cookbook.repository.UploadedFileRepository;
import ru.geekbrains.cookbook.service.FileStorageService;
import ru.geekbrains.cookbook.service.UploadFileService;
import ru.geekbrains.cookbook.service.UserService;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UploadFileServiceImpl implements UploadFileService {
    private FileStorageService fileStorageService;
    private UploadedFileRepository uploadedFileRepository;
    private UploadedFileLinkRepository uploadedFileLinkRepository;
    private UserService userService;

    @Override
    @Transactional
    public UploadedFile uploadFile(Long userId, MultipartFile file) {
        User user = userService.getUser(userId);
        Path filePath = fileStorageService.save(file);
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFilename(filePath.toString());
        uploadedFile.setUser(user);
        uploadedFile.setUploadTime(new Date());
        uploadedFileRepository.save(uploadedFile);
        return uploadedFile;
    }

    @Override
    public List<UploadedFile> getAllUploadedFiles(Long userId) {
        return uploadedFileRepository.findAllByUser_Id(userId);
    }

    @Override
    @Transactional
    public Resource getFileContent(String filename) {
        Resource file = fileStorageService.load(filename);
        return file;
    }

    @Override
    @Transactional
    public UploadedFileLink saveUploadFileLink(Long objectId, Class<?> type, String objectPart, String fileUri, String description) {
        //String filename = Paths.get(URI.create(fileUri)).getFileName().toString();
        int pointIndex = fileUri.lastIndexOf("/");
        String filename = fileUri.substring(pointIndex + 1);
        UploadedFile uploadedFile = uploadedFileRepository.getByFilename(filename).orElseThrow(RuntimeException::new);
        UploadedFileLink uploadedFileLink = new UploadedFileLink();
        uploadedFileLink.setObjectId(objectId);
        uploadedFileLink.setObjectType(type.getSimpleName());
        uploadedFileLink.setObjectPart(objectPart);
        uploadedFileLink.setDescription(description);
        uploadedFileLink.setUploadedFile(uploadedFile);
        uploadedFileLinkRepository.save(uploadedFileLink);
        return uploadedFileLink;
    }

    @Override
    @Transactional
    public List<UploadedFileLink> getUploadedFileListByResource(Long objectId, Class<?> objectType) {
        return uploadedFileLinkRepository.findAllByObjectIdAndObjectType(objectId, objectType.getSimpleName());
    }


}
