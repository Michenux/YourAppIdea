package org.michenux.drodrolib.ui.navdrawer;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class NavDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnDrawerItemClickListener mClickListener;

    private List<NavDrawerItem> items = new ArrayList<>();

    private SparseArray mViewHolderCreators = new SparseArray();

    private NavDrawerSimpleSelector mSelectedItem;

	public NavDrawerAdapter(OnDrawerItemClickListener itemClickListener ) {
		super();
        this.mClickListener = itemClickListener;
	}

    public void registerViewTypeCreator( NavDrawerViewTypeCreator viewTypeCreator ) {
        mViewHolderCreators.put(viewTypeCreator.getType(), viewTypeCreator);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NavDrawerViewTypeCreator viewTypeCreator = (NavDrawerViewTypeCreator) this.mViewHolderCreators.get(viewType);
        if ( viewTypeCreator == null ) {
            throw new IllegalStateException("A view type has not been registered on the adapter of the navigation drawer");
        }
        final RecyclerView.ViewHolder holder = viewTypeCreator.createViewHolder(parent, LayoutInflater.from(parent.getContext()));
        return holder;
    }

    public NavDrawerItem getItem(int position) {
        return this.items.get(position);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        NavDrawerItem menuItem = this.getItem(position);
        menuItem.onBindViewHolder(viewHolder, position);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.drawerItemClicked(position, v);
            }
        });

        if ( menuItem.isCheckable() && position == mSelectedItem.getSelectedItem()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                viewHolder.itemView.setActivated(true);
            }
            viewHolder.itemView.setSelected(true);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                viewHolder.itemView.setActivated(false);
            }
            viewHolder.itemView.setSelected(false);
        }
    }

    @Override
	public int getItemViewType(int position) {
	    return this.getItem(position).getType();
	}

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void clear() {
        this.items.clear();
    }

    public void addAll(List<NavDrawerItem> items) {
       this.items.addAll(items);
    }

    public void removeMenuItemWithId(long id) {
        for( int i = 0 ; i < getItemCount(); i++) {
            if ( id == getItemId(i)) {
                items.remove(getItem(i));
                break;
            }
        }
    }

    public void insert(NavDrawerItem item, int index) {
        this.items.add(index,item);
    }

    public void setSelectedItem(NavDrawerSimpleSelector selectedItem) {
        this.mSelectedItem = selectedItem;
    }

    public void setViewtypeCreators(SparseArray viewHolderCreators) {
        mViewHolderCreators = viewHolderCreators;
    }

    public static interface OnDrawerItemClickListener {
        public void drawerItemClicked(int position, View view);
    }
}
