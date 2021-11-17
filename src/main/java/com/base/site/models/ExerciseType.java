package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="exercise_type")
public class ExerciseType {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "kcal_burned_per_min is mandatory")
    private int kcal_burned_per_min;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @OneToMany(mappedBy="exerciseType", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Exercise> exerciseTypes;

}
