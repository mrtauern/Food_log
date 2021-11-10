package com.base.site.models;

import org.springframework.data.annotation.Id;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name="exercise")
public class Exercise {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Time is mandatory")
    private int time_min;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="fk_exercise_type_id", nullable = false)
    private ExerciseType exerciseType;

    /*
    @OneToMany(mappedBy="exercise", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ExerciseType> exerciseTypes;
*/
    public Exercise() {
    }

    public Exercise(long id, int time_min, ExerciseType exerciseType) {
        this.id = id;
        this.time_min = time_min;
        this.exerciseType = exerciseType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTime_min() {
        return time_min;
    }

    public void setTime_min(int time_min) {
        this.time_min = time_min;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }
}
