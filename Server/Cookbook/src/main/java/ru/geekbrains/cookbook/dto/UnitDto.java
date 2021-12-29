package ru.geekbrains.cookbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class UnitDto {
    private Long id;
    @NotNull
    @NotEmpty
    private String briefName;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private Long unitTypeId;
    private String unitType;
    private BigDecimal measure;
}
