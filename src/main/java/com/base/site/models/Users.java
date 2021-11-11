package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

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
    @Column(name = "active")
    private byte active;

    @Basic
    @Column(name = "fk_user_type_id")
    private long fkUserTypeId;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    public boolean getAccountNonLocked() {
        return accountNonLocked;
    }
    @Override
    public Collection<GrantedAuthority> getAuthorities() {

        return List.of(() -> "read");
    }

    @Override
    public String getUsername() {
        return email;
    }


}
