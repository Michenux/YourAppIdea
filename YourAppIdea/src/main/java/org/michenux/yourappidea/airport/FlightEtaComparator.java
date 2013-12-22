package org.michenux.yourappidea.airport;

import java.util.Comparator;

public class FlightEtaComparator implements Comparator<Flight> {

	public int compare(Flight f1, Flight f2) {

        if ( f1.getEta() == null && f2.getEta() == null ) {
            return 0;
        }

        if ( f1.getEta() != null && f2.getEta() == null ) {
            return 1;
        }

        if ( f1.getEta() == null && f2.getEta() != null ) {
            return -1;
        }

		return (int) f1.getEta().getTime() - (int) f2.getEta().getTime();
	}
}
