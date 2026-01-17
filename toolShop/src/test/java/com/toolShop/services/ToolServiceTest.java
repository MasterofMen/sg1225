package com.toolShop.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import com.toolShop.cache.RentalAgreement;
import com.toolShop.cache.Tool;
import com.toolShop.constant.Constants;

@ExtendWith(MockitoExtension.class)
public class ToolServiceTest {

    @InjectMocks
    private ToolService service;

    @Test
    void testGetAllTools() {
        assertEquals(Constants.ALL_TOOLS, service.getAllTools());
    }

    @Test
    void testGetPrice() {
        // Use LADW (Ladder) which charges weekdays and weekends, not holidays
        Tool tool = Constants.TOOL_MAP.get("LADW");
        tool.setToolCode("LADW");

        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(3);
        ra.setCheckoutDate(LocalDate.of(2021, 8, 1)); // 1 Aug 2021
        ra.setDiscount(10f);

        RentalAgreement result = service.calculatePrice(tool, ra);

        assertEquals(3, result.getChargeDays());
        assertEquals(4.47f, result.getPreDiscountPrice(), 0.001f);
        assertEquals(0.45f, result.getMoneySaved(), 0.001f);
        assertEquals(4.02f, result.getTotalPrice(), 0.001f);
    }

    @Test
    void testRentTool() {
        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(2);
        ra.setCheckoutDate(LocalDate.now().plusDays(1));
        ra.setDiscount(0f);

        RentalAgreement rented = service.rentTool(ra, "LADW");
        assertNotNull(rented.getDueDate());
        assertTrue(rented.getPreDiscountPrice() >= 0f);
    }

    @Test
    void testTransaction() {
        // validateTool: good and bad cases
        RentalAgreement ra = new RentalAgreement();
        ra.settotalRentalDays(1);
        ra.setCheckoutDate(LocalDate.now().plusDays(2));
        ra.setDiscount(0f);

        assertDoesNotThrow(() -> service.validateTool(ra, "LADW"));
        assertThrows(IllegalArgumentException.class, () -> service.validateTool(ra, "BADCODE"));

        // Independence day observance
        assertEquals(LocalDate.of(2020,7,3), ToolService.getIndependenceDayObservance(2020));
        assertEquals(LocalDate.of(2021,7,5), ToolService.getIndependenceDayObservance(2021));
        assertEquals(LocalDate.of(2019,7,4), ToolService.getIndependenceDayObservance(2019));

        // Labor day
        assertEquals(LocalDate.of(2021,9,6), ToolService.getLaborDay(2021));
    }
}
