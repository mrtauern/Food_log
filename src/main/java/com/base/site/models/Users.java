package com.base.site.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "firstname")
    private String firstname;

    @Basic
    @Column(name = "lastname")
    private String lastname;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @Transient
    private String sBirthday;

    /*
    @Basic
    @Column(name = "birthday")
    private Timestamp birthday;
    */
    @Basic
    @Column(name = "birthday")
    private LocalDate birthday;

    @Setter(AccessLevel.NONE)
    @Basic
    @Column(name = "register_date")
    @CreationTimestamp
    private Timestamp redisterDate;

    @Basic
    @Column(name = "start_weight")
    private double startWeight;

    @Basic
    @Column(name = "current_weight")
    private double currentWeight;

    @Basic
    @Column(name = "goal_weight")
    private double goalWeight;

    @Basic
    @Column(name = "height")
    private int height;

    @Basic
    @Column(name = "bmi")
    private double bmi = 1;

    /*
    @Basic
    @Column(name = "fk_user_type_id")
    private long fkUserTypeId = 3;
*/
    @ManyToOne
    @JoinColumn(name="fk_user_type_id")
    private UserType userType;

    @Basic
    @Column(name = "roles")
    private String roles;

    @OneToMany(mappedBy="fkUser", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<DailyLog> dailyLogs;

    @OneToMany(mappedBy="fkUser", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<PrivateFood> privateFoods;

    public Users(String firstname, String lastname, String username, String password, UserType userType, String roles, double bmi) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.roles = roles;
        this.bmi = bmi;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    //basal metabolic rate, how many kcal a person burns without doing any exercise
    //w is weight in kg, h is height in cm, a is age,
    //men BMR = ((10*w)+(6,25*height))-(5*age)+5
    //women BMR = ((10*w)+(6,25*height))-(5*age)-161
    public int getBMR(double weight) {
        int bmr = 0;
        if(getHeight() != 0 && getBirthday() != null) {

            setCurrentWeight(weight);
            LocalDate currentDate = LocalDate.now();

            int age = Period.between(getBirthday(), currentDate).getYears();


            if (userType.getType().equals("User_male")) {
                bmr = (int)((10 * getCurrentWeight()) + (6.25 * getHeight()) - ((5 * age) + 5));
                bmr = (int)(bmr*1.2);
            } else if(userType.getType().equals("User_female")) {
                bmr = (int)((10 * getCurrentWeight()) + (6.25 * getHeight()) - ((5 * age) - 161));
                bmr = (int)(bmr*1.2);
            }
        }
        return bmr;
    }

    @Override
    public String toString() {
        return "Users{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
