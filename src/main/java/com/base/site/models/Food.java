package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double protein;
    private double carbohydrates;
    private double fat;
    private double energy_kilojoule;
    private double energy_kcal;

    @OneToMany(mappedBy="food", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<DailyLog> dailyLogs;

    @ManyToMany(mappedBy = "recipes", fetch = FetchType.LAZY)
    private Set<Recipe> recipes = new HashSet<>();

}
