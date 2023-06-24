package com.example.service_car.unit.controller;

import com.example.service_car.controller.ApplicationMessage;
import com.example.service_car.controller.DetailController;
import com.example.service_car.entities.Detail;
import com.example.service_car.service.DetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DetailController.class)
class DetailControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DetailService detailService;

    Detail detail1 = new Detail(1);
    Detail detail2 = new Detail(2);

    @Test
    void whenCreateDetailThenAddSuccess() throws Exception {
        when(detailService.create()).thenReturn(true);

        mockMvc.perform(
                        post("/detail/create")
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.CREATED.value(),
                        "Новая деталь успешно создана"))));
    }

    @Test
    void whenCreateDetailThenFailedDetailAddEarlier() throws Exception {
        when(detailService.create()).thenReturn(false);

        mockMvc.perform(
                        post("/detail/create")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.BAD_REQUEST.value(),
                        "Деталь была добавлена ранее, повторите попытку"))));
    }

    @Test
    void whenFindAllDetailThenSuccess() throws Exception {
        List<Detail> details = List.of(detail1, detail2);
        when(detailService.findAll(0,2, "asc", "id")).thenReturn(details);

        mockMvc.perform(
                        get("/detail/findAll")
                                .param("offset", "0")
                                .param("limit", "2")
                                .param("direction", "asc")
                                .param("sortByField", "id")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(details)));
    }

    @Test
    void whenFindAllDetailThenFailedDbEmpty() throws Exception {
        List<Detail> details = List.of();
        when(detailService.findAll(0,2, "asc", "id")).thenReturn(details);

        mockMvc.perform(
                        get("/detail/findAll")
                                .param("offset", "0")
                                .param("limit", "2")
                                .param("direction", "asc")
                                .param("sortByField", "id")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Отсутствуют записи о деталях в БД / некорректные параметры запроса"))));
    }

    @Test
    void whenFindBySerialNumberThenSuccess() throws Exception {
        when(detailService.findBySerialNumber(anyString())).thenReturn(detail1);

        mockMvc.perform(
                        get("/detail/findBySerialNumber")
                                .param("serialNumber", detail1.getSerialNumber())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(detail1)));
    }

    @Test
    void whenFindBySerialNumberThenFailedSerialNumberNotFound() throws Exception {
        when(detailService.findBySerialNumber(anyString())).thenReturn(null);

        mockMvc.perform(
                        get("/detail/findBySerialNumber")
                                .param("serialNumber", detail2.getSerialNumber())
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Деталь с серийный номером: " + detail2.getSerialNumber() + " не найдена"))));
    }

    @Test
    void whenFindByIdThenSuccess() throws Exception {
        when(detailService.findById(anyInt())).thenReturn(detail1);

        mockMvc.perform(
                        get("/detail/findById")
                                .param("id", String.valueOf(detail1.getId()))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(detail1)));
    }

    @Test
    void whenFindByIdThenFailedIdNotFound() throws Exception {
        when(detailService.findById(anyInt())).thenReturn(null);

        mockMvc.perform(
                        get("/detail/findById")
                                .param("id", String.valueOf(detail2.getId()))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Деталь с id: " + detail2.getId() + " не найдена"))));
    }

    @Test
    void whenDeleteDetailThenSuccess() throws Exception {
        when(detailService.delete(anyString())).thenReturn(true);

        mockMvc.perform(
                        delete("/detail/delete")
                                .param("serialNumber", detail1.getSerialNumber())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.OK.value(),
                        "Деталь успешно удалена"))));
    }

    @Test
    void whenDeleteDetailThenFailedIdNotFound() throws Exception {
        when(detailService.delete(anyString())).thenReturn(false);

        mockMvc.perform(
                        delete("/detail/delete")
                                .param("serialNumber", detail2.getSerialNumber())
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApplicationMessage(HttpStatus.NOT_FOUND.value(),
                        "Серийный номер не найден или деталь привязана к машине и не может быть удалена"))));
    }

}