package org.michenux.drodrolib.ui.navdrawer.items;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.michenux.drodrolib.R;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerItem;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerViewTypeCreator;

public class NavMenuLabelWithIcon extends NavDrawerItem {

	public static final int ITEM_TYPE = NavMenuLabelWithIcon.class.hashCode();

	private int mLabel ;
	
	private int mIcon ;
	
	private boolean mUpdateActionBarTitle ;

    private boolean mCheckable ;

    public static NavMenuLabelWithIcon createMenuItem( int id, @StringRes int label, @DrawableRes int icon, boolean updateActionBarTitle, boolean checkable ) {
        return new NavMenuLabelWithIcon(id, label, icon, updateActionBarTitle, checkable);
    }

    public NavMenuLabelWithIcon(int id, int label, int icon, boolean updateActionBarTitle, boolean checkable) {
        super(id);
        this.mIcon = icon;
        this.mLabel = label;
        this.mUpdateActionBarTitle = updateActionBarTitle;
        this.mCheckable = checkable;
    }

	@Override
	public int getType() {
		return ITEM_TYPE;
	}

	public int getLabel() {
		return mLabel;
	}

	public void setLabel(int label) {
		this.mLabel = label;
	}

	public int getIcon() {
		return mIcon;
	}

	public void setIcon(int icon) {
		this.mIcon = icon;
	}

	@Override
	public boolean updateActionBarTitle() {
		return this.mUpdateActionBarTitle;
	}

	public void setUpdateActionBarTitle(boolean updateActionBarTitle) {
		this.mUpdateActionBarTitle = updateActionBarTitle;
	}

    public boolean isCheckable() {
        return mCheckable;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        NavMenuLabelWithIconHolder vh = (NavMenuLabelWithIconHolder) viewHolder;
        vh.getLabelView().setText(getLabel());
        vh.getIconView().setImageResource(getIcon());
    }

    public void setCheckable(boolean checkable) {
        this.mCheckable = checkable;
    }

    private static class NavMenuLabelWithIconHolder extends RecyclerView.ViewHolder {
        private TextView mLabelView;
        private ImageView mIconView;

        public NavMenuLabelWithIconHolder(View itemView) {
            super(itemView);
            mLabelView = (TextView) itemView.findViewById(R.id.navmenuitem_label);
            mIconView = (ImageView) itemView.findViewById(R.id.navmenuitem_icon);
        }

        public TextView getLabelView() {
            return mLabelView;
        }

        public ImageView getIconView() {
            return mIconView;
        }
    }

    public static class ViewHolderCreator implements NavDrawerViewTypeCreator {

        @Override
        public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, LayoutInflater inflater) {
            View view = inflater.inflate( R.layout.navdrawer_labelicon, parent, false);
            NavMenuLabelWithIconHolder vh = new NavMenuLabelWithIconHolder(view);
            return vh;
        }

        @Override
        public int getType() {
            return ITEM_TYPE;
        }
    }
}
