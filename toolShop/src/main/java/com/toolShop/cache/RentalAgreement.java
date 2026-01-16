package com.toolShop.cache;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RentalAgreement {
    @NotNull(message = "Number of rentals days is required")
    @Min(value = 1, message="Number of rental days must be 1 or greater")
    @Max(value = 100, message = "Number of rental days must be less than or equal to 100")
    private Integer totalRentalDays;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    private LocalDate checkoutDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    private LocalDate dueDate;
    private int chargeDays;
    @NotNull(message = "Discount is required")
    @Min(value = 0, message = "Discount must be 0% or greater")
    @Max(value = 100, message = "Discount must be 100% or less")
    private Float discount;
    private float preDiscountPrice;
    private float moneySaved;
    private float totalPrice;

    public RentalAgreement(){}

    public Integer getTotalRentalDays() {
        return totalRentalDays;
    }

    public void settotalRentalDays(Integer totalRentalDays) {
        this.totalRentalDays = totalRentalDays;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public void setChargeDays(int chargeDays) {
        this.chargeDays = chargeDays;
    }

    public float getPreDiscountPrice() {
        return preDiscountPrice;
    }

    public void setPreDiscountPrice(float preDiscountPrice) {
        this.preDiscountPrice = preDiscountPrice;
    }

    public float getMoneySaved() {
        return moneySaved;
    }

    public void setMoneySaved(float moneySaved) {
        this.moneySaved = moneySaved;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    
}
