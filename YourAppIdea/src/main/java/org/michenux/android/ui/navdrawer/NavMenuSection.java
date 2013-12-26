package org.michenux.android.ui.navdrawer;

public class NavMenuSection implements NavDrawerItem {

	public static final int SECTION_TYPE = 0;

	private int id;
	
	private int label;

	@Override
	public int getType() {
		return SECTION_TYPE;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean updateActionBarTitle() {
		return false;
	}

    @Override
    public boolean isCheckable() {
        return false;
    }
}
