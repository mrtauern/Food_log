package com.base.site.models;

import org.springframework.data.annotation.Id;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name="exercise_type")
public class ExerciseType {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "kcal_burned_per_min is mandatory")
    private int kcal_burned_per_min;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @OneToMany(mappedBy="exerciseType", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Exercise> exerciseTypes;

    public ExerciseType() {}

    public ExerciseType(long id, int kcal_burned_per_min, String name, Set<Exercise> exerciseTypes) {
        this.id = id;
        this.kcal_burned_per_min = kcal_burned_per_min;
        this.name = name;
        this.exerciseTypes = exerciseTypes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getKcal_burned_per_min() {
        return kcal_burned_per_min;
    }

    public void setKcal_burned_per_min(int kcal_burned_per_min) {
        this.kcal_burned_per_min = kcal_burned_per_min;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Exercise> getExerciseTypes() {
        return exerciseTypes;
    }

    public void setExerciseTypes(Set<Exercise> exerciseTypes) {
        this.exerciseTypes = exerciseTypes;
    }
}
