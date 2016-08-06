package org.michenux.yourappidea.aroundme;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

// https://api.mongolab.com/api/1/databases/michenuxdb/collections/Places?q={\'location\':{$near:{$geometry:{\'type\':\'Point\',\'coordinates\':[%1$s,%2$s]},$maxDistance:%3$s}}}&amp;apiKey=50a2444ae4b0cd0bfc124248
public interface MongolabPlaceService {
    @GET("/api/1/databases/michenuxdb/collections/Places")
    Observable<List<Place>> getPlaces(@Query("query") String query, @Query("apiKey") String apiKey);
}
