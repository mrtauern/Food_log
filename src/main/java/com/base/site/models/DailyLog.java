package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @CreationTimestamp
    private LocalDate datetime;

    @Transient
    private String sDatetime;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "fk_food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "fk_private_food_id")
    private PrivateFood privateFood;

    @Column(name = "current_weight")
    private Double currentWeight;

    /*public DailyLog() {
        LocalDate ldt = LocalDate.now();
        datetime = ldt;
    }*/

    //String format yyyy-mm-dd
    public DailyLog(String date) {
        datetime = LocalDate.parse(date);
    }

}

