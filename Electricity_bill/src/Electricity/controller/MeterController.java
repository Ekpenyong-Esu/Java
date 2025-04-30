package Electricity.controller;

import Electricity.dataAccessOutput.MeterDAO;
import Electricity.model.Meter;
import java.util.List;

/**
 * Controller class for meter information management operations.
 * Handles the business logic for meter registration and information retrieval.
 */
public class MeterController {

    private final MeterDAO meterDAO;

    /**
     * Constructor initializing the MeterDAO
     */
    public MeterController() {
        meterDAO = new MeterDAO();
    }

    /**
     * Register a new meter with details
     * @param meterNumber Meter number (unique identifier)
     * @param location Meter location (Inside/Outside)
     * @param meterType Type of meter (Electric/Solar/Smart)
     * @param phaseCode Phase code value
     * @param billType Bill type (Normal/Industrial)
     * @param days Billing days (typically 30)
     * @return true if registration successful, false otherwise
     */
    public boolean registerMeter(String meterNumber, String location, String meterType,
                                 String phaseCode, String billType, int days) {
        // Input validation
        if (meterNumber == null || meterNumber.trim().isEmpty() ||
                location == null || location.trim().isEmpty() ||
                meterType == null || meterType.trim().isEmpty() ||
                phaseCode == null || phaseCode.trim().isEmpty() ||
                billType == null || billType.trim().isEmpty() ||
                days <= 0) {
            return false;
        }

        // Check if meter already exists
        Meter existingMeter = meterDAO.getMeterByNumber(meterNumber);
        if (existingMeter != null) {
            return false; // Meter already exists
        }

        // Create meter object
        Meter meter = new Meter(meterNumber, location, meterType, phaseCode, billType, days);

        return meterDAO.addMeter(meter);
    }

    /**
     * Get meter information by meter number
     * @param meterNumber Meter number
     * @return Meter object if found, null otherwise
     */
    public Meter getMeterInfo(String meterNumber) {
        if (meterNumber == null || meterNumber.trim().isEmpty()) {
            return null;
        }

        return meterDAO.getMeterByNumber(meterNumber);
    }

    /**
     * Update meter information
     * @param meter Meter object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateMeterInfo(Meter meter) {
        if (meter == null || meter.getMeterNumber() == null || meter.getMeterNumber().trim().isEmpty()) {
            return false;
        }

        return meterDAO.updateMeter(meter);
    }

    /**
     * Update specific meter information fields
     * @param meterNumber Meter number to identify the meter
     * @param location Updated location
     * @param meterType Updated meter type
     * @param phaseCode Updated phase code
     * @param billType Updated bill type
     * @param days Updated billing days
     * @return true if update successful, false otherwise
     */
    public boolean updateMeter(String meterNumber, String location, String meterType,
                               String phaseCode, String billType, int days) {
        if (meterNumber == null || meterNumber.trim().isEmpty()) {
            return false;
        }

        // Get existing meter info
        Meter meter = meterDAO.getMeterByNumber(meterNumber);
        if (meter == null) {
            return false;
        }

        // Update fields if provided
        if (location != null && !location.trim().isEmpty()) {
            meter.setMeterLocation(location);
        }
        if (meterType != null && !meterType.trim().isEmpty()) {
            meter.setMeterType(meterType);
        }
        if (phaseCode != null && !phaseCode.trim().isEmpty()) {
            meter.setPhaseCode(phaseCode);
        }
        if (billType != null && !billType.trim().isEmpty()) {
            meter.setBillType(billType);
        }
        if (days > 0) {
            meter.setDays(days);
        }

        return meterDAO.updateMeter(meter);
    }

    /**
     * Get all meters in the system
     * @return List of all meters
     */
    public List<Meter> getAllMeters() {
        return meterDAO.getAllMeters();
    }

    /**
     * Get meters by location
     * @param location Location to filter by
     * @return List of meters at the specified location
     */
    public List<Meter> getMetersByLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return getAllMeters();
        }

        return meterDAO.getMetersByLocation(location);
    }

    /**
     * Get meters by type
     * @param type Type to filter by
     * @return List of meters of the specified type
     */
    public List<Meter> getMetersByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return getAllMeters();
        }

        return meterDAO.getMetersByType(type);
    }

    /**
     * Check if a meter exists by its number
     * @param meterNumber Meter number to check
     * @return true if meter exists, false otherwise
     */
    public boolean meterExists(String meterNumber) {
        if (meterNumber == null || meterNumber.trim().isEmpty()) {
            return false;
        }

        return meterDAO.getMeterByNumber(meterNumber) != null;
    }

    /**
     * Delete a meter by meter number
     * @param meterNumber Meter number
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteMeter(String meterNumber) {
        if (meterNumber == null || meterNumber.trim().isEmpty()) {
            return false;
        }

        return meterDAO.deleteMeter(meterNumber);
    }
}