package com.base.site.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "register_date")
    private LocalDate registerDate;

    @Column(name = "start_weight")
    private Double startWeight;

    @Column(name = "current_weight")
    private Double currentWeight;

    @Column(name = "goal_weight")
    private Double goalWeight;

    @Column(name = "height")
    private Integer height;

    @Column(name = "bmi")
    private Double bmi;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_user_type_id", nullable = false)
    private UserType fkUserType;

    @Column(name = "account_non_locked", nullable = false)
    private Integer accountNonLocked;

    @Column(name = "roles", length = 45)
    private String roles;



    /*
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", registerDate=" + registerDate +
                ", startWeight=" + startWeight +
                ", currentWeight=" + currentWeight +
                ", goalWeight=" + goalWeight +
                ", height=" + height +
                ", bmi=" + bmi +
                ", fkUserType=" + fkUserType +
                ", accountNonLocked=" + accountNonLocked +
                ", roles='" + roles + '\'' +
                '}';
    }

     */
}