package nl.sikken.bertrik.hab;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Decoding according to "custom_format_icss" format, as used in (for example):
 */
public final class ICSSPayload {

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
    private final List<past_postion_time> past_position_times;

    /**
     * Constructor.
     * 
     * @param loadVoltage the solar voltage under GPS load (volts)
     * @param noloadVoltage the solar voltage NOT under GPS load (volts)
     * @param boardTemp the board temperature (degrees celcius)
     * @param pressure the board pressure (millibars)
     * @param latitude the latitude (units of 1E-7)
     * @param longitude the longitude (units of 1E-7)
     * @param altitude the altitude (unit?)
     * @param numSats number of satellites used in fix
	 * @param past_position_times
	 */
    public ICSSPayload( int loadVoltage, int noloadVoltage, byte boardTemp, float latitude,
    		float longitude, int altitude, int numSats, int pressure, int data_received_flag, int reset_cnt,
    		List<past_postion_time> past_position_times) {
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
    	this.past_position_times = past_position_times;
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
        
        
        
        // tips for parsing 3 bytes; https://stackoverflow.com/a/13154859
        //long ts = ((bb.get() & 0xFF) | ((bb.get() & 0xFF) << 8) | ((bb.get() & 0x0F) << 16)) * 60 + 1577840461;

        
        List<past_postion_time> past_position_times = new ArrayList<past_postion_time>();
        

        
        return new ICSSPayload(loadVoltage, noloadVoltage, boardTemp, latitude, longitude, altitude,  numSats,
        		pressure, data_received_flag, reset_cnt, past_position_times);
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
        return String.format(Locale.ROOT, "batt=%d,temp=%d,lat=%f,lon=%f,alt=%d", loadVoltage,
                boardTemp, latitude, longitude, altitude);
    }

	public List<past_postion_time> getPast_position_times() {
		return past_position_times;
	}

}

