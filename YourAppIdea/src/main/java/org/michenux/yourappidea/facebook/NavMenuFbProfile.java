package org.michenux.yourappidea.facebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerCustomItem;
import org.michenux.yourappidea.R;

public class NavMenuFbProfile implements NavDrawerCustomItem {

    public static final int ITEM_TYPE = 2 ;

    private int mId ;

    private UserHelper mUserHelper;

    public static NavMenuFbProfile createFbProfileMenuItem( int id, UserHelper userHelper ) {
        return new NavMenuFbProfile(id, userHelper);
    }

    public NavMenuFbProfile(int id, UserHelper userHelper) {
        this.mId = id;
        this.mUserHelper = userHelper;
    }

    @Override
    public View getCustomView(View convertView, ViewGroup parentView, NavDrawerCustomItem navDrawerItem, LayoutInflater inflater) {

        NavMenuFbProfileHolder navMenuFbProfileHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate( R.layout.navdrawer_fb_profile, parentView, false);
            navMenuFbProfileHolder = new NavMenuFbProfileHolder();
            navMenuFbProfileHolder.profilePictureView = (ProfilePictureView) convertView.findViewById(R.id.navmenu_fb_profile_picture);
            navMenuFbProfileHolder.textView = (TextView) convertView.findViewById(R.id.navmenu_fb_profile_firstName);
            convertView.setTag(navMenuFbProfileHolder);
        }

        if ( navMenuFbProfileHolder == null ) {
            navMenuFbProfileHolder = (NavMenuFbProfileHolder) convertView.getTag();
        }

        if ( mUserHelper.getCurrentUser() != null ) {
            navMenuFbProfileHolder.profilePictureView.setVisibility(View.VISIBLE);
            navMenuFbProfileHolder.textView.setVisibility(View.VISIBLE);
            navMenuFbProfileHolder.profilePictureView.setProfileId(mUserHelper.getCurrentUser().getUserId());
            navMenuFbProfileHolder.textView.setText( convertView.getContext().getString(R.string.facebook_message_welcome, mUserHelper.getCurrentUser().getUserName()));
        }
        else {
            navMenuFbProfileHolder.profilePictureView.setVisibility(View.GONE);
            navMenuFbProfileHolder.textView.setVisibility(View.GONE);
        }

        return convertView ;
    }

    @Override
    public int getId() {
        return this.mId;
    }

    @Override
    public int getLabel() {
        return 0;
    }

    @Override
    public int getType() {
        return ITEM_TYPE;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    private static class NavMenuFbProfileHolder {
        public ProfilePictureView profilePictureView;
        public TextView textView;
    }
}
