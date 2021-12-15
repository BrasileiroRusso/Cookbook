package ru.geekbrains.cookbook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.URI;
import java.util.Date;

@Data
@NoArgsConstructor
public class UploadedFileDto {
    private String fileUri;
    private Date uploadTime;
}
