package com.coinchange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "COINS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coin {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DENOMINATION", nullable = false)
    private double denomination;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

}