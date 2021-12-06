package ru.geekbrains.cookbook.controller.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.domain.file.UploadedFile;
import ru.geekbrains.cookbook.domain.file.UploadedFileLink;
import ru.geekbrains.cookbook.dto.UploadedFileDto;
import ru.geekbrains.cookbook.dto.UploadedFileLinkDto;
import ru.geekbrains.cookbook.service.UploadFileService;
import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/file")
@Tag(name = "Хранилище файлов", description = "API для загрузки и получения файлов")
public class FileController {
    private UploadFileService uploadFileService;

    @PostMapping(path = "/upload",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE
                )
    public ResponseEntity<?> uploadFile(@RequestParam(value = "userId") Long userId,
                                        MultipartFile file){
        UploadedFile uploadFile = uploadFileService.uploadFile(userId, file);
        return ResponseEntity.created(getFileUrl(uploadFile.getFilename())).build();
    }

    @GetMapping
    public ResponseEntity<?> getUploadedFileList(@RequestParam(value = "userId") Long userId){
        List<UploadedFile> uploadedFileList = uploadFileService.getAllUploadedFiles(userId);
        List<UploadedFileDto> uploadedFileDtoList = uploadedFileList.stream()
                .map(this::uploadedFileToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(uploadedFileDtoList);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<?> getFile(@PathVariable("filename") String filename){
        byte[] file = uploadFileService.getFileContent(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+filename+"\"")
                .body(file);
    }

    @PostMapping("/{filename:.+}/link")
    public ResponseEntity<?> addFileLink(@PathVariable("filename") String filename,
                                         @RequestBody UploadedFileLinkDto uploadedFileLinkDto) {
        uploadedFileLinkDto.setFilename(filename);
        if(uploadedFileLinkDto.getObjectPart() == null)
            uploadedFileLinkDto.setObjectPart("");
        UploadedFileLink uploadedFileLink = uploadFileService.saveUploadFileLink(uploadedFileLinkDto);
        URI newLinkUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(uploadedFileLink.getId())
                .toUri();
        return ResponseEntity.created(newLinkUri).build();
    }

    @GetMapping("/{filename:.+}/link/{id}")
    public ResponseEntity<?> getFileLink(@PathVariable("filename")String filename,
                                         @PathVariable("id") Long linkId) {
        UploadedFileLink uploadedFileLink = uploadFileService.getUploadedFileLink(linkId);
        return ResponseEntity.ok().body(uploadedFileLink);
    }

    @DeleteMapping("/{filename:.+}/link/{id}")
    public ResponseEntity<?> deleteFileLink(@PathVariable("filename")String filename,
                                            @PathVariable("id") Long linkId) {
        uploadFileService.removeUploadedFileLink(linkId);
        return ResponseEntity.ok().build();
    }

    private static URI getFileUrl(String filename){
        return MvcUriComponentsBuilder.fromController(FileController.class)
                                      .path("/{filename}")
                                      .buildAndExpand(filename)
                                      .toUri();
    }

    public static LinkedFiles transformUriInLinkedFiles(LinkedFiles linkedFiles){
        Consumer<LinkedFiles.FileInfo> cons = fileInfo -> fileInfo.setFileUri(getFileUrl(fileInfo.getFileUri()).toString());
        linkedFiles.getFiles().forEach(cons);
        linkedFiles.getEmbeddedFiles().forEach((key, value) -> value.forEach(cons));
        return linkedFiles;
    }

    private UploadedFileDto uploadedFileToDto(UploadedFile uploadedFile){
        UploadedFileDto uploadedFileDto = new UploadedFileDto();
        uploadedFileDto.setUploadTime(uploadedFile.getUploadTime());
        uploadedFileDto.setFileUri(getFileUrl(uploadedFile.getFilename()));
        return uploadedFileDto;
    }
}
