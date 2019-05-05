package nl.sikken.bertrik.hab;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

/**
 * Decoding according to "SODAQ" format, as used in (for example):
 * https://github.com/SodaqMoja/SodaqOne-UniversalTracker
 */
public final class SodaqOnePayload {

    private final long timeStamp;
    private final double battVoltage;
    private final double boardTemp;
    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final double sog;
    private final int cog;
    private final int numSats;
    private final int ttf;

    /**
     * Constructor.
     * 
     * @param timeStamp the time stamp (UTC seconds)
     * @param battVoltage the battery voltage (volts)
     * @param boardTemp the board temperature (degrees celcius)
     * @param latitude the latitude (units of 1E-7)
     * @param longitude the longitude (units of 1E-7)
     * @param altitude the altitude (unit?)
     * @param sog the speed over ground (unit?)
     * @param cog the course over ground (unit?)
     * @param numSats number of satellites used in fix
     * @param ttf the time to fix
     */
    public SodaqOnePayload(long timeStamp, double battVoltage, double boardTemp, double latitude, double longitude,
            double altitude, double sog, int cog, int numSats, int ttf) {
        this.timeStamp = timeStamp;
        this.battVoltage = battVoltage;
        this.boardTemp = boardTemp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.sog = sog;
        this.cog = cog;
        this.numSats = numSats;
        this.ttf = ttf;
    }

    /**
     * Parses a raw buffer into a Sodaq payload object.
     * 
     * @param raw the raw buffer
     * @return a parsed object
     * @throws BufferUnderflowException in case of a buffer underflow
     */
    public static SodaqOnePayload parse(byte[] raw) throws BufferUnderflowException {
        ByteBuffer bb = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN);
        long time = bb.getInt() & 0xFFFFFFFFL;
        double voltage = 3.0 + 0.01 * (bb.get() & 0xFF);
        double boardTemp = bb.get();
        double latitude = bb.getInt() / 1e7;
        double longitude = bb.getInt() / 1e7;
        int altitude = bb.getShort();
        double sog = bb.getShort() / 360.0;
        int cog = bb.get();
        int numSats = bb.get();
        int ttf = bb.get();

        return new SodaqOnePayload(time, voltage, boardTemp, latitude, longitude, altitude, sog, cog, numSats, ttf);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public double getBattVoltage() {
        return battVoltage;
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

    public double getSog() {
        return sog;
    }

    public int getCog() {
        return cog;
    }

    public int getNumSats() {
        return numSats;
    }

    public int getTtf() {
        return ttf;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "ts=%d,batt=%.2f,temp=%.0f,lat=%f,lon=%f,alt=%.0f", timeStamp, battVoltage,
                boardTemp, latitude, longitude, altitude);
    }

}
