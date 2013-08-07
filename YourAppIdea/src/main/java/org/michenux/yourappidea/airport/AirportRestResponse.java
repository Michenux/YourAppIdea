package org.michenux.yourappidea.airport;

import java.util.List;

public class AirportRestResponse {

	private List<Flight> flights ;

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
}
