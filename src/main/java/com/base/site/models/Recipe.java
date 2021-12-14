package com.base.site.models;

import com.base.site.services.FoodService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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

    @Transient
    @Autowired
    FoodService foodService;

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

    public Food getNutritionFromRecipe() {
        Food nutrition = new Food("nutrition",0.0,0.0,0.0,0.0,0.0);
        for (RecipeFood recipeFood : amounts){
            if (recipeFood.getFood() != null){
                nutrition = foodService.setAddFoodNutritionFromRecipe(nutrition, recipeFood, "food");
            }
            else if (recipeFood.getPrivateFood() != null){
                nutrition = foodService.setAddFoodNutritionFromRecipe(nutrition, recipeFood, "pfood");
            }
        }
        return nutrition;
    }

    //Only for not having error -- should be deleted amd fixed

    public Food setAddFoodNutritionFromRecipe(Food nutrition, RecipeFood recipeFood, String type) {
        double fat = type.equals("food") ? (recipeFood.getFood().getFat() * recipeFood.getAmount()) / 100 : (recipeFood.getPrivateFood().getFat() * recipeFood.getAmount()) / 100;
        double carbs = type.equals("food") ? (recipeFood.getFood().getCarbohydrates() * recipeFood.getAmount()) / 100 : (recipeFood.getPrivateFood().getCarbohydrates() * recipeFood.getAmount()) / 100;
        double protein = type.equals("food") ? (recipeFood.getFood().getProtein() * recipeFood.getAmount()) / 100 : (recipeFood.getPrivateFood().getProtein() * recipeFood.getAmount()) / 100;
        double kj = type.equals("food") ? (recipeFood.getFood().getEnergy_kilojoule() * recipeFood.getAmount()) / 100 : (recipeFood.getPrivateFood().getEnergy_kilojoule() * recipeFood.getAmount()) / 100;
        double kcal = type.equals("food") ? (recipeFood.getFood().getEnergy_kcal() * recipeFood.getAmount()) / 100 : (recipeFood.getPrivateFood().getEnergy_kcal() * recipeFood.getAmount()) / 100;

        nutrition.setFat(nutrition.getFat() + fat);
        nutrition.setCarbohydrates(nutrition.getCarbohydrates() + carbs);
        nutrition.setProtein(nutrition.getProtein() + protein);
        nutrition.setEnergy_kilojoule(nutrition.getEnergy_kilojoule() + kj);
        nutrition.setEnergy_kcal(nutrition.getEnergy_kcal() + kcal);

        return nutrition;
    }



}