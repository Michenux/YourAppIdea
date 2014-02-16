package org.michenux.drodrolib.ui.navdrawer;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.michenux.drodrolib.MCXApplication;
import org.michenux.drodrolib.R;

public class NavDrawerAdapter extends ArrayAdapter<NavDrawerItem> {

	private LayoutInflater inflater;
	
	public NavDrawerAdapter(Context context, int textViewResourceId, NavDrawerItem[] objects ) {
		super(context, textViewResourceId, objects);
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null ;
		NavDrawerItem menuItem = this.getItem(position);

        switch (menuItem.getType()) {
            case NavMenuItem.ITEM_TYPE:
                view = getItemView(convertView, parent, menuItem );
                break;
            case NavMenuSection.SECTION_TYPE:
                view = getSectionView(convertView, parent, menuItem);
                break;
        }

        if ( menuItem instanceof NavDrawerCustomItem ) {
            NavDrawerCustomItem customItem = (NavDrawerCustomItem) menuItem;
            view = customItem.getCustomView(convertView, parent, customItem, inflater);
        }

		return view ;
	}
	
	public View getItemView( View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem ) {
		
		NavMenuItem menuItem = (NavMenuItem) navDrawerItem ;
		NavMenuItemHolder navMenuItemHolder = null;
		
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.navdrawer_item, parentView, false);
			TextView labelView = (TextView) convertView
					.findViewById( R.id.navmenuitem_label );
			ImageView iconView = (ImageView) convertView
					.findViewById( R.id.navmenuitem_icon );

			navMenuItemHolder = new NavMenuItemHolder();
			navMenuItemHolder.labelView = labelView ;
			navMenuItemHolder.iconView = iconView ;

			convertView.setTag(navMenuItemHolder);
		}

		if ( navMenuItemHolder == null ) {
			navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
		}
					
		navMenuItemHolder.labelView.setText(menuItem.getLabel());
		navMenuItemHolder.iconView.setImageResource(menuItem.getIcon());
		
		return convertView ;
	}

	public View getSectionView(View convertView, ViewGroup parentView,
			NavDrawerItem navDrawerItem) {
		
		NavMenuSection menuSection = (NavMenuSection) navDrawerItem ;
		NavMenuSectionHolder navMenuItemHolder = null;
		
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.navdrawer_section, parentView, false);
			TextView labelView = (TextView) convertView
					.findViewById( R.id.navmenusection_label );

			navMenuItemHolder = new NavMenuSectionHolder();
			navMenuItemHolder.labelView = labelView ;
			convertView.setTag(navMenuItemHolder);
		}

		if ( navMenuItemHolder == null ) {
			navMenuItemHolder = (NavMenuSectionHolder) convertView.getTag();
		}
					
		navMenuItemHolder.labelView.setText(menuSection.getLabel());
		
		return convertView ;
	}
	
	@Override
	public int getViewTypeCount() {
        SparseIntArray types = new SparseIntArray();
        for( int i = 0 ; i < this.getCount(); i++ ) {
            int itemType = getItemViewType(i);
            if ( types.get(itemType, -1) == -1 ) {
                types.put(itemType, itemType);
            }
        }
	    return types.size();
	}
	
	@Override
	public int getItemViewType(int position) {
	    return this.getItem(position).getType();
	}

	private static class NavMenuItemHolder {
		private TextView labelView;
		private ImageView iconView;
	}
	
	private class NavMenuSectionHolder {
		private TextView labelView;
	}
}
