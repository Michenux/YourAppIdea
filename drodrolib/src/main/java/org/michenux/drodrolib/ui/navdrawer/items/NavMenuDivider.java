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

public class NavMenuDivider extends NavDrawerItem {

    public static final int ITEM_TYPE = NavMenuDivider.class.hashCode();

    public static NavMenuDivider createMenuItem( int id ) {
        return new NavMenuDivider(id);
    }

    public NavMenuDivider(int id) {
        super(id);
    }

    @Override
    public int getType() {
        return ITEM_TYPE;
    }

    @Override
    public int getLabel() {
        //not used
        return 0;
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
    }

    private static class NavMenuDividerHolder extends RecyclerView.ViewHolder {

        public NavMenuDividerHolder(View itemView) {
            super(itemView);
       }
    }

    public static class ViewHolderCreator implements NavDrawerViewTypeCreator {

        @Override
        public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, LayoutInflater inflater) {
            View view = inflater.inflate( R.layout.navdrawer_divider, parent, false);
            NavMenuDividerHolder vh = new NavMenuDividerHolder(view);
            return vh;
        }

        @Override
        public int getType() {
            return ITEM_TYPE;
        }
    }
}
