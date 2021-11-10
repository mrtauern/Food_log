package com.base.site.models;

import javax.persistence.*;

@Table(name = "private_food")
@Entity
public class PrivateFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}