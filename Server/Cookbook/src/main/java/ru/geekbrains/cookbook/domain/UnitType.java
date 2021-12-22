package ru.geekbrains.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "unit_type")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"units"})
public class UnitType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brief_name")
    private String briefName;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "unitType", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private Set<Unit> units;
}
