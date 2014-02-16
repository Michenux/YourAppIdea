package org.michenux.drodrolib.ui.navdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface NavDrawerCustomItem extends NavDrawerItem {

    public View getCustomView( View convertView, ViewGroup parentView, NavDrawerCustomItem navDrawerItem, LayoutInflater inflater );
}
