package com.base.site.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "food_dailylog" )
public class FoodDailylog implements Serializable {
    @Id
    @Column(name = "food_id_fk")
    private int foodIdFk;
    @Id
    @Column(name = "dailylog_id_fk")
    private int dailylogIdFk;

}