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

    // Main function to process a tool rental request
    // Takes a rental agreement and tool code, validates them, calculates the total price,
    // and generates a rental agreement with all details
    public RentalAgreement rentTool(RentalAgreement rentalAgreement, String toolCode){
        // Validate inputs (will throw IllegalArgumentException on invalid input)
        validateTool(rentalAgreement, toolCode);

        Tool curTool = Constants.TOOL_MAP.get(toolCode.toUpperCase());
        curTool.setToolCode(toolCode.toUpperCase());

        rentalAgreement = calculatePrice(curTool, rentalAgreement);

        generateRentalAgreement(curTool, rentalAgreement);

        return rentalAgreement;
    }

    // Validates the rental request to ensure all required information is provided
    // Checks that: tool code exists, rental request is provided, checkout date is provided,
    // and checkout date is not in the past. Throws IllegalArgumentException if any check fails.
    public void validateTool(RentalAgreement rentalRequest, String toolCode){
        LocalDate currentDate = LocalDate.now();

        // Check if tool code is provided and exists in the available tools list (case-insensitive comparison)
        if(toolCode == null || !Constants.ALL_TOOLS.stream().anyMatch(x -> x.equals(toolCode.toUpperCase()))){
            throw new IllegalArgumentException("The tool code '" + toolCode + "' doesn't exist");
        }
        // Check if rental request object is provided
        if(rentalRequest == null){
            throw new IllegalArgumentException("Rental request is required");
        }
        // Check if checkout date is provided
        if(rentalRequest.getCheckoutDate() == null){
            throw new IllegalArgumentException("Checkout date is required");
        }
        // Check if checkout date is not in the past (must be today or later)
        if(rentalRequest.getCheckoutDate().isBefore(currentDate)){
            throw new IllegalArgumentException("The rental day must be on or after the current date: " + currentDate);
        }
    }

    // Returns a list of all the tool codes that are available for rental
    public List<String> getAllTools(){
        return Constants.ALL_TOOLS;
    }
    // Logs all details of the rental agreement to the console
    // This includes tool information, rental dates, charges, discount, and final total
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
    // Calculates the total rental price based on daily charge, rental days, and applicable discounts
    // Accounts for holidays (Independence Day and Labor Day) and weekends that may not be charged
    // depending on the tool's charging rules
    public RentalAgreement calculatePrice(Tool tool, RentalAgreement rentalAgreement){
        float totalPrice = 0f;

        // Get the dates when Independence Day and Labor Day are observed (considering weekends)
        LocalDate independenceDay = getIndependenceDayObservance(rentalAgreement.getCheckoutDate().getYear());
        LocalDate laborDay = getLaborDay(rentalAgreement.getCheckoutDate().getYear());

        // Calculate the due date by adding the rental days to the checkout date
        LocalDate endDate = rentalAgreement.getCheckoutDate().plusDays(rentalAgreement.getTotalRentalDays());
        rentalAgreement.setDueDate(endDate);

        // Start with the total rental days and subtract days that shouldn't be charged
        int totalRentalDays = rentalAgreement.getTotalRentalDays();

        // Loop through each day of the rental period and determine if it should be charged
        for (LocalDate currentDate = rentalAgreement.getCheckoutDate(); !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)){
            // If it's a holiday and the tool doesn't charge for holidays, skip the charge
            if(currentDate == independenceDay || currentDate == laborDay && !tool.isHolidayCharge()){
                totalRentalDays--;
            }
            // If it's a weekend and the tool doesn't charge for weekends, skip the charge
            else if(currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY && !tool.isWeekEndCharge()){
                totalRentalDays--;
            }
            // If it's a weekday and the tool doesn't charge for weekdays, skip the charge
            else if(!tool.isWeekdayCharge()){
                totalRentalDays--;
            }
        }
        // Set the number of days that will actually be charged
        rentalAgreement.setChargeDays(totalRentalDays);

        // Calculate the price before discount by multiplying daily charge by chargeable days
        // Round to 2 decimal places for currency
        totalPrice = Math.round(tool.getDailyCharge() * rentalAgreement.getChargeDays() * 100.0f) / 100.0f;
        rentalAgreement.setPreDiscountPrice(totalPrice);

        // Calculate the discount amount by applying the discount percentage to the pre-discount price
        // Round to 2 decimal places for currency
        rentalAgreement.setMoneySaved(Math.round(totalPrice * (rentalAgreement.getDiscount() / 100) *100.0f) / 100.0f);

        // Calculate the final price by subtracting the discount amount from the pre-discount price
        rentalAgreement.setTotalPrice(totalPrice - rentalAgreement.getMoneySaved());

        return rentalAgreement;
    }

    // Returns the date when Independence Day (July 4th) is observed in the given year
    // If July 4th falls on a Saturday, it's observed on Friday (July 3rd)
    // If July 4th falls on a Sunday, it's observed on Monday (July 5th)
    // If July 4th falls on a weekday, it's observed on July 4th itself
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

    // Returns the date of Labor Day in the given year
    // Labor Day is always the first Monday in September
    public static LocalDate getLaborDay(int year) {
        // Find the first Monday in September of the given year
        return LocalDate.of(year, 9, 1)
                        .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

}
