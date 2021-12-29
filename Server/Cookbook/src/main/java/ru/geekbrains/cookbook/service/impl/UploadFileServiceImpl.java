package ru.geekbrains.cookbook.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.domain.Ingredient;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.domain.file.UploadedFile;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import ru.geekbrains.cookbook.dto.UploadedFileLinkDto;
import ru.geekbrains.cookbook.repository.UploadedFileLinkRepository;
import ru.geekbrains.cookbook.repository.UploadedFileRepository;
import ru.geekbrains.cookbook.service.FileStorageService;
import ru.geekbrains.cookbook.service.UploadFileService;
import ru.geekbrains.cookbook.service.UserService;
import ru.geekbrains.cookbook.service.exception.ResourceNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UploadFileServiceImpl implements UploadFileService {
    private FileStorageService fileStorageService;
    private UploadedFileRepository uploadedFileRepository;
    private UploadedFileLinkRepository uploadedFileLinkRepository;
    private UserService userService;

    @Override
    @Transactional
    public UploadedFile uploadFile(String username, MultipartFile file) {
        User user = userService.findByUsername(username);
        String fileKey = fileStorageService.save(file);
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFilename(fileKey);
        uploadedFile.setUser(user);
        uploadedFile.setUploadTime(new Date());
        uploadedFileRepository.save(uploadedFile);
        return uploadedFile;
    }

    @Override
    @Transactional
    public List<UploadedFile> getAllUploadedFiles(String username) {
        User user = userService.findByUsername(username);
        return uploadedFileRepository.findAllByUser_Id(user.getId());
    }

    @Override
    @Transactional
    public byte[] getFileContent(String filename) {
        return fileStorageService.download(filename);
    }

    @Override
    @Transactional
    public UploadedFileLink saveUploadFileLink(UploadedFileLinkDto uploadedFileLinkDto) {
        UploadedFileLink uploadedFileLink;
        String filename = uploadedFileLinkDto.getFilename();
        UploadedFile uploadedFile = uploadedFileRepository.getByFilename(filename).orElseThrow(ResourceNotFoundException::new);
        Optional<UploadedFileLink> optionalLink = uploadedFileLinkRepository.findByObjectIdAndObjectTypeAndObjectPartAndUploadedFile_Filename(uploadedFileLinkDto.getObjectId(),
                                                                                                                                              uploadedFileLinkDto.getObjectType(),
                                                                                                                                              uploadedFileLinkDto.getObjectPart(),
                                                                                                                                              filename);
        if(optionalLink.isPresent()){
            uploadedFileLink = optionalLink.get();
        }
        else{
            uploadedFileLink = new UploadedFileLink();
            uploadedFileLink.setObjectId(uploadedFileLinkDto.getObjectId());
            uploadedFileLink.setObjectType(uploadedFileLinkDto.getObjectType());
            uploadedFileLink.setObjectPart(uploadedFileLinkDto.getObjectPart());
            uploadedFileLink.setUploadedFile(uploadedFile);
        }
        uploadedFileLink.setDescription(uploadedFileLinkDto.getDescription());
        uploadedFileLinkRepository.save(uploadedFileLink);
        return uploadedFileLink;
    }

    @Override
    @Transactional
    public boolean removeUploadedFileLink(UploadedFileLinkDto uploadedFileLinkDto) {
        UploadedFileLink uploadedFileLink = uploadedFileLinkRepository.findByObjectIdAndObjectTypeAndObjectPartAndUploadedFile_Filename(
                uploadedFileLinkDto.getObjectId(), uploadedFileLinkDto.getObjectType(), uploadedFileLinkDto.getObjectPart(), uploadedFileLinkDto.getFilename()).
                orElseThrow(ResourceNotFoundException::new);
        uploadedFileLinkRepository.deleteById(uploadedFileLink.getId());
        return true;
    }

    @Override
    @Transactional
    public LinkedFiles getUploadedFileListByResource(Long objectId, Class<?> objectType) {
        List<UploadedFileLink> uploadedFileLinks = uploadedFileLinkRepository.findAllByObjectIdAndObjectType(objectId, objectType.getSimpleName());
        return transformToLinkedFiles(uploadedFileLinks);
    }

    @Override
    @Transactional
    public Map<Long, String> getUploadedFilesByObjectList(List<Long> ids, Class<?> objectType) {
        List<UploadedFileLink> fileList = uploadedFileLinkRepository.findAllByObjectIdInAndObjectTypeAndObjectPart(ids, objectType.getSimpleName(), "");
        return fileLinkListToMap(fileList);
    }

    @Override
    @Transactional
    public Map<Long, String> getUploadedFilesByObjectType(Class<?> objectType) {
        List<UploadedFileLink> fileList = uploadedFileLinkRepository.findAllByObjectTypeAndObjectPart(objectType.getSimpleName(), "");
        return fileLinkListToMap(fileList);
    }

    @Override
    @Transactional
    public String getFirstUploadedFile(Long objectId, Class<?> objectType) {
        return getUploadedFileListByResource(objectId, objectType)
                .getFiles()
                .stream()
                .findFirst()
                .map(LinkedFiles.FileInfo::getFileUri)
                .orElse("");
    }

    private Map<Long, String> fileLinkListToMap(List<UploadedFileLink> fileLinkList){
        if(fileLinkList == null)
            return null;
        return fileLinkList
                .stream()
                .collect(Collectors.toMap(
                        UploadedFileLink::getObjectId,
                        fileLink -> fileLink.getUploadedFile().getFilename(),
                        (existingValue, newValue) -> existingValue));
    }

    private LinkedFiles transformToLinkedFiles(List<UploadedFileLink> uploadedFileLinks){
        LinkedFiles linkedFiles = new LinkedFiles();
        for(UploadedFileLink uploadedFileLink: uploadedFileLinks){
            String objectPart = uploadedFileLink.getObjectPart();
            LinkedFiles.FileInfo fileInfo = new LinkedFiles.FileInfo();
            fileInfo.setFileUri(uploadedFileLink.getUploadedFile().getFilename());
            fileInfo.setDescription(uploadedFileLink.getDescription());
            if(objectPart.isEmpty()){
                linkedFiles.getFiles().add(fileInfo);
            }
            else{
                List<LinkedFiles.FileInfo> fileInfoList = linkedFiles.getEmbeddedFiles().computeIfAbsent(objectPart, k -> new ArrayList<>());
                fileInfoList.add(fileInfo);
            }
        }
        return linkedFiles;
    }

}
