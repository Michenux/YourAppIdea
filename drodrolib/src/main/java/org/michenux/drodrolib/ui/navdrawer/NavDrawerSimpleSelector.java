package org.michenux.drodrolib.ui.navdrawer;

import android.os.Parcel;
import android.os.Parcelable;

public class NavDrawerSimpleSelector implements Parcelable {

    private int mSelectedItem = 0 ;

    public int getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.mSelectedItem = selectedItem;
    }

    public boolean hasSelection() {
        return mSelectedItem != 0;
    }

    public void resetSelection() {
        this.mSelectedItem = 0 ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSelectedItem);
    }

    public NavDrawerSimpleSelector() {
    }

    private NavDrawerSimpleSelector(Parcel in) {
        this.mSelectedItem = in.readInt();
    }

    public static final Parcelable.Creator<NavDrawerSimpleSelector> CREATOR = new Parcelable.Creator<NavDrawerSimpleSelector>() {
        public NavDrawerSimpleSelector createFromParcel(Parcel source) {
            return new NavDrawerSimpleSelector(source);
        }

        public NavDrawerSimpleSelector[] newArray(int size) {
            return new NavDrawerSimpleSelector[size];
        }
    };
}
