package org.michenux.drodrolib.ui.navdrawer;

public interface NavDrawerItem {

	public int getId();
	
	public int getLabel();
	
	public int getType();

	public boolean updateActionBarTitle();

    public boolean isCheckable();
}
