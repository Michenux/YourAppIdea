package org.michenux.drodrolib.ui.graphics;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;

public class BitmapHelper {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static int getSizeInBytes(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }
}
