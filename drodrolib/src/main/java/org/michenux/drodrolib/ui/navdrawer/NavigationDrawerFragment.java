package org.michenux.drodrolib.ui.navdrawer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.michenux.drodrolib.MCXApplication;

public abstract class NavigationDrawerFragment extends Fragment implements
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayoutView;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private NavDrawerActivityConfiguration mNavConf ;

    private Integer mSelectItemOnClosed ;

    private NavigationView mNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MCXApplication) getActivity().getApplication()).inject(this);
        setHasOptionsMenu(true);

        if ( savedInstanceState == null ) {
            mTitle = mDrawerTitle = this.getActivity().getTitle();
        }
        else {
            mTitle = savedInstanceState.getCharSequence("title");
            mDrawerTitle = savedInstanceState.getCharSequence("drawerTitle");
        }

        mNavConf = createNavigurationConfiguration();
    }

    protected abstract NavDrawerActivityConfiguration createNavigurationConfiguration();

    /**
     *
     * @param menuItemId menuItem id
     */
    protected abstract void onNavItemSelected( @IdRes int menuItemId );

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( mNavConf.getLayout(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavigationView = (NavigationView) view.findViewById(mNavConf.getNavigationViewId());
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        selectItem(menuItem.getItemId(), true);
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        mDrawerLayoutView = (DrawerLayout) activity.findViewById(mNavConf.getDrawerLayoutViewId());

        mDrawerToggle = new ActionBarDrawerToggle(
                this.getActivity(),
                mDrawerLayoutView,
                (Toolbar) activity.findViewById(mNavConf.getToolbarId()),
                mNavConf.getDrawerOpenDesc(),
                mNavConf.getDrawerCloseDesc()) {

            public void onDrawerClosed(View view) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mTitle);

                if ( NavigationDrawerFragment.this.mSelectItemOnClosed != null ) {
                    NavigationDrawerFragment.this.deferedOnNavItemSelected();
                }
            }

            public void onDrawerOpened(View drawerView) {
                getActivity().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayoutView.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Close drawer when back pressed
     * @return true if closeDrawer() is invoked.
     */
    public boolean onBackPressed() {
        if(getDrawerLayoutView().isDrawerOpen(GravityCompat.START)){
            getDrawerLayoutView().closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Select a item in the menu
     * @param menuItemId menu item id
     * @param deferred wait for drawer to close, then select item.
     */
    public void selectItem(@IdRes int menuItemId, boolean deferred) {

        MenuItem menuItem = mNavigationView.getMenu().findItem(menuItemId);

        if ( menuItem.isCheckable()) {
            mNavigationView.setCheckedItem(menuItemId);
        }

        if ( deferred ) {
            this.mSelectItemOnClosed = menuItemId;
        }
        else {
            onNavItemSelected(menuItemId);
        }

        if ( mNavConf.updateTitleWhenMenuItemClick(menuItemId)) {
            setTitle(menuItem.getTitle());
        }

        if ( mNavConf.closeDrawerWhenMenuItemClick(menuItemId) && this.mDrawerLayoutView.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayoutView.closeDrawer(GravityCompat.START);
        }
    }

    public void deferedOnNavItemSelected() {
        onNavItemSelected(this.mSelectItemOnClosed);
        mSelectItemOnClosed = null;
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mTitle);
    }

    public void setTitleWithDrawerTitle() {
        setTitle(mDrawerTitle);
    }

    public NavDrawerActivityConfiguration getNavConf() {
        return mNavConf;
    }

    public void setNavConf(NavDrawerActivityConfiguration mNavConf) {
        this.mNavConf = mNavConf;
    }

    protected DrawerLayout getDrawerLayoutView() {
        return mDrawerLayoutView;
    }

    protected ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("title", this.mTitle);
        outState.putCharSequence("drawerTitle", this.mDrawerTitle);
    }

    /**
     * Close or open drawer when hardware menu button is pressed.
     * @param keyCode key code
     * @param event key event
     * @return true if key event is consumed.
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            if ( this.mDrawerLayoutView.isDrawerOpen(GravityCompat.START)) {
                this.mDrawerLayoutView.closeDrawer(GravityCompat.START);
            }
            else {
                this.mDrawerLayoutView.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return false ;
    }

    public CharSequence getDrawerTitle() {
        return mDrawerTitle;
    }

    public void resetSelection() {
        for( int i = 0 ; i < mNavigationView.getMenu().size(); i++ ) {
            MenuItem menuItem = mNavigationView.getMenu().getItem(i);
            if ( menuItem.isChecked()) {
                menuItem.setChecked(false);
            }
        }
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }
}
