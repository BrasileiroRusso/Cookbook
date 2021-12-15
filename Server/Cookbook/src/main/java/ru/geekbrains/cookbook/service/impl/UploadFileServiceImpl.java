package ru.geekbrains.cookbook.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.domain.file.UploadedFile;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import ru.geekbrains.cookbook.dto.UploadedFileLinkDto;
import ru.geekbrains.cookbook.repository.UploadedFileLinkRepository;
import ru.geekbrains.cookbook.repository.UploadedFileRepository;
import ru.geekbrains.cookbook.service.FileStorageService;
import ru.geekbrains.cookbook.service.UploadFileService;
import ru.geekbrains.cookbook.service.UserService;
import java.util.ArrayList;
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
        String fileKey = fileStorageService.save(file);
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFilename(fileKey);
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
    public byte[] getFileContent(String filename) {
        return fileStorageService.download(filename);
    }

    @Override
    @Transactional
    public UploadedFileLink saveUploadFileLink(UploadedFileLinkDto uploadedFileLinkDto) {
        String filename = uploadedFileLinkDto.getFilename();
        UploadedFile uploadedFile = uploadedFileRepository.getByFilename(filename).orElseThrow(RuntimeException::new);
        UploadedFileLink uploadedFileLink = new UploadedFileLink();
        uploadedFileLink.setObjectId(uploadedFileLinkDto.getObjectId());
        uploadedFileLink.setObjectType(uploadedFileLinkDto.getObjectType());
        uploadedFileLink.setObjectPart(uploadedFileLinkDto.getObjectPart());
        uploadedFileLink.setDescription(uploadedFileLinkDto.getDescription());
        uploadedFileLink.setUploadedFile(uploadedFile);
        uploadedFileLinkRepository.save(uploadedFileLink);
        return uploadedFileLink;
    }

    @Override
    @Transactional
    public UploadedFileLink getUploadedFileLink(Long linkId) {
        return uploadedFileLinkRepository.findById(linkId).orElseThrow(RuntimeException::new);
    }

    @Override
    @Transactional
    public boolean removeUploadedFileLink(Long linkId) {
        uploadedFileLinkRepository.deleteById(linkId);
        return true;
    }

    @Override
    @Transactional
    public LinkedFiles getUploadedFileListByResource(Long objectId, Class<?> objectType) {
        List<UploadedFileLink> uploadedFileLinks = uploadedFileLinkRepository.findAllByObjectIdAndObjectType(objectId, objectType.getSimpleName());
        LinkedFiles linkedFiles = transformToLinkedFiles(uploadedFileLinks);
        return linkedFiles;
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
                List<LinkedFiles.FileInfo> fileInfoList = linkedFiles.getEmbeddedFiles().get(objectPart);
                if(fileInfoList == null){
                    fileInfoList = new ArrayList<>();
                    fileInfoList.add(fileInfo);
                    linkedFiles.getEmbeddedFiles().put(objectPart, fileInfoList);
                }
            }
        }
        return linkedFiles;
    }

}
