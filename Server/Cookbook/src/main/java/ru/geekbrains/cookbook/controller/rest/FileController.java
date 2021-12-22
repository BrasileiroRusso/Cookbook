package ru.geekbrains.cookbook.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.domain.file.UploadedFile;
import ru.geekbrains.cookbook.dto.UploadedFileDto;
import ru.geekbrains.cookbook.service.UploadFileService;
import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/file")
@Tag(name = "Хранилище файлов", description = "API для загрузки и получения файлов")
public class FileController {
    private UploadFileService uploadFileService;

    @Operation(summary = "Загружает новый файл", description = "Загружает новый файл в хранилище")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно", content = @Content, headers = @Header(name = "Location", required = true, description = "URI загруженного файла")),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/upload",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE
                )
    public ResponseEntity<?> uploadFile(@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "binary"))) @RequestPart("file") MultipartFile file,
                                        Authentication authentication){
        String username = authentication.getName();
        UploadedFile uploadFile = uploadFileService.uploadFile(username, file);
        return ResponseEntity.created(URI.create(getFileUrl(uploadFile.getFilename()))).build();
    }

    @Operation(summary = "Возвращает список файлов, загруженных пользователем")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UploadedFileDto.class)))
                    })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<?> getUploadedFileList(Authentication authentication){
        String username = authentication.getName();
        List<UploadedFile> uploadedFileList = uploadFileService.getAllUploadedFiles(username);
        List<UploadedFileDto> uploadedFileDtoList = uploadedFileList.stream()
                .map(this::uploadedFileToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(uploadedFileDtoList);
    }

    @Operation(summary = "Возвращает содержимое файла в бинарном формате")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(type = "string", format = "binary"))
                    })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{filekey:.+}")
    public ResponseEntity<?> getFile(@Parameter(description = "Уникальное наименование файла", required = true) @PathVariable("filekey") String filekey){
        byte[] file = uploadFileService.getFileContent(filekey);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+filekey+"\"")
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

    private UploadedFileDto uploadedFileToDto(UploadedFile uploadedFile){
        UploadedFileDto uploadedFileDto = new UploadedFileDto();
        uploadedFileDto.setUploadTime(uploadedFile.getUploadTime());
        uploadedFileDto.setFileUri(getFileUrl(uploadedFile.getFilename()));
        return uploadedFileDto;
    }
}
