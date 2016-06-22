package com.example.socket_com;

import java.io.Serializable;

public class GPSLocation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double latitude,longitude;

	public GPSLocation(double latitude, double longitude) {
		this.latitude=latitude;
		this.longitude=longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
}
