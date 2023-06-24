package com.example.service_car.controller;


import com.example.service_car.entities.Car;
import com.example.service_car.service.CarService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.service_car.controller.ApplicationMessage.response;

@RestController
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Car car) {
        boolean rsl = carService.create(car);
        if (rsl) return response("Машина добавлена успешно", HttpStatus.CREATED);
        return response("VIN машины был добавлен ранее / ошибка в серийном номере детали", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam int id, @RequestBody Car car) {
        boolean rsl = carService.update(id, car);
        if (rsl) return response("Машина успешно обновлена", HttpStatus.OK);
        return response("Проверьте правильность id машины / серийный номер детали", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam int id) {
        boolean rsl = carService.delete(id);
        if (rsl) return response("Машина успешно удалена", HttpStatus.OK);
        return response("Машина с id: " + id + " не найдена", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                     @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit,
                                     @RequestParam String direction,
                                     @RequestParam String sortByField) {
        List<Car> cars = carService.findAll(offset, limit, direction, sortByField);
        if (cars.isEmpty()) return response("Отсутствуют записи о машинах в БД / некорректные параметры запроса", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam int id) {
        Car car = carService.findById(id);
        if (car == null) return response("Машина с id: " + id + " не найдена", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping("/findByVin")
    public ResponseEntity<?> findByVin(@RequestParam String vin) {
        Car car = carService.findByVin(vin);
        if (car == null) return response("Машина с vin: " + vin + " не найдена", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @PostMapping("/addDetailOnCar")
    public ResponseEntity<?> addDetailOnCar(@RequestParam int idCar, @RequestParam String serialNumberDetail) {
        boolean rsl = carService.addDetailOnCar(idCar, serialNumberDetail);
        if (rsl) return response("Деталь успешно добавлена", HttpStatus.OK);
        return response("Id машины/детали не найдены или деталь уже установлена", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteDetailOnCar")
    public ResponseEntity<?> deleteDetailOnCar(@RequestParam int idCar, @RequestParam String serialNumberDetail) {
        boolean rsl = carService.deleteDetailOnCar(idCar, serialNumberDetail);
        if (rsl) return response("Деталь успешно удалена", HttpStatus.OK);
        return response("Id машины/детали не найдены или деталь не установлена на данную машину", HttpStatus.NOT_FOUND);
    }
}
