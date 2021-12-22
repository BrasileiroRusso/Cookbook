package ru.geekbrains.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "unit")
@Data
@NoArgsConstructor
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brief_name", nullable = false)
    private String briefName;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_type_id")
    @JsonIgnore
    private UnitType unitType;

    @Column(name = "measure")
    @JsonIgnore
    private BigDecimal measure;

    @Column(name = "is_main")
    @JsonIgnore
    private boolean main;

}
