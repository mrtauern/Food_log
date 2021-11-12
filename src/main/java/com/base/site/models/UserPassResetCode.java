package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="reset_passcode")
public class UserPassResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "used")
    private boolean used;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    public String generateCode() {
        int length = 6;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
}
