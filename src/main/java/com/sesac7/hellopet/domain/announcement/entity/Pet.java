package com.sesac7.hellopet.domain.announcement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pets")
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String health;

    @Column(nullable = false)
    private String personality;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String foundPlace;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private String date;
}
