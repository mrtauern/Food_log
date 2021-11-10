package com.base.site.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int protein;
    private int carbohydrates;
    private int fat;
    private double energy_kilojoule;
    private double energy_kcal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public double getEnergy_kilojoule() {
        return energy_kilojoule;
    }

    public void setEnergy_kilojoule(double energy_kilojoule) {
        this.energy_kilojoule = energy_kilojoule;
    }
    public double getEnergy_kcal() {
        return energy_kcal;
    }

    public void setEnergy_kcal(double energy_kcal) {
        this.energy_kcal = energy_kcal;
    }
}
