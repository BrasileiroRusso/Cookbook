package ru.geekbrains.cookbook.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(exclude = {"units"})
public class UnitTypeDto {
    private Long id;
    @NotNull
    @NotEmpty
    private String briefName;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private String description;
    private Long baseUnitId;
    private List<UnitDto> units;
}
