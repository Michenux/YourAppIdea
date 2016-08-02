package org.michenux.yourappidea.airport;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.michenux.yourappidea.R;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

public class AirportServiceInfoTest {

    @Mock
    private Context mContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mContext.getString(R.string.airport_rest_url)).thenReturn("http://www.flightradar24.com/");
    }

    @Test
    public void testArrivalFlight() throws Exception {

        AirportInfoService airportInfoService = AirportInfoServiceFactory.create(mContext);
        Observable<AirportRestResponse> observable =
                airportInfoService.getFlights("ORY", "in");
        TestSubscriber<AirportRestResponse> testSubscriber = new TestSubscriber<>();
        observable.subscribe(testSubscriber);
        testSubscriber.assertNoErrors();

        List<AirportRestResponse> events = testSubscriber.getOnNextEvents();
        Assert.assertNotNull(events);
        Assert.assertFalse(events.isEmpty());

        AirportRestResponse response = events.get(0);

        List<Flight> flights = response.getFlights();
        Assert.assertNotNull(flights);
        Assert.assertFalse(flights.isEmpty());
    }
}
