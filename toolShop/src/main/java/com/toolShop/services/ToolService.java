package com.toolShop.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.toolShop.cache.RentalAgreement;
import com.toolShop.cache.Tool;
import com.toolShop.constant.Constants;

@Service
public class ToolService {

    private static final Logger LOGGER = Logger.getLogger(ToolService.class.getName());

    //Main rental tool class
    public RentalAgreement rentTool(RentalAgreement rentalAgreement, String toolCode){
        // Validate inputs (will throw IllegalArgumentException on invalid input)
        validateTool(rentalAgreement, toolCode);

        Tool curTool = Constants.TOOL_MAP.get(toolCode.toUpperCase());
        curTool.setToolCode(toolCode.toUpperCase());

        rentalAgreement = calculatePrice(curTool, rentalAgreement);

        generateRentalAgreement(curTool, rentalAgreement);

        return rentalAgreement;
    }

    //Validates input to make sure valid values are entered. Throws IllegalArgumentException on error.
    public void validateTool(RentalAgreement rentalRequest, String toolCode){
        LocalDate currentDate = LocalDate.now();

        if(toolCode == null || !Constants.ALL_TOOLS.stream().anyMatch(x -> x.equals(toolCode.toUpperCase()))){
            throw new IllegalArgumentException("The tool code '" + toolCode + "' doesn't exist");
        }
        if(rentalRequest == null){
            throw new IllegalArgumentException("Rental request is required");
        }
        if(rentalRequest.getCheckoutDate() == null){
            throw new IllegalArgumentException("Checkout date is required");
        }
        if(rentalRequest.getCheckoutDate().isBefore(currentDate)){
            throw new IllegalArgumentException("The rental day must be on or after the current date: " + currentDate);
        }
    }

    // Returns a list of all the tool codes available
    public List<String> getAllTools(){
        return Constants.ALL_TOOLS;
    }
    //Generates the rental agreement
    public void generateRentalAgreement(Tool tool, RentalAgreement rentalAgreement){
        LOGGER.info("Tool Code: " + tool.getToolCode());
        LOGGER.info("Tool Type: " + tool.getToolType());
        LOGGER.info("Tool Brand: " + tool.getBrand());
        LOGGER.info("Rental Days: " + rentalAgreement.getTotalRentalDays());
        LOGGER.info("Checkout date: " + rentalAgreement.getCheckoutDate());
        LOGGER.info("Due date: " + rentalAgreement.getDueDate());
        LOGGER.info("Daily Charge: $" + tool.getDailyCharge());
        LOGGER.info("Charge days: " + rentalAgreement.getChargeDays());
        LOGGER.info("Pre-discount price: $" + rentalAgreement.getPreDiscountPrice());
        LOGGER.info("Discount: " + rentalAgreement.getDiscount() + "%");
        LOGGER.info("Discount amount: $" + rentalAgreement.getMoneySaved());
        LOGGER.info("Final Charge: $" + rentalAgreement.getTotalPrice());
    }
    // Calculate the price of the rental
    public RentalAgreement calculatePrice(Tool tool, RentalAgreement rentalAgreement){
        float totalPrice = 0f;

        LocalDate independenceDay = getIndependenceDayObservance(rentalAgreement.getCheckoutDate().getYear());
        LocalDate laborDay = getLaborDay(rentalAgreement.getCheckoutDate().getYear());

        LocalDate endDate = rentalAgreement.getCheckoutDate().plusDays(rentalAgreement.getTotalRentalDays());
        rentalAgreement.setDueDate(endDate);

        int totalRentalDays = rentalAgreement.getTotalRentalDays();

        for (LocalDate currentDate = rentalAgreement.getCheckoutDate(); !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)){
            if(currentDate == independenceDay || currentDate == laborDay && !tool.isHolidayCharge()){
                totalRentalDays--;
            }else if(currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY && !tool.isWeekEndCharge()){
                totalRentalDays--;
            }else if(!tool.isWeekdayCharge()){
                totalRentalDays--;
            }
        }
        rentalAgreement.setChargeDays(totalRentalDays);

        totalPrice = Math.round(tool.getDailyCharge() * rentalAgreement.getChargeDays() * 100.0f) / 100.0f;
        rentalAgreement.setPreDiscountPrice(totalPrice);

        rentalAgreement.setMoneySaved(Math.round(totalPrice * (rentalAgreement.getDiscount() / 100) *100.0f) / 100.0f);

        rentalAgreement.setTotalPrice(totalPrice - rentalAgreement.getMoneySaved());

        return rentalAgreement;
    }

    public static LocalDate getIndependenceDayObservance(int year) {
        LocalDate julyFourth = LocalDate.of(year, 7, 4);
        DayOfWeek dayOfWeek = julyFourth.getDayOfWeek();

        if (dayOfWeek == DayOfWeek.SATURDAY) {
            // Observed on Friday, July 3rd
            return julyFourth.minusDays(1);
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            // Observed on Monday, July 5th
            return julyFourth.plusDays(1);
        } else {
            // Observed on July 4th itself (Monday-Friday)
            return julyFourth;
        }
    }

    public static LocalDate getLaborDay(int year) {
        // Find the first Monday in September of the given year
        return LocalDate.of(year, 9, 1)
                        .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

}
