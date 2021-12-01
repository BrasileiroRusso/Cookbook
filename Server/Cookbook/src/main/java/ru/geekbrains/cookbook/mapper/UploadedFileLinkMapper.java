package ru.geekbrains.cookbook.mapper;

import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import ru.geekbrains.cookbook.dto.UploadedFileLinkDto;
import java.util.List;
import java.util.stream.Collectors;

public class UploadedFileLinkMapper {
    public static UploadedFileLinkDto uploadedFileLinkToDto(UploadedFileLink uploadedFileLink){
        UploadedFileLinkDto uploadedFileLinkDto = new UploadedFileLinkDto();
        uploadedFileLinkDto.setObjectId(uploadedFileLink.getObjectId());
        uploadedFileLinkDto.setObjectType(uploadedFileLink.getObjectType());
        uploadedFileLinkDto.setObjectPart(uploadedFileLink.getObjectPart());
        uploadedFileLinkDto.setFilename(uploadedFileLink.getUploadedFile().getFilename());
        uploadedFileLinkDto.setDescription(uploadedFileLink.getDescription());
        return uploadedFileLinkDto;
    }

    public static List<UploadedFileLinkDto> uploadedFileLinkListToDto(List<UploadedFileLink> uploadedFileLinks){
        return uploadedFileLinks.stream()
                .map(UploadedFileLinkMapper::uploadedFileLinkToDto)
                .collect(Collectors.toList());
    }
}
