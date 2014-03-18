package org.michenux.drodrolib.security;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.michenux.drodrolib.R;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerItem;

public class NavDrawerUserLoginItem implements NavDrawerItem {

    public static final int ITEM_TYPE = 2;

    private UserHelper mUserHelper;

    public static NavDrawerUserLoginItem createMenuItem( int id, int icon, UserHelper userHelper ) {
        return new NavDrawerUserLoginItem(id, icon, userHelper);
    }

    public NavDrawerUserLoginItem(int id, int icon, UserHelper userHelper) {
        this.mId = id;
        this.icon = icon;
        this.mUserHelper = userHelper;
    }

    private int mId ;

    private int label ;

    private int icon ;

    private boolean updateActionBarTitle ;

    private boolean checkable ;

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
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public boolean updateActionBarTitle() {
        return this.updateActionBarTitle;
    }

    public void setUpdateActionBarTitle(boolean updateActionBarTitle) {
        this.updateActionBarTitle = updateActionBarTitle;
    }

    public boolean isCheckable() {
        return checkable;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    @Override
    public View getView(View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem, LayoutInflater inflater) {
        NavDrawerUserLoginItem menuItem = (NavDrawerUserLoginItem) navDrawerItem ;
        NavMenuHolder navMenuHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate( R.layout.navdrawer_userlogin, parentView, false);
            TextView labelView = (TextView) convertView
                    .findViewById( R.id.navmenu_userlogin_label );
            ImageView iconView = (ImageView) convertView
                    .findViewById( R.id.navmenu_userlogin_icon );
            TextView providerView = (TextView) convertView
                    .findViewById( R.id.navmenu_userlogin_provider );

            navMenuHolder = new NavMenuHolder();
            navMenuHolder.labelView = labelView ;
            navMenuHolder.iconView = iconView ;
            navMenuHolder.providerView = providerView ;
            convertView.setTag(navMenuHolder);
        }

        if ( navMenuHolder == null ) {
            navMenuHolder = (NavMenuHolder) convertView.getTag();
        }

        if ( mUserHelper.getCurrentUser() == null ) {
            navMenuHolder.labelView.setText(R.string.security_login);
            navMenuHolder.iconView.setImageResource(menuItem.getIcon());
            navMenuHolder.providerView.setVisibility(View.GONE);
        }
        else {
            navMenuHolder.labelView.setText(mUserHelper.getCurrentUser().getDisplayName());
            navMenuHolder.providerView.setVisibility(View.VISIBLE);
            navMenuHolder.providerView.setText(mUserHelper.getCurrentUser().getProviderDisplayName());
            navMenuHolder.iconView.setImageResource(menuItem.getIcon());
        }

        return convertView ;
    }

    private static class NavMenuHolder {
        private TextView labelView;
        private ImageView iconView;
        private TextView providerView;
    }
}
