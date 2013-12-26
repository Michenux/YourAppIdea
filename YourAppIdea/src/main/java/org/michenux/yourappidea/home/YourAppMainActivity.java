package org.michenux.yourappidea.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.michenux.android.ui.navdrawer.AbstractNavDrawerActivity;
import org.michenux.android.ui.navdrawer.NavDrawerActivityConfiguration;
import org.michenux.android.ui.navdrawer.NavDrawerAdapter;
import org.michenux.android.ui.navdrawer.NavDrawerItem;
import org.michenux.android.ui.navdrawer.NavMenuBuilder;
import org.michenux.yourappidea.NavigationController;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.airport.AirportListFragment;
import org.michenux.yourappidea.aroundme.AroundMeFragment;
import org.michenux.yourappidea.donations.DonateFragment;
import org.michenux.yourappidea.friends.FriendMainFragment;
import org.michenux.yourappidea.map.SimpleMapFragment;
import org.michenux.yourappidea.tutorial.TutorialListFragment;

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
            this.navController.showWhatsNew(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavDrawerItem[] menu = new NavMenuBuilder()
                .addSection(100, R.string.navdrawer_demos)
                .addSectionItem(104, R.string.navdrawer_tutorial, R.drawable.navdrawer_tutorial, true, true)
                .addSectionItem(101, R.string.navdrawer_listdetail,  R.drawable.navdrawer_friends, true, true)
                .addSectionItem(102, R.string.navdrawer_airport, R.drawable.navdrawer_airport, true, true)
                .addSectionItem(103, R.string.navdrawer_simplemap, R.drawable.navdrawer_map, true, true)
                .addSectionItem(105, R.string.navdrawer_aroundme, R.drawable.navdrawer_aroundme, true, true)
                .addSection(200, R.string.navdrawer_general)
                .addSectionItem(201, R.string.navdrawer_settings, R.drawable.navdrawer_settings, true, true)
                .addSectionItem(202, R.string.navdrawer_rating, R.drawable.navdrawer_rating, false, false)
                .addSectionItem(203, R.string.navdrawer_donations, R.drawable.navdrawer_donations, true, true)
                .addSectionItem(204, R.string.navdrawer_changelog, R.drawable.navdrawer_changelog, false, false)
                .addSectionItem(205, R.string.navdrawer_eula, R.drawable.navdrawer_eula, false, false)
                .build();

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration.Builder()
                .mainLayout(R.layout.main)
                .drawerLayoutId(R.id.drawer_layout)
                .leftDrawerId(R.id.left_drawer)
                .menu(menu)
                .drawerShadow(R.drawable.drawer_shadow)
                .drawerOpenDesc(R.string.drawer_open)
                .drawerCloseDesc(R.string.drawer_close)
                .adapter(new NavDrawerAdapter(this, R.layout.navdrawer_item, menu))
                .drawerIcon(R.drawable.ic_drawer)
                .build();

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
                        .replace(R.id.content_frame, new AirportListFragment())
                        .commit();
                break;
            case 103:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new SimpleMapFragment())
                        .commit();
                break;
            case 104:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new TutorialListFragment())
                        .commit();
                break;
            case 105:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AroundMeFragment())
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
