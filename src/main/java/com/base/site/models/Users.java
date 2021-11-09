package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "firstname")
    private String firstname;

    @Basic
    @Column(name = "lastname")
    private String lastname;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "birthday")
    private Timestamp birthday;

    @Basic
    @Column(name = "register_date")
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
    private double bmi;

    @Basic
    @Column(name = "actime")
    private byte active;

    @Basic
    @Column(name = "fk_user_type_id")
    private double fkUserTypeId;
}
