package org.michenux.yourappidea.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.michenux.drodrolib.gms.gplus.GoogleApiClientDelegate;
import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerActivityConfiguration;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerItem;
import org.michenux.drodrolib.ui.navdrawer.NavMenuBuilder;
import org.michenux.drodrolib.ui.navdrawer.NavigationDrawerFragment;
import org.michenux.drodrolib.ui.navdrawer.items.NavMenuDivider;
import org.michenux.drodrolib.ui.navdrawer.items.NavMenuHeader;
import org.michenux.drodrolib.ui.navdrawer.items.NavMenuLabelWithIcon;
import org.michenux.drodrolib.ui.navdrawer.items.NavMenuLoginHeader;
import org.michenux.drodrolib.ui.navdrawer.items.NavMenuSection;
import org.michenux.yourappidea.NavigationController;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.airport.AirportListFragment;
import org.michenux.yourappidea.aroundme.AroundMeFragment;
import org.michenux.yourappidea.donations.DonateFragment;
import org.michenux.yourappidea.facebook.FbLoginDelegate;
import org.michenux.yourappidea.friends.FriendMainFragment;
import org.michenux.yourappidea.map.SimpleMapFragment;
import org.michenux.yourappidea.tutorial.TutorialListFragment;

import java.util.List;

import javax.inject.Inject;

public class YourAppNavigationFragment extends NavigationDrawerFragment {

    @Inject
    NavigationController navController;

    @Inject
    UserHelper mUserHelper;

    private List<NavDrawerItem> mPrimaryMenu ;

    private List<NavDrawerItem> mSecondaryMenu;

    private NavMenuLoginHeader mHeaderDrawerItem;

    private boolean mPrimaryMenuDisplayed = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ( savedInstanceState != null ) {
            mPrimaryMenuDisplayed = savedInstanceState.getBoolean("primaryMenuDisplayed");
        }
    }

    @Override
    protected NavDrawerActivityConfiguration createNavigurationConfiguration() {
        NavMenuLoginHeader.OnHeaderProfileMenuListener mHeaderArrowListener = new NavMenuLoginHeader.OnHeaderProfileMenuListener() {

            @Override
            public void doLogin() {
                if ( mUserHelper.getCurrentUser() == null ) {
                    navController.showLogin(YourAppNavigationFragment.this.getActivity());
                }
            }

            @Override
            public void onOpen() {
                showSecondaryMenu();
            }

            @Override
            public void onClose() {
                showPrimaryMenu();
            }
        };

        mHeaderDrawerItem = NavMenuLoginHeader.createMenuItem(10, R.drawable.navdrawer_header_bg, mUserHelper, mHeaderArrowListener);

        NavMenuBuilder primaryMenuBuilder = new NavMenuBuilder()
                .addDrawerItem(mHeaderDrawerItem)
                .addLabelWithIcon(104, R.string.navdrawer_tutorial, R.drawable.navdrawer_tutorial_selector, true, true)
                .addLabelWithIcon(101, R.string.navdrawer_listdetail, R.drawable.navdrawer_friends_selector, true, true)
                .addLabelWithIcon(102, R.string.navdrawer_airport, R.drawable.navdrawer_airport_selector, true, true)
                .addLabelWithIcon(103, R.string.navdrawer_simplemap, R.drawable.navdrawer_map_selector, true, true)
                .addLabelWithIcon(105, R.string.navdrawer_aroundme, R.drawable.navdrawer_aroundme_selector, true, true)
                .addDivider(199)
                .addLabelWithIcon(201, R.string.navdrawer_settings, R.drawable.navdrawer_settings_selector, true, true)
                .addLabelWithIcon(202, R.string.navdrawer_rating, R.drawable.navdrawer_ratings, false, false)
                .addLabelWithIcon(203, R.string.navdrawer_donations, R.drawable.navdrawer_donations_selector, true, true)
                .addLabelWithIcon(204, R.string.navdrawer_changelog, R.drawable.navdrawer_changelog_selector, false, false)
                .addLabelWithIcon(205, R.string.navdrawer_eula, R.drawable.navdrawer_eula_selector, false, false);
        mPrimaryMenu = primaryMenuBuilder.build();

        NavMenuBuilder secondaryMenuBuilder = new NavMenuBuilder()
                .addDrawerItem(mHeaderDrawerItem)
                .addDrawerItem(NavMenuLabelWithIcon.createMenuItem(270, R.string.navdrawer_logout, R.drawable.navdrawer_logout_selector, false, false));
        mSecondaryMenu = secondaryMenuBuilder.build();

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration.Builder()
                .layout(R.layout.navdrawer)
                .drawerLayoutId(R.id.drawer_layout)
                .recyclerViewId(R.id.navdrawer_recyclerview)
                .toolbarId(R.id.toolbar)
                .leftDrawerId(R.id.navdrawer_recyclerview)
                .registerViewTypeCreator(new NavMenuLabelWithIcon.ViewHolderCreator())
                .registerViewTypeCreator(new NavMenuDivider.ViewHolderCreator())
                .registerViewTypeCreator(new NavMenuSection.ViewHolderCreator())
                .registerViewTypeCreator(new NavMenuHeader.ViewHolderCreator())
                .registerViewTypeCreator(new NavMenuLoginHeader.ViewHolderCreator())
                .build();
        return navDrawerActivityConfiguration;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if ( !this.mPrimaryMenuDisplayed) {
            mHeaderDrawerItem.setArrowOpened(true);
        }

        if ( this.mPrimaryMenuDisplayed ) {
            this.updateMenu(mPrimaryMenu);
        }
        else {
            this.updateMenu(mSecondaryMenu);
        }
    }

    protected void showSecondaryMenu() {
        this.mHeaderDrawerItem.setArrowOpened(true);
        this.updateMenu(this.mSecondaryMenu);
        this.mPrimaryMenuDisplayed = false;
    }

    protected void showPrimaryMenu() {
        this.mHeaderDrawerItem.setArrowOpened(false);
        this.updateMenu(this.mPrimaryMenu);
        this.mPrimaryMenuDisplayed = true;
    }

    @Override
    protected void onNavItemSelected(int id) {
        switch (id) {
            case 101:
                FriendMainFragment fg = new FriendMainFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fg)
                        .commit();
                break;
            case 102:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AirportListFragment())
                        .commit();
                break;
            case 103:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new SimpleMapFragment())
                        .commit();
                break;
            case 104:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new TutorialListFragment())
                        .commit();
                break;
            case 105:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AroundMeFragment())
                        .commit();
                break;
            case 201:
                this.navController.showSettings(this.getActivity());
                break;
            case 202:
                this.navController.startAppRating(this.getActivity());
                break;
            case 203:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new DonateFragment())
                        .commit();
                break;
            case 204:
                this.navController.showChangeLog(this.getActivity());
                break;
            case 205:
                this.navController.showEula(this.getActivity());
                break;

            case 260:
                if ( mUserHelper.getCurrentUser() == null ) {
                    this.navController.showLogin(this.getActivity());
                }
                break;
            case 270:
                if ( mUserHelper.getCurrentUser() != null ) {
                    switch (mUserHelper.getCurrentUser().getProvider()) {
                        case FbLoginDelegate.PROVIDER_NAME:
                            ((YourAppMainActivity)this.getActivity()).getFbLoginDelegate().logout();
                            break;
                        case GoogleApiClientDelegate.PROVIDER_NAME:
                            ((YourAppMainActivity)this.getActivity()).getGoogleApiClientDlg().signOut();
                            break;
                    }
                }

                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("primaryMenuDisplayed", this.mPrimaryMenuDisplayed);
    }
}
