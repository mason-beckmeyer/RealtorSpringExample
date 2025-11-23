package com.example.demo.dao;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "houses")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String owner;
    private Double price;
    private String address;
}
