package io.takima.reservation.trains.presentation;


import io.takima.reservation.trains.dto.requests.CarCreationRequest;
import io.takima.reservation.trains.dto.requests.TrainCreationRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests
 */
@Sql("/sql/trains.sql")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
@SpringBootTest
public class TestTrainApi {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class getTrainTest {

        @Test
        void getTrain_trainExists_isOk() throws Exception {
            mockMvc.perform(get("/api/trains/1")).andExpect(status().isOk());
        }

        @Test
        void getTrain_trainNotExists_isError() throws Exception {
            mockMvc.perform(get("/api/trains/2")).andExpect(status().is4xxClientError());
        }
    }

    @Nested
    class createTrainTest {

        private String toJson(TrainCreationRequest train) throws JsonProcessingException {
            var objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(train);
        }

        @Test
        void createTrain_1Car_isCreated() throws Exception {
            CarCreationRequest car1 = new CarCreationRequest("VTU", "A", 58, 0);
            TrainCreationRequest train1 = new TrainCreationRequest(List.of(car1));
            mockMvc.perform(post("/api/trains")
                    .content(toJson(train1))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }

        @Test
        void createTrain_2Cars_isCreated() throws Exception {
            CarCreationRequest car1 = new CarCreationRequest("VTU", "A", 58, 0);
            CarCreationRequest car2 = new CarCreationRequest("VTU", "A", 58, 0);
            TrainCreationRequest train1 = new TrainCreationRequest(List.of(car1, car2));
            mockMvc.perform(post("/api/trains")
                    .content(toJson(train1))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }

        @Test
        void createTrain_NoCars_isError() throws Exception {
            TrainCreationRequest train1 = new TrainCreationRequest(List.of());
            mockMvc.perform(post("/api/trains")
                    .content(toJson(train1))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        void createTrain_Null_isError() throws Exception {
            mockMvc.perform(post("/api/trains")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class getTrainsTest {

        @Test
        void getTrains_1Train_isOk() throws Exception {
            mockMvc.perform(get("/api/trains?limit=1")).andExpect(status().isOk());
        }

        @Test
        void getTrains_2Trains_isOk() throws Exception {
            mockMvc.perform(get("/api/trains?limit=2")).andExpect(status().isOk());
        }
    }
}
