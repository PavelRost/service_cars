package com.example.service_car.controller;


import com.example.service_car.entities.Detail;
import com.example.service_car.service.DetailService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.service_car.controller.ApplicationMessage.response;

@RestController
@RequestMapping("/detail")
public class DetailController {

    private final DetailService detailService;

    public DetailController(DetailService detailService) {
        this.detailService = detailService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create() {
        boolean rsl = detailService.create();
        if (rsl) return response("Новая деталь успешно создана", HttpStatus.CREATED);
        return response("Деталь была добавлена ранее, повторите попытку", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
                                     @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) int limit,
                                     @RequestParam String direction,
                                     @RequestParam String sortByField) {
        List<Detail> details = detailService.findAll(offset, limit, direction, sortByField);
        if (details.isEmpty()) return response("Отсутствуют записи о деталях в БД / некорректные параметры запроса", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @GetMapping("/findBySerialNumber")
    public ResponseEntity<?> findBySerialNumber(@RequestParam String serialNumber) {
        Detail detail = detailService.findBySerialNumber(serialNumber);
        if (detail == null) return response("Деталь с серийный номером: " + serialNumber + " не найдена", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(detail, HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam int id) {
        Detail detail = detailService.findById(id);
        if (detail == null) return response("Деталь с id: " + id + " не найдена", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(detail, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String serialNumber) {
        boolean rsl = detailService.delete(serialNumber);
        if (rsl) return response("Деталь успешно удалена", HttpStatus.OK);
        return response("Серийный номер не найден или деталь привязана к машине и не может быть удалена", HttpStatus.NOT_FOUND);
    }
}
