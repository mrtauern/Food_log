package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "private_food")
@Entity
public class PrivateFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private Integer protein;
    private Integer carbohydrates;
    private Integer fat;
    private Double energy_kilojoule;
    private Double energy_kcal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private Users fkUser;

    @OneToMany(mappedBy="privateFood", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<DailyLog> dailyLogs;
/*
    @OneToMany(mappedBy = "privateFood", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<RecipeFood> amounts;

 */

}