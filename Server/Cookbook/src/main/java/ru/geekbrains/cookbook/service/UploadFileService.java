package ru.geekbrains.cookbook.service;

import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.domain.file.UploadedFile;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import ru.geekbrains.cookbook.dto.UploadedFileLinkDto;
import java.util.List;
import java.util.Map;

public interface UploadFileService {
    UploadedFile uploadFile(String username, MultipartFile file);
    List<UploadedFile> getAllUploadedFiles(String username);
    byte[] getFileContent(String filename);
    UploadedFileLink saveUploadFileLink(UploadedFileLinkDto uploadedFileLinkDto);
    boolean removeUploadedFileLink(UploadedFileLinkDto uploadedFileLinkDto);
    LinkedFiles getUploadedFileListByResource(Long objectId, Class<?> objectType);
    Map<Long, String> getUploadedFilesByObjectList(List<Long> ids, Class<?> objectType);
    Map<Long, String> getUploadedFilesByObjectType(Class<?> objectType);
    String getFirstUploadedFile(Long objectId, Class<?> objectType);
}
