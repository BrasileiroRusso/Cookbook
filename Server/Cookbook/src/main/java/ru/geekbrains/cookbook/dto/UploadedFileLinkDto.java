package ru.geekbrains.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFileLinkDto {
    private Long objectId;
    private String objectType;
    private String objectPart;
    private String filename;
    private String description;
}

