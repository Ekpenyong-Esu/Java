package Electricity.model;

/*
 * Bill class represents an electricity bill with various attributes.
 * It includes methods to calculate the total amount and display bill details.
 */
public class Bill {
    private String meter;
    private String month;
    private int units;
    private double totalBill;
    private String status;


    /*
     * Default constructor initializes the bill with default values.
     *
     */
    public Bill() {

    }

    /*
     * Parameterized constructor initializes the bill with provided values.
     *
     * @param meter The meter number associated with the bill.
     * @param month The month for which the bill is generated.
     * @param units The number of units consumed.
     * @param totalBill The total amount of the bill.
     * @param status The status of the bill (e.g., paid, unpaid).
     */
    public Bill(String meter, String month, int units, double totalBill, String status) {
        this.meter = meter;
        this.month = month;
        this.units = units;
        this.totalBill = totalBill;
        this.status = status;
    }

    /*
     * Calculates the total bill amount based on the number of units consumed.
     *
     * @return The total bill amount.
     */
    public String getMeter() {
        return meter;
    }


    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculate total bill amount based on units and tax rates
     * @param tax Tax rates to use for calculation
     * @return Calculated bill amount
     */
    public double calculateBillAmount(Tax tax) {
        double billAmount = units * tax.getCostPerUnit();
        billAmount += tax.getMeterRent();
        billAmount += tax.getServiceCharge();

        // Add taxes
        double serviceTax = billAmount * (tax.getServiceTax() / 100.0);
        double cbl = billAmount * (tax.getClimateChangeLevy() / 100.0);
        double fixedTax = tax.getFixedTax();

        totalBill = billAmount + serviceTax + cbl + fixedTax;
        return totalBill;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "meter='" + meter + '\'' +
                ", month='" + month + '\'' +
                ", units=" + units +
                ", totalBill=" + totalBill +
                ", status='" + status + '\'' +
                '}';
    }

}

