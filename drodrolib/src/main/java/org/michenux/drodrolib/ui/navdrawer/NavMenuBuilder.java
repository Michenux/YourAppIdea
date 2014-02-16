package org.michenux.drodrolib.ui.navdrawer;

import java.util.ArrayList;
import java.util.List;

public class NavMenuBuilder {

    private List<NavDrawerItem> mMenu = new ArrayList<>();

    public NavMenuBuilder addSection( int id, int label ) {
        NavMenuSection section = new NavMenuSection();
        section.setLabel(label);
        mMenu.add(section);
        return this;
    }

    public NavMenuBuilder addSectionItem(  int id, int label, int icon, boolean updateActionBarTitle, boolean checkable ) {
        NavMenuItem item = new NavMenuItem();
        item.setId(id);
        item.setLabel(label);
        item.setIcon(icon);
        item.setUpdateActionBarTitle(updateActionBarTitle);
        item.setCheckable(checkable);
        mMenu.add(item);
        return this;
    }

    public NavMenuBuilder addCustomItem( NavDrawerCustomItem customItem ) {
        mMenu.add(customItem);
        return this;
    }

    public NavDrawerItem[] build() {
        return mMenu.toArray(new NavDrawerItem[mMenu.size()]);
    }
}