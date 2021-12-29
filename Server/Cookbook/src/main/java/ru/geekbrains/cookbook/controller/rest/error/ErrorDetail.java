package ru.geekbrains.cookbook.controller.rest.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    private String title;
    private String detail;
    private long timeStamp;
    private String message;
}
