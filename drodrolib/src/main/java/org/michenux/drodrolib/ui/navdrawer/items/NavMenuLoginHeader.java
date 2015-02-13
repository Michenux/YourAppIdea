package org.michenux.drodrolib.ui.navdrawer.items;

import android.media.Image;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.michenux.drodrolib.R;
import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerItem;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerViewTypeCreator;
import org.michenux.drodrolib.ui.navdrawer.NavdrawerHeaderArrowView;

public class NavMenuLoginHeader extends NavDrawerItem {
    public static final int ITEM_TYPE = NavMenuLoginHeader.class.hashCode();

    private int mBackgroundRes;

    private UserHelper mUserHelper;

    private OnHeaderProfileMenuListener mProfileMenuListener;

    private boolean mArrowOpened = false;

    public static NavMenuLoginHeader createMenuItem( int id, @DrawableRes int backgroundRes, UserHelper userHelper, OnHeaderProfileMenuListener profileMenuListener ) {
        return new NavMenuLoginHeader(id, backgroundRes, userHelper, profileMenuListener);
    }

    public NavMenuLoginHeader(int id, @DrawableRes int mBackgroundRes, UserHelper userHelper, OnHeaderProfileMenuListener profileMenuListener) {
        super(id);
        this.mBackgroundRes = mBackgroundRes;
        this.mUserHelper = userHelper;
        this.mProfileMenuListener = profileMenuListener;
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
    public boolean closeDrawerOnClick() {
        return false;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        NavMenuLoginHeaderHolder vh = (NavMenuLoginHeaderHolder) viewHolder;
        vh.itemView.setBackgroundResource(this.mBackgroundRes);

        if ( mUserHelper.getCurrentUser() == null ) {
            vh.getLoginView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NavMenuLoginHeader.this.mProfileMenuListener != null) {
                        NavMenuLoginHeader.this.mProfileMenuListener.doLogin();
                    }
                }
            });

            vh.getConnectedViewGroup().setVisibility(View.GONE);
            vh.getDisconnectedViewGroup().setVisibility(View.VISIBLE);
            vh.getArrowView().setOnClickListener(null);
        }
        else {
            vh.getNameView().setText(mUserHelper.getCurrentUser().getDisplayName());
            vh.getMailView().setText(mUserHelper.getCurrentUser().getMail());
            vh.getLoginView().setOnClickListener(null);
            vh.getArrowView().setExpanded(mArrowOpened);
            vh.getArrowView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavdrawerHeaderArrowView view = (NavdrawerHeaderArrowView) v;
                    mArrowOpened = view.switchExpandedState();
                    if (NavMenuLoginHeader.this.mProfileMenuListener != null) {
                        if (view.isExpanded()) {
                            NavMenuLoginHeader.this.mProfileMenuListener.onOpen();
                        } else {
                            NavMenuLoginHeader.this.mProfileMenuListener.onClose();
                        }
                    }
                }
            });

            vh.getConnectedViewGroup().setVisibility(View.VISIBLE);
            vh.getDisconnectedViewGroup().setVisibility(View.GONE);
        }
    }

    private static class NavMenuLoginHeaderHolder extends RecyclerView.ViewHolder {

        private ViewGroup connectedViewGroup;
        private TextView nameView;
        private TextView mailView;
        private NavdrawerHeaderArrowView arrowView ;
        private ViewGroup disconnectedViewGroup;
        private TextView loginView;

        public NavMenuLoginHeaderHolder(View itemView) {
            super(itemView);
            connectedViewGroup = (ViewGroup) itemView.findViewById(R.id.nadrawer_loginheader_connected_viewgroup);
            nameView = (TextView) itemView.findViewById(R.id.nadrawer_loginheader_name);
            mailView = (TextView) itemView.findViewById(R.id.nadrawer_loginheader_email);
            arrowView = (NavdrawerHeaderArrowView) itemView.findViewById(R.id.navdrawer_loginheader_arrow);
            disconnectedViewGroup = (ViewGroup) itemView.findViewById(R.id.nadrawer_loginheader_disconnected_viewgroup);
            loginView = (TextView) itemView.findViewById(R.id.nadrawer_loginheader_login);
        }

        public NavdrawerHeaderArrowView getArrowView() {
            return arrowView;
        }

        public ViewGroup getConnectedViewGroup() {
            return connectedViewGroup;
        }

        public TextView getNameView() {
            return nameView;
        }

        public TextView getMailView() {
            return mailView;
        }

        public ViewGroup getDisconnectedViewGroup() {
            return disconnectedViewGroup;
        }

        public TextView getLoginView() {
            return loginView;
        }
    }

    public void setArrowOpened(boolean arrowOpened) {
        this.mArrowOpened = arrowOpened;
    }

    public static class ViewHolderCreator implements NavDrawerViewTypeCreator {

        @Override
        public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, LayoutInflater inflater) {
            View view = inflater.inflate( R.layout.navdrawer_loginheader, parent, false);
            NavMenuLoginHeaderHolder vh = new NavMenuLoginHeaderHolder(view);
            return vh;
        }

        @Override
        public int getType() {
            return ITEM_TYPE;
        }
    }

    public static interface OnHeaderProfileMenuListener {

        public void doLogin();

        public void onOpen();

        public void onClose();
    }
}
