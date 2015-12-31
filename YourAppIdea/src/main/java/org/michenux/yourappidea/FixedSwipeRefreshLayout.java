package org.michenux.yourappidea;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by lmichenaud on 31/12/2015.
 */
public class FixedSwipeRefreshLayout extends SwipeRefreshLayout {
    public FixedSwipeRefreshLayout(Context context) {
        super(context);
    }

    public FixedSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        clearAnimation();
        return super.onSaveInstanceState();
    }
}
