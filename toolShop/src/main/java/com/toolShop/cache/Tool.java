package com.toolShop.cache;

// Model class that represents a tool available for rental
// Contains information about the tool type, brand, daily rental charge,
// and which types of days (weekday/weekend/holidays) are charged
public class Tool {
    private String toolCode;              // Unique identifier for the tool
    private String toolType;               // Type of tool (e.g., Chainsaw, Jackhammer)
    private String brand;                  // Brand/manufacturer of the tool
    private float dailyCharge;             // Daily rental charge for the tool
    private boolean weekdayCharge;         // Whether weekdays are charged
    private boolean weekendCharge;         // Whether weekends are charged
    private boolean holidayCharge;         // Whether holidays are charged

    public Tool(){}

    // Constructor that initializes a tool with all its rental parameters
    public Tool(String toolType, String brand, float dailyCharge, boolean weekdayCharge,
            boolean weekendCharge, boolean holidayCharge) {
        this.toolType = toolType;
        this.brand = brand;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    // Getter and Setter for tool code
    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    // Getter and Setter for tool type
    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    // Getter and Setter for brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Getter and Setter for daily charge
    public float getDailyCharge() {
        return dailyCharge;
    }

    public void setDailyCharge(float dailyCharge) {
        this.dailyCharge = dailyCharge;
    }

    // Getter and Setter for weekday charge flag
    public boolean isWeekdayCharge() {
        return weekdayCharge;
    }

    public void setWeekdayCharge(boolean weekdayCharge) {
        this.weekdayCharge = weekdayCharge;
    }

    // Getter and Setter for weekend charge flag
    public boolean isWeekEndCharge() {
        return weekendCharge;
    }

    public void setWeekEndCharge(boolean weekEndCharge) {
        this.weekendCharge = weekEndCharge;
    }

    // Getter and Setter for holiday charge flag
    public boolean isHolidayCharge() {
        return holidayCharge;
    }

    public void setHolidayCharge(boolean holidayCharge) {
        this.holidayCharge = holidayCharge;
    }
    
}
