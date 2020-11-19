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
    private final int loadVoltage;
    private final int noloadVoltage;
    private final byte boardTemp;
    private final float latitude;
    private final float longitude;
    private final int altitude;
    private final int numSats;
    private final int pressure;
    private final int data_received_flag;
    private final int reset_cnt;

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
    public ICSSPayload(long timeStamp, int loadVoltage, int noloadVoltage, byte boardTemp, float latitude, float longitude,
            int altitude, int numSats, int pressure, int data_received_flag, int reset_cnt) {
        this.timeStamp = timeStamp;
        this.loadVoltage = loadVoltage;
        this.noloadVoltage = noloadVoltage;
        this.boardTemp = boardTemp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.numSats = numSats;
        this.pressure = pressure;
        this.data_received_flag = data_received_flag;
        this.reset_cnt = reset_cnt;
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
        byte byte0  = bb.get();
        byte byte1  = bb.get();
        byte byte2  = bb.get();
        byte byte3  = bb.get();

        int noloadVoltage = ((byte0 >> 3) & 0b00011111)+18;
        int loadVoltage = (((byte0 << 2) & 0b00011100) | ((byte1 >> 6) & 0b00000011)) + 18;
        byte boardTemp = (byte) ((byte) ((byte1 & 0b00111111) << 2)+1);
        int pressure = ((byte2 >> 1) & 0b01111111)*10;
        int data_received_flag = byte2 & 0b00000001;
        
        int numSats = (byte3>>3) & 0b00011111;
        int reset_cnt = byte3 & 0b00000111;
        
        
        float latitude = (float) ((double)((long)bb.getShort() * (long) 0xFFFF)/1e7);
        float longitude = (float) ((double)((long)bb.getShort() * (long) 0xFFFF)/1e7);
        int altitude = ((bb.getShort() & 0xFFFF) * 0xFF)/1000;

        long ts = 1503518401;
        

        return new ICSSPayload(ts, loadVoltage, noloadVoltage, boardTemp, latitude, longitude, altitude,  numSats,
        		pressure, data_received_flag, reset_cnt);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getloadVoltage() {
        return loadVoltage;
    }
    
    public int getnoloadVoltage() {
        return noloadVoltage;
    }

    public byte getBoardTemp() {
        return boardTemp;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public int getNumSats() {
        return numSats;
    }

    public int getPressure() {
        return pressure;
    }
    
    public int getData_received_flag() {
        return data_received_flag;
    }
    
    public int getReset_cnt() {
        return reset_cnt;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "ts=%d,batt=%d,temp=%d,lat=%f,lon=%f,alt=%d", timeStamp, loadVoltage,
                boardTemp, latitude, longitude, altitude);
    }

}
