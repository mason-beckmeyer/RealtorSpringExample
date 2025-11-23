package com.example.demo.dao;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Data
public class OldHouse {

    private Long id;
    private String owner;
    private Double price;
    private String address;
}
