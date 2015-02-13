package org.michenux.drodrolib.ui.navdrawer;

import android.support.annotation.DrawableRes;
import android.view.View;

import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.ui.navdrawer.items.NavMenuDivider;
import org.michenux.drodrolib.ui.navdrawer.items.NavMenuHeader;
import org.michenux.drodrolib.ui.navdrawer.items.NavMenuLabelWithIcon;
import org.michenux.drodrolib.ui.navdrawer.items.NavMenuLoginHeader;
import org.michenux.drodrolib.ui.navdrawer.items.NavMenuSection;

import java.util.ArrayList;
import java.util.List;

public class NavMenuBuilder {

    private List<NavDrawerItem> mMenu = new ArrayList<>();

    public NavMenuBuilder addSection( int id, int label ) {
        NavMenuSection section = new NavMenuSection(id);
        section.setLabel(label);
        mMenu.add(section);
        return this;
    }

    public NavMenuBuilder addLabelWithIcon(int id, int label, int icon, boolean updateActionBarTitle, boolean checkable) {
        mMenu.add(NavMenuLabelWithIcon.createMenuItem(id, label, icon, updateActionBarTitle, checkable));
        return this;
    }

    public NavMenuBuilder addLabelWithIconAtIndex(int id, int label, int icon, boolean updateActionBarTitle, boolean checkable, int index) {
        mMenu.add(index, NavMenuLabelWithIcon.createMenuItem(id, label, icon, updateActionBarTitle, checkable));
        return this;
    }

    public NavMenuBuilder addHeader( int id, @DrawableRes int backgroundRes ) {
        mMenu.add(NavMenuHeader.createMenuItem(id, backgroundRes));
        return this;
    }

    public NavMenuBuilder addLoginHeader( int id, @DrawableRes int backgroundRes, UserHelper userHelper, NavMenuLoginHeader.OnHeaderProfileMenuListener arrowClickListener ) {
        mMenu.add(NavMenuLoginHeader.createMenuItem(id, backgroundRes, userHelper, arrowClickListener));
        return this;
    }

    public NavMenuBuilder addDrawerItem( NavDrawerItem customItem ) {
        mMenu.add(customItem);
        return this;
    }

    public NavMenuBuilder addDrawerItemAtIndex( NavDrawerItem customItem, int index ) {
        mMenu.add(index, customItem);
        return this;
    }

    public NavMenuBuilder addDivider( int id  ) {
        mMenu.add(NavMenuDivider.createMenuItem(id));
        return this;
    }

    public List<NavDrawerItem> build() {
        return mMenu;
    }
}