package ru.geekbrains.cookbook.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

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

}
