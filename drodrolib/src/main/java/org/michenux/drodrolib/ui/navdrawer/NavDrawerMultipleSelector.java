package org.michenux.drodrolib.ui.navdrawer;

import android.util.SparseBooleanArray;

public class NavDrawerMultipleSelector {

    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private boolean mIsSelectable = false;

    public void setSelected(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
    }

    public boolean isSelected(int position) {
        return mSelectedPositions.get(position);
    }

    public void setSelectable(boolean selectable) {
        mIsSelectable = selectable;
    }

    public boolean isSelectable() {
        return mIsSelectable;
    }
}
