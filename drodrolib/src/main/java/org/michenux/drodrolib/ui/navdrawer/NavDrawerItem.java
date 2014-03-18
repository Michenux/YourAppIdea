package org.michenux.drodrolib.ui.navdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface NavDrawerItem {

	public int getId();
	
	public int getLabel();
	
	public int getType();

	public boolean updateActionBarTitle();

    public boolean isCheckable();

    public View getView( View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem, LayoutInflater inflater );
}
