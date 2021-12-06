package ru.geekbrains.cookbook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UploadedFileLinkDto {
    private Long objectId;
    private String objectType;
    private String objectPart;
    private String filename;
    private String description;
}

