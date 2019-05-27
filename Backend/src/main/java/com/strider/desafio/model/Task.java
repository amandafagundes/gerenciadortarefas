package com.strider.desafio.model;


import lombok.Data;

import javax.persistence.Id;

import javax.persistence.*;

@Data
@Entity(name = "com.strider.desafio.model.Task")
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "status")
    private String status;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
