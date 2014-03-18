package org.michenux.drodrolib.ui.navdrawer;

import android.widget.BaseAdapter;

import org.michenux.drodrolib.R;

public class NavDrawerActivityConfiguration {

	private int mainLayout;
	private int drawerShadow;
	private int drawerLayoutId;
	private int leftDrawerId;
	private int[] actionMenuItemsToHideWhenDrawerOpen;
//	private NavDrawerItem[] navItems;
	private int drawerOpenDesc;
	private int drawerCloseDesc;
    private int drawerIcon ;
    private NavDrawerAdapter mAdapter;

    public boolean hasMenuItemWithId( long id ) {
        NavDrawerAdapter adapter = (NavDrawerAdapter) getAdapter();
        for( int i = 0 ; i < adapter.getCount(); i++) {
            if ( id == adapter.getItem(i).getId()) {
                return true;
            }
        }
        return false;
    }

    public void removeMenuItemWithId( long id ) {
        NavDrawerAdapter adapter = (NavDrawerAdapter) getAdapter();
        for( int i = 0 ; i < adapter.getCount(); i++) {
            if ( id == adapter.getItem(i).getId()) {
                adapter.remove(adapter.getItem(i));
                break;
            }
        }
    }

    public void addMenuItemAtIndex( NavDrawerItem item, int index ) {
        NavDrawerAdapter adapter = (NavDrawerAdapter) getAdapter();
        adapter.insert(item, index);
    }

	public int getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(int mainLayout) {
		this.mainLayout = mainLayout;
	}

	public int getDrawerShadow() {
		return drawerShadow;
	}

	public void setDrawerShadow(int drawerShadow) {
		this.drawerShadow = drawerShadow;
	}

	public int getDrawerLayoutId() {
		return drawerLayoutId;
	}

	public void setDrawerLayoutId(int drawerLayoutId) {
		this.drawerLayoutId = drawerLayoutId;
	}

	public int getLeftDrawerId() {
		return leftDrawerId;
	}

	public void setLeftDrawerId(int leftDrawerId) {
		this.leftDrawerId = leftDrawerId;
	}

	public int[] getActionMenuItemsToHideWhenDrawerOpen() {
		return actionMenuItemsToHideWhenDrawerOpen;
	}

	public void setActionMenuItemsToHideWhenDrawerOpen(
			int[] actionMenuItemsToHideWhenDrawerOpen) {
		this.actionMenuItemsToHideWhenDrawerOpen = actionMenuItemsToHideWhenDrawerOpen;
	}

//	public NavDrawerItem[] getNavItems() {
//		return navItems;
//	}

//	public void setNavItems(NavDrawerItem[] navItems) {
//		this.navItems = navItems;
//	}

	public int getDrawerOpenDesc() {
		return drawerOpenDesc;
	}

	public void setDrawerOpenDesc(int drawerOpenDesc) {
		this.drawerOpenDesc = drawerOpenDesc;
	}

	public int getDrawerCloseDesc() {
		return drawerCloseDesc;
	}

	public void setDrawerCloseDesc(int drawerCloseDesc) {
		this.drawerCloseDesc = drawerCloseDesc;
	}

	public NavDrawerAdapter getAdapter() {
		return mAdapter;
	}

	public void setAdapter(NavDrawerAdapter adapter) {
		this.mAdapter = adapter;
	}

    public int getDrawerIcon() {
        return drawerIcon;
    }

    public void setDrawerIcon(int drawerIcon) {
        this.drawerIcon = drawerIcon;
    }


    public static class Builder {

        private NavDrawerActivityConfiguration mConf = new NavDrawerActivityConfiguration();

        public Builder() {
            drawerOpenDesc(R.string.drawer_open);
            drawerCloseDesc(R.string.drawer_close);
            drawerShadow(R.drawable.drawer_shadow);
        }

        public Builder mainLayout(int mainLayout) {
            mConf.setMainLayout(mainLayout);
            return this;
        }

        public NavDrawerActivityConfiguration build() {
            return mConf;
        }

        public Builder drawerLayoutId(int drawerLayoutId) {
            mConf.setDrawerLayoutId(drawerLayoutId);
            return this;
        }

        public Builder leftDrawerId(int leftDrawerId) {
            mConf.setLeftDrawerId(leftDrawerId);
            return this;
        }

//        public Builder menu(NavDrawerItem menuitems[]) {
//            mConf.setNavItems(menuitems);
//            return this;
//        }

        public Builder drawerShadow(int drawerShadowId) {
            mConf.setDrawerShadow(drawerShadowId);
            return this;
        }

        public Builder drawerOpenDesc(int drawerOpenId) {
            mConf.setDrawerOpenDesc(drawerOpenId);
            return this;
        }

        public Builder drawerCloseDesc(int drawerCloseId) {
            mConf.setDrawerCloseDesc(drawerCloseId);
            return this;
        }

        public Builder adapter( NavDrawerAdapter adapter) {
            mConf.setAdapter(adapter);
            return this;
        }

        public Builder drawerIcon(int icDrawer) {
            mConf.setDrawerIcon(icDrawer);
            return this;
        }
    }
}
