package nl.sikken.bertrik.hab;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

/**
 * Decoding according to "custom_format_icss" format, as used in (for example):
 */
public final class ICSSPayload {

    private final long timeStamp;
    private final double loadVoltage;
    private final double noloadVoltage;
    private final double boardTemp;
    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final int numSats;
    private final int pressure;
    
    /**
     * Constructor.
     * 
     * @param timeStamp the time stamp (UTC seconds)
     * @param loadVoltage the solar voltage under GPS load (volts)
     * @param noloadVoltage the solar voltage NOT under GPS load (volts)
     * @param boardTemp the board temperature (degrees celcius)
     * @param pressure the board pressure (millibars)
     * @param latitude the latitude (units of 1E-7)
     * @param longitude the longitude (units of 1E-7)
     * @param altitude the altitude (unit?)
     * @param numSats number of satellites used in fix
     */
    public ICSSPayload(long timeStamp, double loadVoltage, double noloadVoltage, double boardTemp, double latitude, double longitude,
            double altitude, int numSats, int pressure) {
        this.timeStamp = timeStamp;
        this.loadVoltage = loadVoltage;
        this.noloadVoltage = noloadVoltage;
        this.boardTemp = boardTemp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.numSats = numSats;
        this.pressure = pressure;

    }

    /**
     * Parses a raw buffer into a Sodaq payload object.
     * 
     * @param raw the raw buffer
     * @return a parsed object
     * @throws BufferUnderflowException in case of a buffer underflow
     */
    public static ICSSPayload parse(byte[] raw) throws BufferUnderflowException {
        ByteBuffer bb = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN);
        long time = bb.getInt() & 0xFFFFFFFFL;
        double voltage = 3.0 + 0.01 * (bb.get() & 0xFF);
        double boardTemp = bb.get();
        double latitude = bb.getInt() / 1e7;
        double longitude = bb.getInt() / 1e7;
        int altitude = bb.getShort();
        int numSats = bb.get();
        int pressure = bb.get();

        return new ICSSPayload(time, voltage, voltage, boardTemp, latitude, longitude, altitude,  numSats, pressure);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public double getloadVoltage() {
        return loadVoltage;
    }
    
    public double getnoloadVoltage() {
        return noloadVoltage;
    }

    public double getBoardTemp() {
        return boardTemp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public int getNumSats() {
        return numSats;
    }

    public int getPressure() {
        return pressure;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "ts=%d,batt=%.2f,temp=%.0f,lat=%f,lon=%f,alt=%.0f", timeStamp, loadVoltage,
                boardTemp, latitude, longitude, altitude);
    }

}
