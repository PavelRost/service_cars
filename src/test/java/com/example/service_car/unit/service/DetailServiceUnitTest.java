package com.example.service_car.unit.service;

import com.example.service_car.entities.Car;
import com.example.service_car.entities.Detail;
import com.example.service_car.repository.DetailRepository;
import com.example.service_car.service.DetailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DetailServiceUnitTest {

    @InjectMocks
    private DetailService detailService;

    @Mock
    private DetailRepository detailRepository;

    @Test
    void whenCreateDetailThenSuccess() {
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());

        boolean actual = detailService.create();

        verify(detailRepository).findBySerialNumber(anyString());
        verify(detailRepository).save(any());
        assertTrue(actual);
    }

    @Test
    void whenDeleteDetailThenSuccess() {
        Detail detail = new Detail(1, Set.of());
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(detail));

        boolean actual = detailService.delete(detail.getSerialNumber());

        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        verify(detailRepository).delete(detail);
        assertTrue(actual);
    }

    @Test
    void whenDeleteDetailThenFailedDetailRelatedCar() {
        Detail detail = new Detail(1, Set.of(new Car(1)));
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(detail));

        boolean actual = detailService.delete(detail.getSerialNumber());

        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        assertFalse(actual);
    }

    @Test
    void whenDeleteDetailThenFailedNotFoundDetail() {
        Detail detail = new Detail(1);
        when(detailRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());

        boolean actual = detailService.delete(detail.getSerialNumber());

        verify(detailRepository).findBySerialNumber(detail.getSerialNumber());
        assertFalse(actual);
    }
}