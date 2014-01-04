package org.michenux.drodrolib.network.volley;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BitmapCacheHolder {

    private DiskBitmapCache bitmapCache ;

    @Inject
    public BitmapCacheHolder(){

    }

    public void init( Context context, int size ) {
        bitmapCache = new DiskBitmapCache(context.getCacheDir(), size);
    }

    public DiskBitmapCache getBitmapCache() {
        if ( bitmapCache == null ) {
            throw new IllegalStateException("DiskBitmapCache has never been called.");
        }
        return bitmapCache;
    }
}
