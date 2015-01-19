package org.michenux.yourappidea.aroundme;

import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

public class RecyclerViewPauseOnScrollListener extends OnScrollListener {

    private ImageLoader mImageLoader;

    private final boolean mPauseOnScroll;
    private final boolean mPauseOnFling;
    private final OnScrollListener mExternalListener;

    public RecyclerViewPauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
        this(imageLoader, pauseOnScroll, pauseOnFling, null);
    }

    public RecyclerViewPauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling,
                                             OnScrollListener customListener) {
        this.mImageLoader = imageLoader;
        this.mPauseOnScroll = pauseOnScroll;
        this.mPauseOnFling = pauseOnFling;
        mExternalListener = customListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mExternalListener != null) {
            mExternalListener.onScrolled(recyclerView, dx, dy);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                mImageLoader.resume();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                if (mPauseOnScroll) {
                    mImageLoader.pause();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                if (mPauseOnFling) {
                    mImageLoader.pause();
                }
                break;
        }
        if (mExternalListener != null) {
            mExternalListener.onScrollStateChanged(recyclerView, newState);
        }
    }
}
