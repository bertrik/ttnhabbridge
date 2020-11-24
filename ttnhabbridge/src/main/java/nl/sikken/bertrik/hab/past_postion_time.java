package nl.sikken.bertrik.hab;

public final class past_postion_time {


	private final float longitude;
    private final float latitude;
    private final int altitude;
    private final long unix_time;


    public past_postion_time(float longitude, float latitude, int altitude, long unix_time) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.unix_time = unix_time;
	}

	public float getLongitude() {
		return longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public int getAltitude() {
		return altitude;
	}
	public long getUnix_time() {
		return unix_time;
	}

}
