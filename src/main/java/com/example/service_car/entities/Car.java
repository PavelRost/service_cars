package com.example.service_car.entities;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String vin;
    private String regNumber;
    private String manufacturer;
    private String brand;
    private String yearManufacture;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "car_detail",
            joinColumns = @JoinColumn(name = "car_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "detail_id", referencedColumnName = "id"))
    private Set<Detail> details = new HashSet<>();

    public Car() {
    }

    public Car(int id) {
        this.id = id;
    }

    public Car(int id, String vin) {
        this.id = id;
        this.vin = vin;
    }

    public Car(int id, String vin, Set<Detail> details) {
        this.id = id;
        this.vin = vin;
        this.details = details;
    }

    public Car(int id, String vin, String regNumber, String manufacturer, String brand, String yearManufacture, Set<Detail> details) {
        this.id = id;
        this.vin = vin;
        this.regNumber = regNumber;
        this.manufacturer = manufacturer;
        this.brand = brand;
        this.yearManufacture = yearManufacture;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public String getVin() {
        return vin;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getBrand() {
        return brand;
    }

    public String getYearManufacture() {
        return yearManufacture;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addDetail(Detail detail) {
        details.add(detail);
    }

    public void deleteDetail(Detail detail) {
        details.remove(detail);
    }

    public Set<Detail> getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && Objects.equals(vin, car.vin) && Objects.equals(regNumber, car.regNumber) && Objects.equals(manufacturer, car.manufacturer) && Objects.equals(brand, car.brand) && Objects.equals(yearManufacture, car.yearManufacture) && Objects.equals(details, car.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vin, regNumber, manufacturer, brand, yearManufacture, details);
    }
}
