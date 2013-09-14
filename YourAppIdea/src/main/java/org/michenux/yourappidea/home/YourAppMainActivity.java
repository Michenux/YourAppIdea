package org.michenux.yourappidea.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.airport.AirportFragment;
import org.michenux.yourappidea.NavigationController;
import org.michenux.yourappidea.donations.DonateFragment;
import org.michenux.yourappidea.friends.FriendMainFragment;
import org.michenux.android.ui.navdrawer.AbstractNavDrawerActivity;
import org.michenux.android.ui.navdrawer.NavDrawerActivityConfiguration;
import org.michenux.android.ui.navdrawer.NavDrawerAdapter;
import org.michenux.android.ui.navdrawer.NavDrawerItem;
import org.michenux.android.ui.navdrawer.NavMenuItem;
import org.michenux.android.ui.navdrawer.NavMenuSection;

import javax.inject.Inject;

public class YourAppMainActivity extends AbstractNavDrawerActivity {

    @Inject
    NavigationController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getApplication()).inject(this);

        if (savedInstanceState == null) {
            this.navController.goHomeFragment(this);
            this.navController.confirmEulaAndShowChangeLog(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuSection.create(100, "Demos"),
                NavMenuItem.create(101, "List/Detail", "navdrawer_friends",
                        true, true, this),
                NavMenuItem.create(102, "Airport", "navdrawer_airport", true, true,
                this),
                NavMenuItem.create(103, "Simple Map", "navdrawer_map", true, true,
                        this),
                NavMenuSection.create(200, "General"),
                NavMenuItem.create(201, "Settings", "navdrawer_settings", true, true, this),
                NavMenuItem.create(202, "Rate this app", "navdrawer_rating",
                        false, false, this),
                NavMenuItem.create(203, "Donate", "navdrawer_donations", true, true, this),
                NavMenuItem.create(204, "ChangeLog", "navdrawer_changelog", false, false, this),
                NavMenuItem.create(205, "Eula", "navdrawer_eula", false, false, this) };

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.main);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
        navDrawerActivityConfiguration
                .setDrawerShadow(R.drawable.drawer_shadow);
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration
                .setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(new NavDrawerAdapter(
                this, R.layout.navdrawer_item, menu));
        navDrawerActivityConfiguration.setDrawerIcon(R.drawable.ic_drawer);
        return navDrawerActivityConfiguration;
    }

    @Override
    protected void onNavItemSelected(int id) {
        switch (id) {
            case 101:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FriendMainFragment())
                        .commit();
                break;
            case 102:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AirportFragment())
                        .commit();
                break;
            case 103:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new SupportMapFragment())
                        .commit();
                break;
            case 201:
                this.navController.showSettings(this);
                break;
            case 202:
                this.navController.startAppRating(this);
                break;
            case 203:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new DonateFragment())
                        .commit();
                break;
            case 204:
                this.navController.showChangeLog(this);
                break;
            case 205:
                this.navController.showEula(this);
                break;
            case 206:
                this.navController.showExitDialog(this);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        // See bug: http://stackoverflow.com/questions/13418436/android-4-2-back-stack-behaviour-with-nested-fragments/14030872#14030872
        // If the fragment exists and has some back-stack entry
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.content_frame);
        if (currentFragment != null && currentFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            // Get the fragment fragment manager - and pop the backstack
            currentFragment.getChildFragmentManager().popBackStack();
        }
        // Else, nothing in the direct fragment back stack
        else {
            if ( !NavigationController.HOME_FRAGMENT_TAG.equals(currentFragment.getTag())) {
                this.navController.goHomeFragment(this);
            }
            else {
                super.onBackPressed();
            }
        }
    }
}
