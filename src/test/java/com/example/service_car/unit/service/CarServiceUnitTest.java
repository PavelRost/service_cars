package com.example.service_car.unit.service;

import com.example.service_car.entities.Car;
import com.example.service_car.entities.Detail;
import com.example.service_car.repository.CarRepository;
import com.example.service_car.repository.DetailRepository;
import com.example.service_car.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceUnitTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private DetailRepository detailRepository;

    private final int idCar1 = 1;
    private final int idCar2 = 2;
    Detail detail = new Detail(1);
    Car newCarWithoutDetails = new Car(1, "213-542-555-789", new HashSet<>());
    Car newCarWithDetails = new Car(2, "111-222-333-444", new HashSet<>(List.of(detail)));

    @Test
    void whenCreateCarWithoutDetailsThenSuccess() {
        when(carRepository.findByVin(anyString())).thenReturn(Optional.empty());

        boolean actual = carService.create(newCarWithoutDetails);

        verify(carRepository).findByVin(newCarWithoutDetails.getVin());
        verify(carRepository).save(newCarWithoutDetails);
        assertTrue(actual);
    }

    @Test
    void whenCreateCarWithDetailsThenSuccess() {
        when(carRepository.findByVin(anyString())).thenReturn(Optional.empty());
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(detail));

        boolean actual = carService.create(newCarWithDetails);

        verify(carRepository).findByVin(newCarWithDetails.getVin());
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        verify(carRepository).save(newCarWithDetails);
        assertTrue(actual);
    }

    @Test
    void whenCreateCarThenFailedCarAddEarlier() {
        when(carRepository.findByVin(anyString())).thenReturn(Optional.of(newCarWithoutDetails));

        boolean actual = carService.create(newCarWithoutDetails);

        verify(carRepository).findByVin(newCarWithoutDetails.getVin());
        assertFalse(actual);
    }

    @Test
    void whenCreateCarThenFailedNumberDetailNotFound() {
        when(carRepository.findByVin(anyString())).thenReturn(Optional.empty());
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());

        boolean actual = carService.create(newCarWithDetails);

        verify(carRepository).findByVin(newCarWithDetails.getVin());
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        assertFalse(actual);
    }

    @Test
    void whenUpdateCarWithoutDetailsThenSuccess() {
        when(carRepository.existsById(anyInt())).thenReturn(true);

        boolean actual = carService.update(idCar2, newCarWithoutDetails);

        verify(carRepository).existsById(idCar2);
        verify(carRepository).save(newCarWithoutDetails);
        assertTrue(actual);
    }

    @Test
    void whenUpdateCarWithDetailsThenSuccess() {
        when(carRepository.existsById(anyInt())).thenReturn(true);
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(detail));

        boolean actual = carService.update(idCar1, newCarWithDetails);

        verify(carRepository).existsById(idCar1);
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        verify(carRepository).save(newCarWithDetails);
        assertTrue(actual);
    }

    @Test
    void whenUpdateCarThenFailedCarNotExist() {
        when(carRepository.existsById(anyInt())).thenReturn(false);

        boolean actual = carService.update(idCar1, newCarWithDetails);

        verify(carRepository).existsById(idCar1);
        assertFalse(actual);
    }

    @Test
    void whenUpdateCarThenFailedDetailNotFound() {
        when(carRepository.existsById(anyInt())).thenReturn(true);
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());

        boolean actual = carService.update(idCar1, newCarWithDetails);

        verify(carRepository).existsById(idCar1);
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        assertFalse(actual);
    }

    @Test
    void whenDeleteCarThenSuccess() {
        when(carRepository.existsById(anyInt())).thenReturn(true);

        boolean actual = carService.delete(idCar1);

        verify(carRepository).existsById(idCar1);
        verify(carRepository).deleteById(idCar1);
        assertTrue(actual);
    }

    @Test
    void whenDeleteCarThenFailedIdCarNotFound() {
        when(carRepository.existsById(anyInt())).thenReturn(false);

        boolean actual = carService.delete(idCar1);

        verify(carRepository).existsById(idCar1);
        assertFalse(actual);
    }

    @Test
    void whenFindAllSortAscThenSuccess() {
        int offset = 0;
        int limit = 3;
        String direction = "asc";
        String field = "id";
        List<Car> cars = List.of(newCarWithoutDetails, newCarWithDetails);
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(field));
        Mockito.when(carRepository.findAll(ArgumentMatchers.any())).thenReturn(
                new SliceImpl<>(cars, pageRequest,false)
        );

        List<Car> actual = carService.findAll(offset, limit, direction, field);

        Mockito.verify(carRepository).findAll(pageRequest);
        assertEquals(cars, actual);
    }

    @Test
    void whenFindAllSortDescThenSuccess() {
        int offset = 0;
        int limit = 3;
        String direction = "desc";
        String field = "id";
        List<Car> cars = List.of(newCarWithDetails, newCarWithoutDetails);
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(field).descending());
        Mockito.when(carRepository.findAll(ArgumentMatchers.any())).thenReturn(
                new SliceImpl<>(cars, pageRequest,false)
        );

        List<Car> actual = carService.findAll(offset, limit, direction, field);

        Mockito.verify(carRepository).findAll(pageRequest);
        assertEquals(cars, actual);
    }

    @Test
    void whenFindAllThenFailedCarNotFoundIncorrectParam() {
        int offset = 0;
        int limit = 3;
        String direction = "";
        String field = "id";
        List<Car> cars  = List.of();

        List<Car> actual = carService.findAll(offset, limit, direction, field);

        assertEquals(cars, actual);
    }

    @Test
    void whenAddDetailOnCarThenSuccess() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(newCarWithoutDetails));
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(detail));

        boolean actual = carService.addDetailOnCar(newCarWithoutDetails.getId(), detail.getSerialNumber());

        verify(carRepository).findById(newCarWithoutDetails.getId());
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        verify(carRepository).save(newCarWithoutDetails);
        assertTrue(actual);
    }

    @Test
    void whenAddDetailOnCarThenFailedIdCarNotFound() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(detail));

        boolean actual = carService.addDetailOnCar(newCarWithoutDetails.getId(), detail.getSerialNumber());

        verify(carRepository).findById(newCarWithoutDetails.getId());
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        assertFalse(actual);
    }

    @Test
    void whenAddDetailOnCarThenFailedNumberDetailNotFound() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(newCarWithoutDetails));
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());

        boolean actual = carService.addDetailOnCar(newCarWithoutDetails.getId(), detail.getSerialNumber());

        verify(carRepository).findById(newCarWithoutDetails.getId());
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        assertFalse(actual);
    }

    @Test
    void whenDeleteDetailOnCarThenSuccess() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(newCarWithDetails));
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(detail));

        boolean actual = carService.deleteDetailOnCar(newCarWithDetails.getId(), detail.getSerialNumber());

        verify(carRepository).findById(newCarWithDetails.getId());
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        verify(carRepository).save(newCarWithDetails);
        assertTrue(actual);
    }

    @Test
    void whenDeleteDetailOnCarThenFailedIdCarNotFound() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(detail));

        boolean actual = carService.deleteDetailOnCar(newCarWithoutDetails.getId(), detail.getSerialNumber());

        verify(carRepository).findById(newCarWithoutDetails.getId());
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        assertFalse(actual);
    }

    @Test
    void whenDeleteDetailOnCarThenFailedNumberDetailNotFound() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(newCarWithoutDetails));
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());

        boolean actual = carService.deleteDetailOnCar(newCarWithoutDetails.getId(), detail.getSerialNumber());

        verify(carRepository).findById(newCarWithoutDetails.getId());
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        assertFalse(actual);
    }

    @Test
    void whenDeleteDetailOnCarThenFailedDetailNotSetupCar() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(newCarWithoutDetails));
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(detail));

        boolean actual = carService.deleteDetailOnCar(newCarWithoutDetails.getId(), detail.getSerialNumber());

        verify(carRepository).findById(newCarWithoutDetails.getId());
        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        assertFalse(actual);
    }
}