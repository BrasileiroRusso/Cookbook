package ru.geekbrains.cookbook.controller.rest.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    private String title;
    private String detail;
    private long timeStamp;
    private String message;
    private Map<String, List<ValidationError>> errors = new HashMap<>();
}
