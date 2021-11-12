package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "daily_log")
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User fkUser;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "food_dailylog",
            joinColumns = @JoinColumn(name = "dailylog_id_fk"),
            inverseJoinColumns = @JoinColumn(name = "food_id_fk")
    )
    private List<Food> food = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "fk_log_type")
    private LogType fkLogType;

    @ManyToOne
    @JoinColumn(name = "fk_exercise_id")
    private Exercise fkExercise;

    @Column(name = "datetime")
    private Instant datetime;

    @Column(name = "food_amount")
    private Double foodAmount;

    @ManyToOne
    @JoinColumn(name = "fk_private_food_id")
    private PrivateFood fkPrivateFood;
/*
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "fkUser = " + fkUser + ", " +
                "fkLogType = " + fkLogType + ", " +
                "fkExercise = " + fkExercise + ", " +
                "datetime = " + datetime + ", " +
                "foodAmount = " + foodAmount + ", " +
                "fkPrivateFood = " + fkPrivateFood + ")";
    }

 */
}

