package org.michenux.drodrolib.ui.navdrawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class NavDrawerAdapter extends ArrayAdapter<NavDrawerItem> {

	private LayoutInflater inflater;

	public NavDrawerAdapter(Context context, int textViewResourceId ) {
		super(context, textViewResourceId);
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null ;
		NavDrawerItem menuItem = this.getItem(position);
        view = menuItem.getView(convertView, parent, menuItem, inflater);
		return view ;
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


    public void setData(NavDrawerItem[] data) {
        clear();
        if (data != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                addAllHoneyComb(data);
            }
            else {
                for(NavDrawerItem item: data){
                    super.add(item);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void addAllHoneyComb(NavDrawerItem... items) {
        super.addAll(items);
    }
}
