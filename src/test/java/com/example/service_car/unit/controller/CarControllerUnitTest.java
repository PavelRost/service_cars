package com.example.service_car.unit.controller;

import com.example.service_car.controller.ApplicationMessage;
import com.example.service_car.controller.CarController;
import com.example.service_car.entities.Car;
import com.example.service_car.entities.Detail;
import com.example.service_car.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CarController.class)
class CarControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CarService carService;

    private final int idCar1 = 1;
    private final int idCar2 = 2;
    Detail detail = new Detail(1);
    String vin = "213-542-555-789";
    String serialNumber = "123-456-789";
    Car newCarWithoutDetails = new Car(1, "213-542-555-789", new HashSet<>());
    Car newCarWithDetails = new Car(2, "111-222-333-444", new HashSet<>(List.of(detail)));


    @Test
    void whenCreateCarThenAddSuccess() throws Exception {
        when(carService.create(any())).thenReturn(true);

        mockMvc.perform(
                        post("/car/create")
                                .content(objectMapper.writeValueAsString(newCarWithoutDetails))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.CREATED.value(),
                        "Машина добавлена успешно"))));
    }

    @Test
    void whenCreateCarThenFailedCarAddEarlier() throws Exception {
        when(carService.create(any())).thenReturn(false);

        mockMvc.perform(
                        post("/car/create")
                                .content(objectMapper.writeValueAsString(newCarWithoutDetails))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.BAD_REQUEST.value(),
                        "VIN машины был добавлен ранее / ошибка в серийном номере детали"))));
    }

    @Test
    void whenUpdateCarSuccess() throws Exception {
        when(carService.update(anyInt(), any())).thenReturn(true);

        mockMvc.perform(
                        put("/car/update")
                                .param("id", String.valueOf(idCar1))
                                .content(objectMapper.writeValueAsString(newCarWithDetails))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.OK.value(),
                        "Машина успешно обновлена"))));
    }

    @Test
    void whenUpdateCarThenFailedIdCarNotFound() throws Exception {
        when(carService.update(anyInt(), any())).thenReturn(false);

        mockMvc.perform(
                        put("/car/update")
                                .param("id", String.valueOf(idCar1))
                                .content(objectMapper.writeValueAsString(newCarWithDetails))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Проверьте правильность id машины / серийный номер детали"))));
    }


    @Test
    void whenDeleteCarThenSuccess() throws Exception {
        when(carService.delete(anyInt())).thenReturn(true);

        mockMvc.perform(
                        delete("/car/delete")
                                .param("id", String.valueOf(idCar1))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.OK.value(),
                        "Машина успешно удалена"))));
    }

    @Test
    void whenDeleteCarThenFailedIdCarNotFound() throws Exception {
        when(carService.delete(anyInt())).thenReturn(false);

        mockMvc.perform(
                        delete("/car/delete")
                                .param("id", String.valueOf(idCar1))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Машина с id: " + idCar1 + " не найдена"))));
    }

    @Test
    void whenFindAllCarThenSuccess() throws Exception {
        List<Car> cars = List.of(newCarWithoutDetails, newCarWithDetails);
        when(carService.findAll(0,2, "asc", "id")).thenReturn(cars);

        mockMvc.perform(
                        get("/car/findAll")
                                .param("offset", "0")
                                .param("limit", "2")
                                .param("direction", "asc")
                                .param("sortByField", "id")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cars)));
    }

    @Test
    void whenFindAllCarThenFailedDbEmpty() throws Exception {
        List<Car> cars = List.of();
        when(carService.findAll(0,2, "asc", "id")).thenReturn(cars);

        mockMvc.perform(
                        get("/car/findAll")
                                .param("offset", "0")
                                .param("limit", "2")
                                .param("direction", "asc")
                                .param("sortByField", "id")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Отсутствуют записи о машинах в БД / некорректные параметры запроса"))));
    }

    @Test
    void whenFindByIdThenSuccess() throws Exception {
        when(carService.findById(anyInt())).thenReturn(newCarWithoutDetails);

        mockMvc.perform(
                        get("/car/findById")
                                .param("id", String.valueOf(newCarWithoutDetails.getId()))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newCarWithoutDetails)));
    }

    @Test
    void whenFindByIdThenFailedIdCarNotFound() throws Exception {
        when(carService.findById(anyInt())).thenReturn(null);

        mockMvc.perform(
                        get("/car/findById")
                                .param("id", String.valueOf(idCar2))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Машина с id: " + idCar2 + " не найдена"))));
    }

    @Test
    void whenFindByVinThenSuccess() throws Exception {
        when(carService.findByVin(anyString())).thenReturn(newCarWithoutDetails);

        mockMvc.perform(
                        get("/car/findByVin")
                                .param("vin", vin)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newCarWithoutDetails)));
    }

    @Test
    void whenFindByVinThenFailedIdCarNotFound() throws Exception {
        when(carService.findByVin(anyString())).thenReturn(null);

        mockMvc.perform(
                        get("/car/findByVin")
                                .param("vin", vin)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Машина с vin: " + vin + " не найдена"))));
    }

    @Test
    void whenAddDetailOnCarThenSuccess() throws Exception {
        when(carService.addDetailOnCar(anyInt(), anyString())).thenReturn(true);

        mockMvc.perform(
                        post("/car/addDetailOnCar")
                                .param("idCar", String.valueOf(idCar1))
                                .param("serialNumberDetail", serialNumber)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.OK.value(),
                        "Деталь успешно добавлена"))));
    }

    @Test
    void whenAddDetailOnCarThenFailedIdCarOrDetailNotFound() throws Exception {
        when(carService.addDetailOnCar(anyInt(), anyString())).thenReturn(false);

        mockMvc.perform(
                        post("/car/addDetailOnCar")
                                .param("idCar", String.valueOf(idCar2))
                                .param("serialNumberDetail", serialNumber)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Id машины/детали не найдены или деталь уже установлена"))));
    }

    @Test
    void whenDeleteDetailOnCarThenSuccess() throws Exception {
        when(carService.deleteDetailOnCar(anyInt(), anyString())).thenReturn(true);

        mockMvc.perform(
                        delete("/car/deleteDetailOnCar")
                                .param("idCar", String.valueOf(idCar1))
                                .param("serialNumberDetail", serialNumber)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.OK.value(),
                        "Деталь успешно удалена"))));
    }

    @Test
    void whenDeleteDetailOnCarThenFailedIdCarOrDetailNotFound() throws Exception {
        when(carService.deleteDetailOnCar(anyInt(), anyString())).thenReturn(false);

        mockMvc.perform(
                        delete("/car/deleteDetailOnCar")
                                .param("idCar", String.valueOf(idCar2))
                                .param("serialNumberDetail", serialNumber)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Id машины/детали не найдены или деталь не установлена на данную машину"))));

    }
}