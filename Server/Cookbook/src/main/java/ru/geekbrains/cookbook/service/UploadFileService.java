package ru.geekbrains.cookbook.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.domain.file.UploadedFile;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import java.util.List;

public interface UploadFileService {
    UploadedFile uploadFile(Long userId, MultipartFile file);
    List<UploadedFile> getAllUploadedFiles(Long userId);
    Resource getFileContent(String filename);
    UploadedFileLink saveUploadFileLink(Long objectId, Class<?> type, String objectPart, String fileUri, String description);
    List<UploadedFileLink> getUploadedFileListByResource(Long objectId, Class<?> objectType);
}
