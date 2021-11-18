package com.base.site.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Table(name = "exercise")
@Getter
@Setter
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "kcal_burned_per_min")
    private Integer kcalBurnedPerMin;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy="fkExercise", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<DailyLog> dailyLogs;

}