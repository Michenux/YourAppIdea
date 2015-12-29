package org.michenux.yourappidea.airport;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

//http://www.flightradar24.com/AirportInfoService.php?airport=ORY&type=in
//LFRS
public interface AirportInfoService {

    @GET("/AirportInfoService.php")
    Observable<AirportRestResponse> getFlights(@Query("airport") String airportCode, @Query("type") String flightType );
}
