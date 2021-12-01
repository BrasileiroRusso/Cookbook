package ru.geekbrains.cookbook.domain.file;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor
public class LinkedFiles {
    private List<FileInfo> files = new ArrayList<>();
    private Map<String, List<FileInfo>> embeddedFiles = new TreeMap<>();

    @Data
    @NoArgsConstructor
    public static class FileInfo {
        private String fileUri;
        private String description;
    }
}
