package org.michenux.drodrolib.ui.navdrawer;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.michenux.drodrolib.MCXApplication;

import java.util.List;

public abstract class NavigationDrawerFragment extends Fragment implements NavDrawerAdapter.OnDrawerItemClickListener {

    private DrawerLayout mDrawerLayoutView;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private NavDrawerActivityConfiguration mNavConf ;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private NavDrawerSimpleSelector mSelectedItem ;

    private NavDrawerAdapter mAdapter;

    private Integer mSelectItemOnClosed ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MCXApplication) getActivity().getApplication()).inject(this);
        setHasOptionsMenu(true);

        if ( savedInstanceState == null ) {
            mTitle = mDrawerTitle = this.getActivity().getTitle();
            mSelectedItem = new NavDrawerSimpleSelector();
        }
        else {
            mTitle = savedInstanceState.getCharSequence("title");
            mDrawerTitle = savedInstanceState.getCharSequence("drawerTitle");
            mSelectedItem = savedInstanceState.getParcelable("mLastItemChecked");
        }

        mAdapter = new NavDrawerAdapter(this);
        mAdapter.setSelectedItem(this.mSelectedItem);

        mNavConf = createNavigurationConfiguration();
        mAdapter.setViewtypeCreators(mNavConf.getViewHolderCreators());
    }

    protected abstract NavDrawerActivityConfiguration createNavigurationConfiguration();

    protected abstract void onNavItemSelected( int id );

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( mNavConf.getLayout(), container, false);
        mRecyclerView = (RecyclerView) view.findViewById(mNavConf.getRecyclerViewId());
        mRecyclerView.setAdapter(mAdapter);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrawerLayoutView = (DrawerLayout) getActivity().findViewById(mNavConf.getDrawerLayoutViewId());
        mDrawerToggle = new ActionBarDrawerToggle(
                this.getActivity(),
                mDrawerLayoutView,
                (Toolbar) getActivity().findViewById(mNavConf.getToolbarId()),
                mNavConf.getDrawerOpenDesc(),
                mNavConf.getDrawerCloseDesc()
        ) {
            public void onDrawerClosed(View view) {
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(mTitle);
                ActivityCompat.invalidateOptionsMenu(NavigationDrawerFragment.this.getActivity());

                if ( NavigationDrawerFragment.this.mSelectItemOnClosed != null ) {
                    NavigationDrawerFragment.this.deferedOnNavItemSelected();
                }
            }

            public void onDrawerOpened(View drawerView) {
                ((ActionBarActivity)getActivity()).setTitle(mDrawerTitle);
                ActivityCompat.invalidateOptionsMenu(NavigationDrawerFragment.this.getActivity());
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

    public boolean onBackPressed() {
        if(getDrawerLayoutView().isDrawerOpen(Gravity.LEFT)){
            getDrawerLayoutView().closeDrawer(Gravity.LEFT);
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void highlightSelectedItem( int newPosition, int oldPosition ) {
        if ( oldPosition != 0 ) {
            RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForPosition(oldPosition);
            if ( vh != null ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    vh.itemView.setActivated(false);
                }
                vh.itemView.setSelected(false);
            }
        }

        RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForPosition(newPosition);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            vh.itemView.setActivated(true);
        }
        vh.itemView.setSelected(true);
    }

    public void selectItem(int position, boolean deferred) {
        NavDrawerItem selectedItem = mAdapter.getItem(position);

        if ( selectedItem.isCheckable()) {
            this.highlightSelectedItem(position, this.mSelectedItem.getSelectedItem());
            this.mSelectedItem.setSelectedItem(position);
        }

        if ( deferred ) {
            this.mSelectItemOnClosed = position;
        }
        else {
            onNavItemSelected(selectedItem.getId());
        }

        if ( selectedItem.updateActionBarTitle()) {
            setTitle(getString(selectedItem.getLabel()));
        }

        if ( selectedItem.closeDrawerOnClick() && this.mDrawerLayoutView.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayoutView.closeDrawer(Gravity.LEFT);
        }
    }

    public void deferedOnNavItemSelected() {
        NavDrawerItem selectedItem = mAdapter.getItem(this.mSelectItemOnClosed);
        onNavItemSelected(selectedItem.getId());
        mSelectItemOnClosed = null;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if ( mNavConf.getActionMenuItemsToHideWhenDrawerOpen() != null ) {
            boolean drawerOpen = mDrawerLayoutView.isDrawerOpen(Gravity.LEFT);
            for( int iItem : mNavConf.getActionMenuItemsToHideWhenDrawerOpen()) {
                menu.findItem(iItem).setVisible(!drawerOpen);
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void drawerItemClicked(int position, View view) {
        this.selectItem(position, true);
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

    public NavDrawerSimpleSelector getSelectedItem() {
        return mSelectedItem;
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
        outState.putParcelable("mLastItemChecked", this.mSelectedItem);
    }

    public void updateMenu( List<NavDrawerItem> menu ) {
        this.mAdapter.clear();
        this.mAdapter.addAll(menu);
        this.mAdapter.notifyDataSetChanged();
    }

    public void refreshMenu() {
        this.mAdapter.notifyDataSetChanged();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            if ( this.mDrawerLayoutView.isDrawerOpen(Gravity.LEFT)) {
                this.mDrawerLayoutView.closeDrawer(Gravity.LEFT);
            }
            else {
                this.mDrawerLayoutView.openDrawer(Gravity.LEFT);
            }
            return true;
        }
        return false ;
    }

    public boolean hasMenuItemWithId( long id ) {
        for( int i = 0 ; i < mAdapter.getItemCount(); i++) {
            if ( id == mAdapter.getItem(i).getId()) {
                return true;
            }
        }
        return false;
    }

    public void removeMenuItemWithId( long id ) {
        mAdapter.removeMenuItemWithId(id);
    }

    public void addMenuItemAtIndex( NavDrawerItem item, int index ) {
        mAdapter.insert(item, index);
    }

    public void resetSelection() {
        if ( mSelectedItem.hasSelection()) {

            // unmark old selection
            RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForPosition(mSelectedItem.getSelectedItem());
            if ( vh != null ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    vh.itemView.setActivated(false);
                }
                vh.itemView.setSelected(false);
            }

            // reset selection
            mSelectedItem.resetSelection();
        }
    }

    public CharSequence getDrawerTitle() {
        return mDrawerTitle;
    }
}
