package org.michenux.drodrolib.ui.navdrawer;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class NavDrawerItem {

    private int mId ;
    public abstract int getType();

    public @StringRes abstract int getLabel();

    public abstract boolean updateActionBarTitle();

    public abstract boolean isCheckable();

    public abstract void onBindViewHolder(ViewHolder viewHolder, int position);

    public NavDrawerItem( int id ) {
        this.mId = id ;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public boolean closeDrawerOnClick() {
        return true;
    }
}
