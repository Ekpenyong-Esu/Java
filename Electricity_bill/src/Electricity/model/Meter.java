package Electricity.model;

/**
 * Model class representing a meter in the Electricity Billing System.
 */
public class Meter {
    private String meterNumber;
    private String meterLocation;
    private String meterType;
    private String phaseCode;
    private String billType;
    private int days;

    /**
     * Default constructor
     */
    public Meter() {
    }

    /**
     * Parameterized constructor to create a meter with all details
     */
    public Meter(String meterNumber, String meterLocation, String meterType,
                 String phaseCode, String billType, int days) {
        this.meterNumber = meterNumber;
        this.meterLocation = meterLocation;
        this.meterType = meterType;
        this.phaseCode = phaseCode;
        this.billType = billType;
        this.days = days;
    }

    // Getters and Setters

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getMeterLocation() {
        return meterLocation;
    }

    public void setMeterLocation(String meterLocation) {
        this.meterLocation = meterLocation;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getPhaseCode() {
        return phaseCode;
    }

    public void setPhaseCode(String phaseCode) {
        this.phaseCode = phaseCode;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "Meter{" +
                "meterNumber='" + meterNumber + '\'' +
                ", meterLocation='" + meterLocation + '\'' +
                ", meterType='" + meterType + '\'' +
                ", phaseCode='" + phaseCode + '\'' +
                ", billType='" + billType + '\'' +
                ", days=" + days +
                '}';
    }
}