package com.base.site.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "recipe")
public class Recipe implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "total_weight")
    private double total_weight;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="fk_user_id", nullable = false)
    private Users fkUser;

    /*
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "recipe_food",
        joinColumns = {@JoinColumn(name = "fk_recipe_id", referencedColumnName = "id", nullable = false, updatable = false)},
        inverseJoinColumns = {@JoinColumn(name = "fk_food_id", referencedColumnName = "id", nullable = false, updatable = false)})
        private Set<Food> foods = new HashSet<>();
    */
    /*
    @ManyToMany(mappedBy = "recipes", fetch = FetchType.LAZY)
    private Set<Food> foods = new HashSet<>();
    */

    //private List<RecipeFood> recipeFoods;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<RecipeFood> amounts;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "archived")
    private boolean archived;

    public double getCalculateCaloriesInRecipe() {
        double total = 0;
        for (RecipeFood recipeFood : amounts) {
            if(recipeFood.getFood() != null) {
                total += (recipeFood.getFood().getEnergy_kcal()*recipeFood.getAmount() ) / 100;
            } else if (recipeFood.getPrivateFood() != null) {
                total +=( recipeFood.getPrivateFood().getEnergy_kcal()*recipeFood.getAmount()) / 100;
            }
        }
        total = (total/getTotal_weight())*100;
        return total;
    }
}