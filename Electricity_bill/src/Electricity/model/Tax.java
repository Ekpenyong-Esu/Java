package Electricity.model;

/**
 * Model class representing tax rates and billing constants in the Electricity Billing System.
 */
public class Tax {
    private int id;
    private double costPerUnit;
    private double meterRent;
    private double serviceCharge;
    private double serviceTax;
    private double climateChangeLevy;
    private double fixedTax;

    /**
     * Default constructor
     */
    public Tax() {
    }

    /**
     * Parameterized constructor to create a tax object with all rates
     */
    public Tax(double costPerUnit, double meterRent, double serviceCharge,
               double serviceTax, double climateChangeLevy, double fixedTax) {
        this.costPerUnit = costPerUnit;
        this.meterRent = meterRent;
        this.serviceCharge = serviceCharge;
        this.serviceTax = serviceTax;
        this.climateChangeLevy = climateChangeLevy;
        this.fixedTax = fixedTax;
    }

    /**
     * Parameterized constructor with ID to create a tax object with all rates
     */
    public Tax(int id, double costPerUnit, double meterRent, double serviceCharge,
               double serviceTax, double climateChangeLevy, double fixedTax) {
        this.id = id;
        this.costPerUnit = costPerUnit;
        this.meterRent = meterRent;
        this.serviceCharge = serviceCharge;
        this.serviceTax = serviceTax;
        this.climateChangeLevy = climateChangeLevy;
        this.fixedTax = fixedTax;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public double getMeterRent() {
        return meterRent;
    }

    public void setMeterRent(double meterRent) {
        this.meterRent = meterRent;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public double getClimateChangeLevy() {
        return climateChangeLevy;
    }

    public void setClimateChangeLevy(double climateChangeLevy) {
        this.climateChangeLevy = climateChangeLevy;
    }

    public double getFixedTax() {
        return fixedTax;
    }

    public void setFixedTax(double fixedTax) {
        this.fixedTax = fixedTax;
    }

    @Override
    public String toString() {
        return "Tax{" +
                "id=" + id +
                ", costPerUnit=" + costPerUnit +
                ", meterRent=" + meterRent +
                ", serviceCharge=" + serviceCharge +
                ", serviceTax=" + serviceTax +
                ", climateChangeLevy=" + climateChangeLevy +
                ", fixedTax=" + fixedTax +
                '}';
    }
}