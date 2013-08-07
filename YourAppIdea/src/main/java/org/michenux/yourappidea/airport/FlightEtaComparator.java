package org.michenux.yourappidea.airport;

import java.util.Comparator;

public class FlightEtaComparator implements Comparator<Flight> {

	public int compare(Flight f1, Flight f2) {
		return (int) f1.getEta().getTime() - (int) f2.getEta().getTime();
	}
}
