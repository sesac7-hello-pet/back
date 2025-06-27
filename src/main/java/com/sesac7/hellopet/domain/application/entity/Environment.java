package com.sesac7.hellopet.domain.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "environments")
public class Environment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
