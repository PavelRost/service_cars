package com.example.service_car.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "details")
public class Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private final String serialNumber = UUID.randomUUID().toString();

    @ManyToMany(mappedBy = "details")
    @JsonIgnore
    private Set<Car> cars = new HashSet<>();

    public Detail() {
    }

    public Detail(int id) {
        this.id = id;
    }

    public Detail(int id, Set<Car> cars) {
        this.id = id;
        this.cars = cars;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Set<Car> getCars() {
        return cars;
    }
}
