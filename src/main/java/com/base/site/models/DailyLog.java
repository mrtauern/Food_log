package com.base.site.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Table(name = "daily_log")
@Getter
@Setter
@Entity
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private Users1 fkUser;

    @ManyToOne
    @JoinColumn(name = "fk_food_id")
    private Food fkFood;

    @ManyToOne
    @JoinColumn(name = "fk_log_type")
    private LogType fkLogType;

    @ManyToOne
    @JoinColumn(name = "fk_exercise_id")
    private Exercise fkExercise;

    @Column(name = "datetime")
    private Timestamp datetime;

    @Column(name = "food_amount")
    private Double foodAmount;

    @ManyToOne
    @JoinColumn(name = "fk_private_food_id")
    private PrivateFood fkPrivateFood;
/*
    public PrivateFood getFkPrivateFood() {
        return fkPrivateFood;
    }

    public void setFkPrivateFood(PrivateFood fkPrivateFood) {
        this.fkPrivateFood = fkPrivateFood;
    }

    public Double getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(Double foodAmount) {
        this.foodAmount = foodAmount;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public Exercise getFkExercise() {
        return fkExercise;
    }

    public void setFkExercise(Exercise fkExercise) {
        this.fkExercise = fkExercise;
    }

    public LogType getFkLogType() {
        return fkLogType;
    }

    public void setFkLogType(LogType fkLogType) {
        this.fkLogType = fkLogType;
    }

    public Food getFkFood() {
        return fkFood;
    }

    public void setFkFood(Food fkFood) {
        this.fkFood = fkFood;
    }

    public Users1 getFkUser() {
        return fkUser;
    }

    public void setFkUser(Users1 fkUser) {
        this.fkUser = fkUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

 */
}