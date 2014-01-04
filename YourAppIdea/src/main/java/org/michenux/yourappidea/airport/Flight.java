package org.michenux.yourappidea.airport;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

// {"callsign":"VLG1982","iata":"LIS","type":"A320","lat":39.379,"lon":-8.195,"spd":434,"alt":26175,"flight":"VY1982","name":"Lisbon Lisboa","eta":1364422653}
public class Flight {

	@SerializedName("callsign")
	private String callSign ;
	
	private String iata ;
	
	private String type ;
	
	@SerializedName("lat")
	private double latitude ;
	
	@SerializedName("lon")
	private double longitude ;
	
	@SerializedName("spd")
	private long speed ;
	
	@SerializedName("alt")
	private long altitude ;
	
	private String flight ;
	
	private String name ;

    @SerializedName("eta")
	private Timestamp eta ;

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public long getSpeed() {
		return speed;
	}

	public void setSpeed(long speed) {
		this.speed = speed;
	}

	public long getAltitude() {
		return altitude;
	}

	public void setAltitude(long altitude) {
		this.altitude = altitude;
	}

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getEta() {
		return eta;
	}

	public void setEta(Timestamp eta) {
		this.eta = eta;
	}
}
