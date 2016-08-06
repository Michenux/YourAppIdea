package org.michenux.drodrolib.ui.navdrawer;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.SparseArray;

import org.michenux.drodrolib.R;

public class NavDrawerActivityConfiguration {
    private int layout;
    private int toolbarId;
    private int drawerLayoutId;
    private int drawerOpenDesc;
    private int drawerCloseDesc;
    private SparseArray closeDrawerWhenMenuItemClicked = new SparseArray();
    private SparseArray updateTitleWhenMenuItemClicked = new SparseArray();

    private int navigationViewId;

    public
    @IdRes
    int getDrawerLayoutViewId() {
        return drawerLayoutId;
    }

    public void setDrawerLayoutId(@IdRes int drawerLayoutId) {
        this.drawerLayoutId = drawerLayoutId;
    }

    public int getDrawerOpenDesc() {
        return drawerOpenDesc;
    }

    public void setDrawerOpenDesc(@StringRes int drawerOpenDesc) {
        this.drawerOpenDesc = drawerOpenDesc;
    }

    public int getDrawerCloseDesc() {
        return drawerCloseDesc;
    }

    public void setDrawerCloseDesc(@StringRes int drawerCloseDesc) {
        this.drawerCloseDesc = drawerCloseDesc;
    }

    public int getToolbarId() {
        return toolbarId;
    }

    public void setToolbarId(@IdRes int toolbarId) {
        this.toolbarId = toolbarId;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(@LayoutRes int layout) {
        this.layout = layout;
    }

    public int getNavigationViewId() {
        return this.navigationViewId;
    }

    public void setNavigationViewId(int navigationViewId) {
        this.navigationViewId = navigationViewId;
    }

    public void setDontCloseDrawerWhenMenuItemClick(@IdRes int menuItemId) {
        closeDrawerWhenMenuItemClicked.put(menuItemId, false);
    }

    public boolean closeDrawerWhenMenuItemClick(@IdRes int menuItemId) {
        return (Boolean) closeDrawerWhenMenuItemClicked.get(menuItemId, Boolean.TRUE);
    }

    public void setUpdateTitleWhenMenuItemClick(@IdRes int menuItemId) {
        updateTitleWhenMenuItemClicked.put(menuItemId, Boolean.TRUE);
    }

    public boolean updateTitleWhenMenuItemClick(@IdRes int menuItemId) {
        return (Boolean) updateTitleWhenMenuItemClicked.get(menuItemId, Boolean.FALSE);
    }

    public static class Builder {
        private NavDrawerActivityConfiguration mConf = new NavDrawerActivityConfiguration();

        public Builder() {
            drawerOpenDesc(R.string.drawer_open);
            drawerCloseDesc(R.string.drawer_close);
        }

        public Builder toolbarId(@IdRes int toolbarId) {
            mConf.setToolbarId(toolbarId);
            return this;
        }

        public NavDrawerActivityConfiguration build() {
            return mConf;
        }

        public Builder navigationViewId(@IdRes int navigationViewId) {
            mConf.setNavigationViewId(navigationViewId);
            return this;
        }

        public Builder drawerLayoutId(@IdRes int drawerLayoutId) {
            mConf.setDrawerLayoutId(drawerLayoutId);
            return this;
        }

        public Builder drawerOpenDesc(@StringRes int drawerOpenDesc) {
            mConf.setDrawerOpenDesc(drawerOpenDesc);
            return this;
        }

        public Builder drawerCloseDesc(@StringRes int drawerCloseId) {
            mConf.setDrawerCloseDesc(drawerCloseId);
            return this;
        }

        public Builder layout(@LayoutRes int layout) {
            mConf.setLayout(layout);
            return this;
        }

        public Builder dontCloseDrawerWhenMenuItemClick(@IdRes int menuItemId) {
            mConf.setDontCloseDrawerWhenMenuItemClick(menuItemId);
            return this;
        }

        public Builder titleUpdatedWhenMenuItemClicked(@IdRes int menuItemId) {
            mConf.setUpdateTitleWhenMenuItemClick(menuItemId);
            return this;
        }
    }
}
