package ru.geekbrains.cookbook.controller.mvc;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.domain.file.UploadedFile;
import ru.geekbrains.cookbook.service.UploadFileService;

import java.net.URI;
import java.util.function.Consumer;

@Controller("mvcFileController")
@AllArgsConstructor
@RequestMapping("/file")
public class FileController {
    private UploadFileService uploadFileService;

    @GetMapping("/{filekey:.+}")
    public ResponseEntity<?> getFile(@PathVariable("filekey") String filekey){
        byte[] file = uploadFileService.getFileContent(filekey);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().toString())
                .body(file);
    }

    @PostMapping(path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> uploadFile(@RequestPart("file") MultipartFile file,
                                        Authentication authentication){
        String username = authentication.getName();
        UploadedFile uploadFile = uploadFileService.uploadFile(username, file);
        return ResponseEntity.ok(uploadFile);
    }

    public static String getFileUrl(String filename){
        if(filename == null || filename.isEmpty())
            return "";
        return MvcUriComponentsBuilder.fromController(FileController.class)
                                      .path("/{filename}")
                                      .buildAndExpand(filename)
                                      .toUri()
                                      .toString();
    }

    public static LinkedFiles transformUriInLinkedFiles(LinkedFiles linkedFiles){
        Consumer<LinkedFiles.FileInfo> cons = fileInfo -> fileInfo.setFileUri(getFileUrl(fileInfo.getFileUri()));
        linkedFiles.getFiles().forEach(cons);
        linkedFiles.getEmbeddedFiles().forEach((key, value) -> value.forEach(cons));
        return linkedFiles;
    }

}
