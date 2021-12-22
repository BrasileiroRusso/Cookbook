package ru.geekbrains.cookbook.dto;

import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(exclude = {"units"})
public class UnitTypeDto {
    private Long id;
    private String briefName;
    private String name;
    private String description;
    private Long baseUnitId;
    private List<UnitDto> units;
}
