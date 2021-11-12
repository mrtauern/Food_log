package com.base.site.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@Entity
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



    @ManyToMany(mappedBy = "food")
    private List<DailyLog> dailyLog = new ArrayList<>();

    /*
    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", protein=" + protein +
                ", carbohydrates=" + carbohydrates +
                ", fat=" + fat +
                ", energy_kilojoule=" + energy_kilojoule +
                ", energy_kcal=" + energy_kcal +
                ", dailyLog=" + dailyLog +
                '}';
    }

     */
}
