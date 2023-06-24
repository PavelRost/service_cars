package com.example.service_car.repository;

import com.example.service_car.entities.Detail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailRepository extends CrudRepository<Detail, Integer> {

    Optional<Detail> findBySerialNumber(String serialNumber);

    Slice<Detail> findAll(Pageable page);

}
