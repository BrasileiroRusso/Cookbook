package ru.geekbrains.cookbook.controller.mvc;

import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.service.UploadFileService;
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
