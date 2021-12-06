package ru.geekbrains.cookbook.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.domain.file.UploadedFile;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import ru.geekbrains.cookbook.dto.UploadedFileLinkDto;

import java.util.List;

public interface UploadFileService {
    UploadedFile uploadFile(Long userId, MultipartFile file);
    List<UploadedFile> getAllUploadedFiles(Long userId);
    byte[] getFileContent(String filename);
    UploadedFileLink saveUploadFileLink(UploadedFileLinkDto uploadedFileLinkDto);
    UploadedFileLink getUploadedFileLink(Long linkId);
    boolean removeUploadedFileLink(Long linkId);
    LinkedFiles getUploadedFileListByResource(Long objectId, Class<?> objectType);
}
