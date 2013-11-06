package org.michenux.android.network.volley;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;

import org.michenux.android.ui.graphics.BitmapHelper;

import java.io.File;
import java.nio.ByteBuffer;

public class DiskBitmapCache extends DiskBasedCache implements ImageLoader.ImageCache {

    public DiskBitmapCache(File rootDirectory, int maxCacheSizeInBytes) {
        super(rootDirectory, maxCacheSizeInBytes);
    }

    public DiskBitmapCache(File cacheDir) {
        super(cacheDir);
    }

    public Bitmap getBitmap(String url) {
        Log.d("Volley", "DiskBitmapCache: get bitmap in cache: " + this);
        Log.d("Volley", "DiskBitmapCache: get bitmap in cache key: " + url);
        final Entry requestedItem = get(url);
        Log.d("Volley", "DiskBitmapCache: get bitmap in cache: value: " + requestedItem);

        if (requestedItem == null)
            return null;

        return BitmapFactory.decodeByteArray(requestedItem.data, 0, requestedItem.data.length);
    }

    public void putBitmap(String url, Bitmap bitmap) {
        Log.d("Volley", "DiskBitmapCache: put bitmap in cache: " + this);
        Log.d("Volley", "DiskBitmapCache: put bitmap in cache: " + url);
        final Entry entry = new Entry();

        ByteBuffer buffer = ByteBuffer.allocate(BitmapHelper.getSizeInBytes(bitmap));
        bitmap.copyPixelsToBuffer(buffer);
        entry.data = buffer.array();

        put(url, entry);
    }
}
