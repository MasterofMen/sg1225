package com.toolShop.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class ToolApiValidationIT {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yy");

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void missingCheckoutDate_returns400() throws Exception {
        String json = "{\"totalRentalDays\":3,\"discount\":0}";

        mockMvc.perform(post("/api/tools/rental/LADW")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString("checkoutDate")));
    }

    @Test
    void discountOver100_returns400() throws Exception {
        String json = "{\"totalRentalDays\":3,\"checkoutDate\":\"07/02/20\",\"discount\":101}";

        mockMvc.perform(post("/api/tools/rental/LADW")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString("Discount must be 100% or less")));
    }

    @Test
    void discountNegative_returns400() throws Exception {
        String json = "{\"totalRentalDays\":3,\"checkoutDate\":\"07/02/20\",\"discount\":-1}";

        mockMvc.perform(post("/api/tools/rental/LADW")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString("Discount must be 0% or greater")));
    }

    @Test
    void rentalDaysLessThanOne_returns400() throws Exception {
        String json = "{\"totalRentalDays\":0,\"checkoutDate\":\"07/02/20\",\"discount\":0}";

        mockMvc.perform(post("/api/tools/rental/LADW")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString("Number of rental days must be 1 or greater")));
    }

    @Test
    void rentalDaysOver100_returns400() throws Exception {
        String json = "{\"totalRentalDays\":101,\"checkoutDate\":\"07/02/20\",\"discount\":0}";

        mockMvc.perform(post("/api/tools/rental/LADW")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString("Number of rental days must be less than or equal to 100")));
    }

    @Test
    void missingDiscount_returns400() throws Exception {
        String json = "{\"totalRentalDays\":3,\"checkoutDate\":\"07/02/20\"}";

        mockMvc.perform(post("/api/tools/rental/LADW")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString("Discount is required")));
    }

    @Test
    void checkoutDateBeforeToday_returns400_fromService() throws Exception {
        String past = LocalDate.now().minusDays(1).format(fmt);
        String json = String.format("{\"totalRentalDays\":3,\"checkoutDate\":\"%s\",\"discount\":0}", past);

        mockMvc.perform(post("/api/tools/rental/LADW")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", Matchers.containsString("on or after the current date")));
    }
}
