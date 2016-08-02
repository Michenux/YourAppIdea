package org.michenux.yourappidea.airport;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

//http://www.flightradar24.com/AirportInfoService.php?airport=ORY&type=in
//new API: https://api.flightradar24.com/common/v1/airport.json?code=ory
//LFRS
public interface AirportInfoService {
    @GET("/AirportInfoService.php")
    Observable<AirportRestResponse> getFlights(@Query("airport") String airportCode, @Query("type") String flightType);
}
