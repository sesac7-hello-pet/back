package com.sesac7.hellopet.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_details")
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String nickname;

    @Getter
    @Column(nullable = false)
    private String username;

    @Getter
    @Setter
    private String userProfileUrl;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
