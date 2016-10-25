package org.michenux.yourappidea.tutorial;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.michenux.drodrolib.wordpress.json.WPJsonPost;
import org.michenux.drodrolib.wordpress.json.WPJsonResponse;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.tutorial.sync.WordpressService;
import org.michenux.yourappidea.tutorial.sync.WordpressServiceFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

public class WordpressServiceTest {
    @Mock
    private Context mContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mContext.getString(R.string.tutorial_sync_url)).thenReturn("http://www.michenux.net/");
    }

    @Test
    public void testQuery() throws Exception {
        WordpressService wordpressService = WordpressServiceFactory.create(mContext);
        Observable<WPJsonResponse> observable =
                wordpressService.query("get_recent_posts", "android", "android_desc", "android", 9999);
        TestSubscriber<WPJsonResponse> testSubscriber = new TestSubscriber<>();
        observable.subscribe(testSubscriber);
        testSubscriber.assertNoErrors();

        List<WPJsonResponse> jsonPosts = testSubscriber.getOnNextEvents();
        Assert.assertNotNull(jsonPosts);
        Assert.assertFalse(jsonPosts.isEmpty());

        WPJsonResponse response = jsonPosts.get(0);
        Assert.assertEquals(response.getStatus(), "ok");

        List<WPJsonPost> posts = response.getPosts();
        Assert.assertNotNull(posts);
        Assert.assertFalse(posts.isEmpty());
    }
}
