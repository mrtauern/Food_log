package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
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


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="fk_user_id", nullable = false)
    private Users fkUser;



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
    @JoinColumn(name = "fk_food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "fk_private_food_id")
    private PrivateFood privateFood;

}

