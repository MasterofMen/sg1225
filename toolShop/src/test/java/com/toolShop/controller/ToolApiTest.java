package com.toolShop.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.toolShop.cache.RentalAgreement;
import com.toolShop.constant.Constants;
import com.toolShop.services.ToolService;

@ExtendWith(MockitoExtension.class)
public class ToolApiTest {

    @Mock
    private ToolService toolService;

    @InjectMocks
    private ToolApi toolApi;


    @Test
    void test1_invalidDiscountReturnsBadRequest() {
        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(5);
        ra.setCheckoutDate(LocalDate.of(2015,9,3));
        ra.setDiscount(101f);

        when(toolService.rentTool(any(), eq("JAKR"))).thenThrow(new IllegalArgumentException("Discount must be between 0 and 100"));

        ResponseEntity<?> resp = toolApi.rentTool(ra, "JAKR");
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody() instanceof java.util.Map);
        @SuppressWarnings("unchecked")
        java.util.Map<String,String> map = (java.util.Map<String,String>) resp.getBody();
        assertEquals("Discount must be between 0 and 100", map.get("error"));
    }

    @Test
    void test2_ladw_7_2_20_3days_10pct() {
        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(3);
        ra.setCheckoutDate(LocalDate.of(2020,7,2));
        ra.setDiscount(10f);

        RentalAgreement returned = new RentalAgreement();
        returned.settotalRentalDays(3);
        returned.setCheckoutDate(ra.getCheckoutDate());
        returned.setDueDate(ra.getCheckoutDate().plusDays(3));
        returned.setPreDiscountPrice(4.47f);

        when(toolService.rentTool(any(), eq("LADW"))).thenReturn(returned);

        ResponseEntity<?> resp = toolApi.rentTool(ra, "LADW");
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertTrue(resp.getBody() instanceof RentalAgreement);
    }

    @Test
    void test3_chns_7_2_15_5days_25pct() {
        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(5);
        ra.setCheckoutDate(LocalDate.of(2015,7,2));
        ra.setDiscount(25f);

        RentalAgreement returned = new RentalAgreement();
        returned.settotalRentalDays(5);
        returned.setCheckoutDate(ra.getCheckoutDate());
        returned.setDueDate(ra.getCheckoutDate().plusDays(5));
        returned.setPreDiscountPrice(9.95f);

        when(toolService.rentTool(any(), eq("CHNS"))).thenReturn(returned);

        ResponseEntity<?> resp = toolApi.rentTool(ra, "CHNS");
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertTrue(resp.getBody() instanceof RentalAgreement);
    }

    @Test
    void test4_jakd_9_3_15_defaults() {
        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(6);
        ra.setCheckoutDate(LocalDate.of(2015,9,3));
        ra.setDiscount(0f);

        RentalAgreement returned = new RentalAgreement();
        returned.settotalRentalDays(6);
        returned.setCheckoutDate(ra.getCheckoutDate());
        returned.setDueDate(ra.getCheckoutDate().plusDays(6));
        returned.setPreDiscountPrice(17.94f);

        when(toolService.rentTool(any(), eq("JAKD"))).thenReturn(returned);

        ResponseEntity<?> resp = toolApi.rentTool(ra, "JAKD");
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertTrue(resp.getBody() instanceof RentalAgreement);
    }

    @Test
    void test5_jakr_7_2_15_9days_0pct() {
        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(9);
        ra.setCheckoutDate(LocalDate.of(2015,7,2));
        ra.setDiscount(0f);

        RentalAgreement returned = new RentalAgreement();
        returned.settotalRentalDays(9);
        returned.setCheckoutDate(ra.getCheckoutDate());
        returned.setDueDate(ra.getCheckoutDate().plusDays(9));
        returned.setPreDiscountPrice(26.91f);

        when(toolService.rentTool(any(), eq("JAKR"))).thenReturn(returned);

        ResponseEntity<?> resp = toolApi.rentTool(ra, "JAKR");
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertTrue(resp.getBody() instanceof RentalAgreement);
    }

    @Test
    void test6_jakr_7_2_20_6days_50pct() {
        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(6);
        ra.setCheckoutDate(LocalDate.of(2020,7,2));
        ra.setDiscount(50f);

        RentalAgreement returned = new RentalAgreement();
        returned.settotalRentalDays(6);
        returned.setCheckoutDate(ra.getCheckoutDate());
        returned.setDueDate(ra.getCheckoutDate().plusDays(6));
        returned.setPreDiscountPrice(17.94f);

        when(toolService.rentTool(any(), eq("JAKR"))).thenReturn(returned);

        ResponseEntity<?> resp = toolApi.rentTool(ra, "JAKR");
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertTrue(resp.getBody() instanceof RentalAgreement);
    }

    @Test
    void testRentTool() {
        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(2);
        ra.setCheckoutDate(LocalDate.now().plusDays(1));
        ra.setDiscount(0f);

        RentalAgreement returned = new RentalAgreement();
        returned.settotalRentalDays(2);
        returned.setCheckoutDate(ra.getCheckoutDate());
        returned.setDueDate(ra.getCheckoutDate().plusDays(2));
        returned.setPreDiscountPrice(1.49f);

        when(toolService.rentTool(ra, "LADW")).thenReturn(returned);

        ResponseEntity<?> resp = toolApi.rentTool(ra, "LADW");
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody() instanceof RentalAgreement);
        RentalAgreement body = (RentalAgreement) resp.getBody();
        assertEquals(returned.getPreDiscountPrice(), body.getPreDiscountPrice());
    }

    @Test
    void testRentToolHandlesException() {
        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(1);
        ra.setCheckoutDate(LocalDate.now().plusDays(1));
        ra.setDiscount(0f);

        when(toolService.rentTool(ra, "BAD")).thenThrow(new RuntimeException("bad"));

        ResponseEntity<?> resp = toolApi.rentTool(ra, "BAD");
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody() instanceof java.util.Map);
        @SuppressWarnings("unchecked")
        java.util.Map<String,String> map = (java.util.Map<String,String>) resp.getBody();
        assertEquals("Invalid request", map.get("error"));
    }
}
