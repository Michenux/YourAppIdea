package org.michenux.yourappidea.tutorial.sync;

import org.michenux.drodrolib.wordpress.json.WPJsonResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

// "http://www.michenux.net/?json=get_recent_posts&amp;count=1"
// "http://www.michenux.net/?json=get_category_posts&slug=android&custom_fields=android_desc&categories=android&count=9999"
// &include=id,title,custom_fields,url,date,modified,excerpt,author,thumbnail_images,content
public interface WordpressService {

    @GET("/")
    Observable<WPJsonResponse> query(
            @Query("json") String command,
            @Query("slug") String slug,
            @Query("custom_fields") String customFields,
            @Query("categories") String categories,
            @Query("count") int count );
}
