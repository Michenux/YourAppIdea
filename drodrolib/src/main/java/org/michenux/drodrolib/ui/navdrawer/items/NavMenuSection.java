package org.michenux.drodrolib.ui.navdrawer.items;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.michenux.drodrolib.R;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerItem;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerViewTypeCreator;

public class NavMenuSection extends NavDrawerItem {

	public static final int SECTION_TYPE = NavMenuSection.class.hashCode();

	private int label;

    public NavMenuSection(int id) {
        super(id);
    }

    @Override
	public int getType() {
		return SECTION_TYPE;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	@Override
	public boolean updateActionBarTitle() {
		return false;
	}

    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        NavMenuSectionHolder vh = (NavMenuSectionHolder) viewHolder;
        vh.getLabelView().setText(getLabel());
    }

    private static class NavMenuSectionHolder extends RecyclerView.ViewHolder {
        private TextView mLabelView;

        public NavMenuSectionHolder(View itemView) {
            super(itemView);
            mLabelView = (TextView) itemView.findViewById(R.id.navmenusection_label);
        }

        public TextView getLabelView() {
            return mLabelView;
        }
    }

    public static class ViewHolderCreator implements NavDrawerViewTypeCreator {

        @Override
        public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, LayoutInflater inflater) {
            View view = inflater.inflate( R.layout.navdrawer_section, parent, false);
            NavMenuSectionHolder vh = new NavMenuSectionHolder(view);
            return vh;
        }

        @Override
        public int getType() {
            return SECTION_TYPE;
        }
    }
}
