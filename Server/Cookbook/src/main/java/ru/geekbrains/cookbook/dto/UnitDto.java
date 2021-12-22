package ru.geekbrains.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class UnitDto {
    private Long id;
    private String briefName;
    private String name;
    private Long unitTypeId;
    private String unitType;
    private BigDecimal measure;
}
