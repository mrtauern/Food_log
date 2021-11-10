package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_type")
public class UserType {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy="userType", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Users> users;
}
