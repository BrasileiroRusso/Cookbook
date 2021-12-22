package ru.geekbrains.cookbook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileLinkDto {
    private String fileKey;
    private String description;
}
