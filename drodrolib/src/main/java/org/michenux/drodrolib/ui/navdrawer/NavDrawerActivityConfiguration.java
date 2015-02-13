package org.michenux.drodrolib.ui.navdrawer;

import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.SparseArray;

import org.michenux.drodrolib.R;

public class NavDrawerActivityConfiguration {

    private int layout;
    private int toolbarId;
	private int drawerLayoutId;
	private int leftDrawerId;
    private int recyclerViewId;
	private int[] actionMenuItemsToHideWhenDrawerOpen;
	private int drawerOpenDesc;
	private int drawerCloseDesc;
    private SparseArray viewHolderCreators = new SparseArray();

	public @IdRes int getDrawerLayoutViewId() {
		return drawerLayoutId;
	}

	public void setDrawerLayoutId( @IdRes int drawerLayoutId) {
		this.drawerLayoutId = drawerLayoutId;
	}

	public int getLeftDrawerId() {
		return leftDrawerId;
	}

	public void setLeftDrawerId( @IdRes int leftDrawerId) {
		this.leftDrawerId = leftDrawerId;
	}

	public int[] getActionMenuItemsToHideWhenDrawerOpen() {
		return actionMenuItemsToHideWhenDrawerOpen;
	}

	public void setActionMenuItemsToHideWhenDrawerOpen(
			int[] actionMenuItemsToHideWhenDrawerOpen) {
		this.actionMenuItemsToHideWhenDrawerOpen = actionMenuItemsToHideWhenDrawerOpen;
	}

	public int getDrawerOpenDesc() {
		return drawerOpenDesc;
	}

	public void setDrawerOpenDesc( @StringRes int drawerOpenDesc) {
		this.drawerOpenDesc = drawerOpenDesc;
	}

	public int getDrawerCloseDesc() {
		return drawerCloseDesc;
	}

	public void setDrawerCloseDesc( @StringRes int drawerCloseDesc) {
		this.drawerCloseDesc = drawerCloseDesc;
	}

    public int getToolbarId() {
        return toolbarId;
    }

    public void setToolbarId( @IdRes int toolbarId) {
        this.toolbarId = toolbarId;
    }

    public int getRecyclerViewId() {
        return recyclerViewId;
    }

    public void setRecyclerViewId(int recyclerViewId) {
        this.recyclerViewId = recyclerViewId;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout( @LayoutRes int layout) {
        this.layout = layout;
    }

    public SparseArray getViewHolderCreators() {
        return viewHolderCreators;
    }

    public static class Builder {

        private NavDrawerActivityConfiguration mConf = new NavDrawerActivityConfiguration();

        public Builder() {
            drawerOpenDesc(R.string.drawer_open);
            drawerCloseDesc(R.string.drawer_close);
        }

        public Builder toolbarId( @IdRes int toolbarId) {
            mConf.setToolbarId(toolbarId);
            return this;
        }

        public NavDrawerActivityConfiguration build() {
            return mConf;
        }

        public Builder drawerLayoutId( @IdRes int drawerLayoutId) {
            mConf.setDrawerLayoutId(drawerLayoutId);
            return this;
        }

        public Builder leftDrawerId( @IdRes int leftDrawerId) {
            mConf.setLeftDrawerId(leftDrawerId);
            return this;
        }

        public Builder recyclerViewId( @IdRes int recyclerViewId) {
            mConf.setRecyclerViewId(recyclerViewId);
            return this;
        }

        public Builder drawerOpenDesc( @StringRes int drawerOpenDesc) {
            mConf.setDrawerOpenDesc(drawerOpenDesc);
            return this;
        }

        public Builder drawerCloseDesc( @StringRes int drawerCloseId) {
            mConf.setDrawerCloseDesc(drawerCloseId);
            return this;
        }

        public Builder layout(@LayoutRes int layout) {
            mConf.setLayout(layout);
            return this;
        }

        public Builder registerViewTypeCreator( NavDrawerViewTypeCreator viewTypeCreator ) {
            mConf.getViewHolderCreators().put(viewTypeCreator.getType(), viewTypeCreator);
            return this;
        }
    }
}
