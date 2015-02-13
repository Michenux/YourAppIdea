package org.michenux.drodrolib.ui.navdrawer.items;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.michenux.drodrolib.R;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerItem;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerViewTypeCreator;

public class NavMenuHeader extends NavDrawerItem {

    public static final int ITEM_TYPE = NavMenuHeader.class.hashCode();

    private int mBackgroundRes;

    public static NavMenuHeader createMenuItem( int id, @DrawableRes int backgroundRes ) {
        return new NavMenuHeader(id, backgroundRes);
    }

    public NavMenuHeader(int id, @DrawableRes int mBackgroundRes) {
        super(id);
        this.mBackgroundRes = mBackgroundRes;
    }

    public int getLabel() {
        // not used
        return 0;
    }

    @Override
    public int getType() {
        return ITEM_TYPE;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    public boolean isCheckable() {
        return false;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        viewHolder.itemView.setBackgroundResource(this.mBackgroundRes);
    }

    private static class NavMenuHeaderHolder extends RecyclerView.ViewHolder {

        public NavMenuHeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ViewHolderCreator implements NavDrawerViewTypeCreator {

        @Override
        public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, LayoutInflater inflater) {
            View view = inflater.inflate( R.layout.navdrawer_header, parent, false);
            NavMenuHeaderHolder vh = new NavMenuHeaderHolder(view);
            return vh;
        }

        @Override
        public int getType() {
            return ITEM_TYPE;
        }
    }
}
