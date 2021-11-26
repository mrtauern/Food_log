package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercise")
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

    public Exercise(Long id, Integer kcalBurnedPerMin, String name) {
        this.id = id;
        this.kcalBurnedPerMin = kcalBurnedPerMin;
        this.name = name;
    }
}