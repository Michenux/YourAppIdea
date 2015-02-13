package org.michenux.drodrolib.ui.navdrawer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class NavdrawerHeaderArrowView extends ImageView {

    private boolean mExpanded = false;
    private static final int[] EXPANDED_STATE = new int[] { android.R.attr.state_expanded };

    public NavdrawerHeaderArrowView(Context context) {
        super(context);
    }

    public NavdrawerHeaderArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] state = super.onCreateDrawableState(extraSpace + 1);
        if (mExpanded) {
            mergeDrawableStates(state, EXPANDED_STATE);
        }
        return state;
    }

    public boolean switchExpandedState() {
        this.mExpanded = !mExpanded;
        refreshDrawableState();
        return this.mExpanded;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        if ( mExpanded != expanded) {
            switchExpandedState();
        }
    }
}
