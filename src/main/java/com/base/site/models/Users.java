package com.base.site.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
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

    @Basic
    @Column(name = "birthday")
    private Timestamp birthday;

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
    private double bmi = 0;

    @Basic
    @Column(name = "fk_user_type_id")
    private long fkUserTypeId = 3;

    @Basic
    @Column(name = "roles")
    private String roles;

    @OneToMany(mappedBy="fkUser", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<DailyLog> dailyLogs;

    public Users(String firstname, String lastname, String username, String password, long fkUserTypeId, String roles, double bmi) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.fkUserTypeId = fkUserTypeId;
        this.roles = roles;
        this.bmi = bmi;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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
