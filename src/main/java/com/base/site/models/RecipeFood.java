package com.base.site.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "recipe_food")
public class RecipeFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_recipe_id")
    Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "fk_food_id")
    Food food;

    @Basic
    @Column(name = "amount")
    int amount;
}
