package org.michenux.drodrolib.ui.navdrawer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface NavDrawerViewTypeCreator {

    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, LayoutInflater inflater);

    public int getType();
}
