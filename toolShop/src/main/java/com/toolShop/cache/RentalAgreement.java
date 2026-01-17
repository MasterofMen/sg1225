package com.toolShop.cache;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// Model class representing a rental agreement for a tool
// Contains rental period, dates, charges, and discount information
public class RentalAgreement {
    // Total number of days the tool is being rented (must be 1-100)
    @NotNull(message = "Number of rentals days is required")
    @Min(value = 1, message="Number of rental days must be 1 or greater")
    @Max(value = 100, message = "Number of rental days must be less than or equal to 100")
    private Integer totalRentalDays;
    
    // The date when the tool is being checked out (date format: MM/dd/yy)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    private LocalDate checkoutDate;
    
    // The date when the tool must be returned (calculated based on rental days)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    private LocalDate dueDate;
    
    // Number of days that will actually be charged (may be less due to holidays/weekends)
    private int chargeDays;
    
    // Discount percentage applied to the rental (must be 0-100%)
    @NotNull(message = "Discount is required")
    @Min(value = 0, message = "Discount must be 0% or greater")
    @Max(value = 100, message = "Discount must be 100% or less")
    private Float discount;
    
    // Total price before discount is applied
    private float preDiscountPrice;
    
    // Total money saved due to the discount
    private float moneySaved;
    
    // Final total price after discount is applied
    private float totalPrice;

    public RentalAgreement(){}

    // Getter and Setter for total rental days
    public Integer getTotalRentalDays() {
        return totalRentalDays;
    }

    public void settotalRentalDays(Integer totalRentalDays) {
        this.totalRentalDays = totalRentalDays;
    }

    // Getter and Setter for checkout date
    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    // Getter and Setter for due date
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Getter and Setter for charge days
    public int getChargeDays() {
        return chargeDays;
    }

    public void setChargeDays(int chargeDays) {
        this.chargeDays = chargeDays;
    }

    // Getter and Setter for pre-discount price
    public float getPreDiscountPrice() {
        return preDiscountPrice;
    }

    public void setPreDiscountPrice(float preDiscountPrice) {
        this.preDiscountPrice = preDiscountPrice;
    }

    // Getter and Setter for money saved
    public float getMoneySaved() {
        return moneySaved;
    }

    public void setMoneySaved(float moneySaved) {
        this.moneySaved = moneySaved;
    }

    // Getter and Setter for total price
    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Getter and Setter for discount percentage
    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    
}
