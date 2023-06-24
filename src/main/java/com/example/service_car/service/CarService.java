package com.example.service_car.service;

import com.example.service_car.entities.Car;
import com.example.service_car.entities.Detail;
import com.example.service_car.repository.CarRepository;
import com.example.service_car.repository.DetailRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final DetailRepository detailRepository;

    public CarService(CarRepository carRepository, DetailRepository detailRepository) {
        this.carRepository = carRepository;
        this.detailRepository = detailRepository;
    }

    public boolean create(Car newCar) {
        boolean rsl = false;
        Car car = carRepository.findByVin(newCar.getVin()).orElse(null);
        if (car == null) {
            if (!isCorrectNumberDetail(newCar.getDetails())) {
                return false;
            }
            carRepository.save(newCar);
            rsl = true;
        }
        return rsl;
    }

    public boolean update(int id, Car newCar) {
        boolean rsl = carRepository.existsById(id);
        if (rsl) {
            if (!isCorrectNumberDetail(newCar.getDetails())) {
                return false;
            }
            newCar.setId(id);
            carRepository.save(newCar);
        }
        return rsl;
    }

    public boolean delete(int id) {
        boolean rsl = carRepository.existsById(id);
        if (rsl) {
            carRepository.deleteById(id);
        }
        return rsl;
    }

    public List<Car> findAll(int offset, int limit, String direction, String field) {
        switch (direction) {
            case "asc" -> { return carRepository.findAll(PageRequest.of(offset, limit, Sort.by(field))).getContent(); }
            case "desc" -> { return carRepository.findAll(PageRequest.of(offset, limit, Sort.by(field).descending())).getContent(); }
            default -> { return List.of(); }
        }
    }

    public Car findById(int id) { return carRepository.findById(id).orElse(null); }

    public Car findByVin(String vin) {
        return carRepository.findByVin(vin).orElse(null);
    }

    public boolean addDetailOnCar(int idCar, String serialNumberDetail) {
        boolean rsl = false;
        Detail detail = detailRepository.findBySerialNumber(serialNumberDetail).orElse(null);
        Car car = carRepository.findById(idCar).orElse(null);
        if (detail != null && car != null) {
            if (car.getDetails().contains(detail)) return false;
            car.addDetail(detail);
            carRepository.save(car);
            rsl = true;
        }
        return rsl;
    }

    public boolean deleteDetailOnCar(int idCar, String serialNumberDetail) {
        boolean rsl = false;
        Detail detail = detailRepository.findBySerialNumber(serialNumberDetail).orElse(null);
        Car car = carRepository.findById(idCar).orElse(null);
        if (detail != null && car != null) {
            if (car.getDetails().contains(detail)) {
                car.deleteDetail(detail);
                carRepository.save(car);
                rsl = true;
            }
        }
        return rsl;
    }

    private boolean isCorrectNumberDetail(Set<Detail> details) {
        for (var detail : details) {
            Optional<Detail> tmpDetail = detailRepository.findBySerialNumber(detail.getSerialNumber());
            if (tmpDetail.isEmpty()) {
                return false;
            }
            detail.setId(tmpDetail.get().getId());
        }
        return true;
    }



}
