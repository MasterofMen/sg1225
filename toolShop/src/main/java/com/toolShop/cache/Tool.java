package com.toolShop.cache;

//The base tools class
public class Tool {
    private String toolCode;
    private String toolType;
    private String brand;
    private float dailyCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;

    public Tool(){}

    public Tool(String toolType, String brand, float dailyCharge, boolean weekdayCharge,
            boolean weekendCharge, boolean holidayCharge) {
        this.toolType = toolType;
        this.brand = brand;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getDailyCharge() {
        return dailyCharge;
    }

    public void setDailyCharge(float dailyCharge) {
        this.dailyCharge = dailyCharge;
    }

    public boolean isWeekdayCharge() {
        return weekdayCharge;
    }

    public void setWeekdayCharge(boolean weekdayCharge) {
        this.weekdayCharge = weekdayCharge;
    }

    public boolean isWeekEndCharge() {
        return weekendCharge;
    }

    public void setWeekEndCharge(boolean weekEndCharge) {
        this.weekendCharge = weekEndCharge;
    }

    public boolean isHolidayCharge() {
        return holidayCharge;
    }

    public void setHolidayCharge(boolean holidayCharge) {
        this.holidayCharge = holidayCharge;
    }
    
}
