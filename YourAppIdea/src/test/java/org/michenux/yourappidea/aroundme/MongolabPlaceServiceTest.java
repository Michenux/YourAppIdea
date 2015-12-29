package org.michenux.yourappidea.aroundme;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.michenux.yourappidea.R;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

public class MongolabPlaceServiceTest {

    @Mock
    private Context mContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mContext.getString(R.string.aroundme_placeremoteprovider_url)).thenReturn("https://api.mongolab.com/");
        when(mContext.getString(R.string.aroundme_apiKey)).thenReturn("50a2444ae4b0cd0bfc124248");
    }

    @Test
    public void testRemoteProvider() {
        MongolabPlaceService placeService = MongolabPlaceServiceFactory.create(mContext);

        String query = "{'location':{$near:{$geometry:{'type':'Point','coordinates':["
                + 0 + "," + 0 + "]},$maxDistance:1000000000}}}";

        Observable<List<Place>> observable =
                placeService.getPlaces(query, mContext.getString(R.string.aroundme_apiKey));
        TestSubscriber<List<Place>> testSubscriber = new TestSubscriber<>();
        observable.subscribe(testSubscriber);
        testSubscriber.assertNoErrors();

        List<List<Place>> events = testSubscriber.getOnNextEvents();
        Assert.assertNotNull(events);
        Assert.assertFalse(events.isEmpty());

        List<Place> places = events.get(0);
        Assert.assertNotNull(places);
        Assert.assertFalse(places.isEmpty());
    }
}
