package org.michenux.drodrolib.ui.navdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.michenux.drodrolib.R;
import org.michenux.drodrolib.security.UserHelper;

public class NavMenuItem implements NavDrawerItem {

	public static final int ITEM_TYPE = 1;
	
	private int mId ;
	
	private int mLabel ;
	
	private int mIcon ;
	
	private boolean mUpdateActionBarTitle ;

    private boolean mCheckable ;

    public static NavMenuItem createMenuItem( int id, int label, int icon, boolean updateActionBarTitle, boolean checkable ) {
        return new NavMenuItem(id, label, icon, updateActionBarTitle, checkable);
    }

    public NavMenuItem(int id, int label, int icon, boolean updateActionBarTitle, boolean checkable) {
        this.mId = id;
        this.mIcon = icon;
        this.mLabel = label;
        this.mUpdateActionBarTitle = updateActionBarTitle;
        this.mCheckable = checkable;
    }

	@Override
	public int getType() {
		return ITEM_TYPE;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public int getLabel() {
		return mLabel;
	}

	public void setLabel(int label) {
		this.mLabel = label;
	}

	public int getIcon() {
		return mIcon;
	}

	public void setIcon(int icon) {
		this.mIcon = icon;
	}

	@Override
	public boolean updateActionBarTitle() {
		return this.mUpdateActionBarTitle;
	}

	public void setUpdateActionBarTitle(boolean updateActionBarTitle) {
		this.mUpdateActionBarTitle = updateActionBarTitle;
	}

    public boolean isCheckable() {
        return mCheckable;
    }

    public void setCheckable(boolean checkable) {
        this.mCheckable = checkable;
    }

    public View getView( View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem, LayoutInflater inflater ) {

        NavMenuItem menuItem = (NavMenuItem) navDrawerItem ;
        NavMenuItemHolder navMenuItemHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate( R.layout.navdrawer_item, parentView, false);
            TextView labelView = (TextView) convertView
                    .findViewById( R.id.navmenuitem_label );
            ImageView iconView = (ImageView) convertView
                    .findViewById( R.id.navmenuitem_icon );

            navMenuItemHolder = new NavMenuItemHolder();
            navMenuItemHolder.labelView = labelView ;
            navMenuItemHolder.iconView = iconView ;

            convertView.setTag(navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
        }

        navMenuItemHolder.labelView.setText(menuItem.getLabel());
        navMenuItemHolder.iconView.setImageResource(menuItem.getIcon());

        return convertView ;
    }

    private static class NavMenuItemHolder {
        private TextView labelView;
        private ImageView iconView;
    }
}
