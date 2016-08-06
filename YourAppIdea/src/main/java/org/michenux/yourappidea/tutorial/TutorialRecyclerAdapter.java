package org.michenux.yourappidea.tutorial;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.michenux.drodrolib.db.CursorRecyclerViewAdapter;
import org.michenux.drodrolib.db.utils.CursorUtils;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.tutorial.sync.TutorialContentProvider;

public class TutorialRecyclerAdapter extends CursorRecyclerViewAdapter<TutorialRecyclerAdapter.TutorialViewHolder> {
    public TutorialRecyclerAdapter() {
        super(null);
    }

    public TutorialRecyclerAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public TutorialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutorial_listitem, parent, false);
        return new TutorialViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TutorialViewHolder viewHolder, Cursor cursor) {
        String title = CursorUtils.getString(TutorialContentProvider.TITLE_COLUMN, cursor);
        viewHolder.getTitleView().setText(Html.fromHtml(title));
        viewHolder.getDescriptionView().setText(Html.fromHtml(CursorUtils.getString(TutorialContentProvider.DESCRIPTION_COLUMN, cursor)));

        long date = CursorUtils.getLong(TutorialContentProvider.DATECREATION_COLUMN, cursor) * 1000;
        int flags = 0;
        flags |= DateUtils.FORMAT_SHOW_DATE;
        flags |= DateUtils.FORMAT_ABBREV_MONTH;
        flags |= DateUtils.FORMAT_SHOW_YEAR;

        viewHolder.getDateView().setText(DateUtils.formatDateTime(viewHolder.itemView.getContext(), date, flags));
    }

    public static class TutorialViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleView;
        private TextView mDescriptionView;
        private TextView mDateView;

        public TutorialViewHolder(View itemView) {
            super(itemView);
            mTitleView = (TextView) itemView.findViewById(R.id.tutorial_title);
            mDescriptionView = (TextView) itemView.findViewById(R.id.tutorial_desc);
            mDateView = (TextView) itemView.findViewById(R.id.tutorial_date);
        }

        public TextView getTitleView() {
            return mTitleView;
        }

        public TextView getDescriptionView() {
            return mDescriptionView;
        }

        public TextView getDateView() {
            return mDateView;
        }
    }
}
