package com.example.service_car.service;

import com.example.service_car.entities.Detail;
import com.example.service_car.repository.DetailRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetailService {

    private final DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }

    public boolean create() {
        boolean rsl = false;
        Detail newDetail = new Detail();
        Detail tmpDetail = detailRepository.findBySerialNumber(newDetail.getSerialNumber()).orElse(null);
        if (tmpDetail == null) {
            detailRepository.save(newDetail);
            rsl = true;
        }
        return rsl;
    }

    public List<Detail> findAll(int offset, int limit, String direction, String field) {
        switch (direction) {
            case "asc" -> { return detailRepository.findAll(PageRequest.of(offset, limit, Sort.by(field))).getContent(); }
            case "desc" -> { return detailRepository.findAll(PageRequest.of(offset, limit, Sort.by(field).descending())).getContent(); }
            default -> { return List.of(); }
        }
    }

    public Detail findBySerialNumber(String number) {
        return detailRepository.findBySerialNumber(number).orElse(null);
    }

    public Detail findById(int id) {
        return detailRepository.findById(id).orElse(null);
    }

    public boolean delete(String number) {
        boolean rsl = false;
        Optional<Detail> tmpDetail = detailRepository.findBySerialNumber(number);
        if (tmpDetail.isPresent()) {
            if (!tmpDetail.get().getCars().isEmpty()) {
                return false;
            }
            detailRepository.delete(tmpDetail.get());
            rsl = true;
        }
        return rsl;
    }
}
