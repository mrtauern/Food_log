package com.base.site.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_type", schema = "food_log", catalog = "")
public class UserType {
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "type")
    private String type;
}
