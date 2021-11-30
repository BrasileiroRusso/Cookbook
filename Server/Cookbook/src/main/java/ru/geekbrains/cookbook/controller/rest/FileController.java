package ru.geekbrains.cookbook.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.geekbrains.cookbook.domain.file.UploadedFile;
import ru.geekbrains.cookbook.dto.UploadedFileDto;
import ru.geekbrains.cookbook.service.UploadFileService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/file")
public class FileController {
    private UploadFileService uploadFileService;

    @PostMapping("/upload")
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
    public ResponseEntity<Resource> getFile(@PathVariable("filename")String filename){
        Resource file = uploadFileService.getFileContent(filename);
        return ResponseEntity.ok()
                //.header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    private URI getFileUrl(String filename){
        return MvcUriComponentsBuilder.fromController(FileController.class)
                                      .path("/{filename}")
                                      .buildAndExpand(filename)
                                      .toUri();
    }

    private UploadedFileDto uploadedFileToDto(UploadedFile uploadedFile){
        UploadedFileDto uploadedFileDto = new UploadedFileDto();
        uploadedFileDto.setUploadTime(uploadedFile.getUploadTime());
        uploadedFileDto.setFileUri(getFileUrl(uploadedFile.getFilename()));
        return uploadedFileDto;
    }
}
