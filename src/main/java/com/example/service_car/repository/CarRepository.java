package com.example.service_car.repository;

import com.example.service_car.entities.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends CrudRepository<Car, Integer> {

    Optional<Car> findByVin(String vin);

    Slice<Car> findAll(Pageable page);

}
