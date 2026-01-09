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

    Logger log;

    //Main rental tool class
    public void rentTool(String code, int days, float discount, LocalDate checkoutDate){
        //Validate the inputed values
        if(validateTool(code, days, discount, checkoutDate)){

            RentalAgreement rentalAgreement = new RentalAgreement();
            rentalAgreement.settotalRentalDays(days);
            rentalAgreement.setCheckoutDate(checkoutDate);
            rentalAgreement.setDiscount(discount);

            Tool curTool = Constants.TOOL_MAP.get(code.toUpperCase());
            
            calculatePrice(curTool, rentalAgreement);

            generateRentalAgreement(curTool, rentalAgreement);
        }else {
            new Exception("Validation Failed for Rental");
        }
    }

    //Validates input to make sure valid values are entered
    public boolean validateTool(String code, int days, float discount, LocalDate date){
        LocalDate currentDate = LocalDate.now();

        if(!Constants.ALL_TOOLS.stream().anyMatch(x -> x.toUpperCase().equals(code))){
            new Exception("The tool code: " + code + " doesn't exist");
            return false;
        }
        if(date.isBefore(currentDate)){
            new Exception("The rental day must be on or after the current date: " + LocalDate.now());
            return false;
        }/*
        if(days < 1 || days > 100){
            new Exception("Number of rental days must be between 1 and 100");
            return false;
        }
        if(discount < 0f || discount > 100f){
            new Exception("Discount can't be less than 0% or greater than 100%");
            return false;
        }*/
        return true;
    }

    // Returns a list of all the tool codes available
    public List<String> getAllTools(){
        return Constants.ALL_TOOLS;
    }
    //Generates the rental agreement
    public void generateRentalAgreement(Tool tool, RentalAgreement rentalAgreement){
        log.info("Tool Code: " + tool.getToolCode());
        log.info("Tool Type: " + tool.getToolType());
        log.info("Tool Brand: " + tool.getBrand());
        log.info("Rental Days: " + rentalAgreement.getTotalRentalDays());
        log.info("Checkout date: " + rentalAgreement.getCheckoutDate());
        log.info("Due date: " + rentalAgreement.getDueDate());
        log.info("Daily Charge: $" + tool.getDailyCharge());
        log.info("Charge days: " + rentalAgreement.getChargeDays());
        log.info("Pre-discount price: $" + rentalAgreement.getPreDiscountPrice());
        log.info("Discount: " + rentalAgreement.getDiscount() + "%");
        log.info("Discount amount: $" + rentalAgreement.getMoneySaved());
        log.info("Final Charge: $" + rentalAgreement.getTotalPrice());
    }
    // Calculate the price of the rental
    public RentalAgreement calculatePrice(Tool tool, RentalAgreement rentalAgreement){
        float totalPrice = 0;

        LocalDate independenceDay = getIndependenceDayObservance(rentalAgreement.getCheckoutDate().getYear());
        LocalDate laborDay = getLaborDay(rentalAgreement.getCheckoutDate().getYear());

        LocalDate endDate = rentalAgreement.getCheckoutDate().plusDays(rentalAgreement.getTotalRentalDays());
        rentalAgreement.setDueDate(endDate);

        int totalRentalDays = rentalAgreement.getTotalRentalDays();

        for (LocalDate currentDate = rentalAgreement.getCheckoutDate(); !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)){
            if(currentDate == independenceDay || currentDate == laborDay && tool.isHolidayCharge()){
                totalRentalDays--;
            }else if(currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY && tool.isWeekEndCharge()){
                totalRentalDays--;
            }else if(tool.isWeekdayCharge()){
                totalRentalDays--;
            }
        }
        rentalAgreement.setChargeDays(totalRentalDays);

        totalPrice = tool.getDailyCharge() * rentalAgreement.getChargeDays();
        rentalAgreement.setPreDiscountPrice(totalPrice);

        rentalAgreement.setMoneySaved(totalPrice - (totalPrice * (rentalAgreement.getDiscount() / 100)));

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
