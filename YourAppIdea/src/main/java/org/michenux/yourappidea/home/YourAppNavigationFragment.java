package org.michenux.yourappidea.home;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.michenux.drodrolib.gms.auth.GoogleAuthDelegate;
import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerActivityConfiguration;
import org.michenux.drodrolib.ui.navdrawer.NavdrawerHeaderArrowView;
import org.michenux.drodrolib.ui.navdrawer.NavigationDrawerFragment;
import org.michenux.yourappidea.NavigationController;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.airport.AirportListFragment;
import org.michenux.yourappidea.aroundme.AroundMeFragment;
import org.michenux.yourappidea.donations.DonateFragment;
import org.michenux.yourappidea.facebook.FacebookDelegate;
import org.michenux.yourappidea.friends.FriendMainFragment;
import org.michenux.yourappidea.map.SimpleMapFragment;
import org.michenux.yourappidea.tutorial.TutorialListFragment;

import javax.inject.Inject;

public class YourAppNavigationFragment extends NavigationDrawerFragment {
    @Inject
    NavigationController navController;

    @Inject
    UserHelper mUserHelper;

    private boolean mPrimaryMenuDisplayed = true;

    private boolean mHeaderArrowOpened = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mPrimaryMenuDisplayed = savedInstanceState.getBoolean("primaryMenuDisplayed");
        }
    }

    @Override
    protected NavDrawerActivityConfiguration createNavigurationConfiguration() {
        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration.Builder()
                .layout(R.layout.navdrawer)
                .navigationViewId(R.id.navigation)
                .drawerLayoutId(R.id.drawer_layout)
                .toolbarId(R.id.toolbar)
                .build();
        navDrawerActivityConfiguration.setUpdateTitleWhenMenuItemClick(R.id.navdrawer_tutorial);
        navDrawerActivityConfiguration.setUpdateTitleWhenMenuItemClick(R.id.navdrawer_friends);
        navDrawerActivityConfiguration.setUpdateTitleWhenMenuItemClick(R.id.navdrawer_airport);
        navDrawerActivityConfiguration.setUpdateTitleWhenMenuItemClick(R.id.navdrawer_map);
        navDrawerActivityConfiguration.setUpdateTitleWhenMenuItemClick(R.id.navdrawer_aroundme);
        navDrawerActivityConfiguration.setUpdateTitleWhenMenuItemClick(R.id.navdrawer_settings);
        navDrawerActivityConfiguration.setUpdateTitleWhenMenuItemClick(R.id.navdrawer_donations);

        return navDrawerActivityConfiguration;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!this.mPrimaryMenuDisplayed) {
            mHeaderArrowOpened = true;
        }

        if (this.mPrimaryMenuDisplayed) {
            createMenu(R.menu.navigation_drawer_menu);
        } else {
            createMenu(R.menu.navigation_drawer_secondarymenu);

        }
    }

    protected void createMenu(@MenuRes int menu) {
        this.getNavigationView().getMenu().clear();
        this.getNavigationView().inflateMenu(menu);
        setupHeaderView();
    }

    public void setupHeaderView() {
        View headerView = this.getNavigationView().getHeaderView(0);
        ViewGroup connectedViewGroup = (ViewGroup) headerView.findViewById(R.id.nadrawer_loginheader_connected_viewgroup);
        TextView nameView = (TextView) headerView.findViewById(R.id.nadrawer_loginheader_name);
        TextView mailView = (TextView) headerView.findViewById(R.id.nadrawer_loginheader_email);
        NavdrawerHeaderArrowView arrowView = (NavdrawerHeaderArrowView) headerView.findViewById(R.id.navdrawer_loginheader_arrow);
        ViewGroup disconnectedViewGroup = (ViewGroup) headerView.findViewById(R.id.nadrawer_loginheader_disconnected_viewgroup);
        TextView loginView = (TextView) headerView.findViewById(R.id.nadrawer_loginheader_login);

        if (mUserHelper.getCurrentUser() == null) {
            loginView.setOnClickListener(v -> {
                if (mUserHelper.getCurrentUser() == null) {
                    navController.showLogin(YourAppNavigationFragment.this.getActivity());
                }
            });
            connectedViewGroup.setVisibility(View.GONE);
            disconnectedViewGroup.setVisibility(View.VISIBLE);
            arrowView.setOnClickListener(null);
        } else {
            nameView.setText(mUserHelper.getCurrentUser().getDisplayName());
            mailView.setText(mUserHelper.getCurrentUser().getMail());
            loginView.setOnClickListener(null);
            arrowView.setExpanded(mHeaderArrowOpened);
            arrowView.setOnClickListener(v -> {
                NavdrawerHeaderArrowView view = (NavdrawerHeaderArrowView) v;
                mHeaderArrowOpened = view.switchExpandedState();
                if (view.isExpanded()) {
                    showSecondaryMenu();
                } else {
                    showPrimaryMenu();
                }
            });

            connectedViewGroup.setVisibility(View.VISIBLE);
            disconnectedViewGroup.setVisibility(View.GONE);
        }
    }

    protected void showSecondaryMenu() {
        this.mHeaderArrowOpened = true;
        this.getNavigationView().getMenu().clear();
        this.getNavigationView().inflateMenu(R.menu.navigation_drawer_secondarymenu);
        this.mPrimaryMenuDisplayed = false;
    }

    protected void showPrimaryMenu() {
        this.mHeaderArrowOpened = false;
        this.getNavigationView().getMenu().clear();
        this.getNavigationView().inflateMenu(R.menu.navigation_drawer_menu);
        this.mPrimaryMenuDisplayed = true;
    }

    @Override
    protected void onNavItemSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.navdrawer_friends:
                FriendMainFragment fg = new FriendMainFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fg)
                        .commit();
                break;
            case R.id.navdrawer_airport:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AirportListFragment())
                        .commit();
                break;
            case R.id.navdrawer_map:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new SimpleMapFragment())
                        .commit();
                break;
            case R.id.navdrawer_tutorial:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new TutorialListFragment())
                        .commit();
                break;
            case R.id.navdrawer_aroundme:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AroundMeFragment())
                        .commit();
                break;
            case R.id.navdrawer_settings:
                this.navController.showSettings(this.getActivity());
                break;
            case R.id.navdrawer_rating:
                this.navController.startAppRating(this.getActivity());
                break;
            case R.id.navdrawer_donations:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new DonateFragment())
                        .commit();
                break;
            case R.id.navdrawer_changelog:
                this.navController.showChangeLog(this.getActivity());
                break;
            case R.id.navdrawer_eula:
                this.navController.showEula(this.getActivity());
                break;
            case R.id.navdrawer_logout:
                if (mUserHelper.getCurrentUser() != null) {
                    switch (mUserHelper.getCurrentUser().getProvider()) {
                        case FacebookDelegate.PROVIDER_NAME:
                            ((YourAppMainActivity) this.getActivity()).getFacebookDelegate().logout();
                            break;
                        case GoogleAuthDelegate.PROVIDER_NAME:
                            ((YourAppMainActivity) this.getActivity()).getGoogleAuthDelegate().signOut();
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
