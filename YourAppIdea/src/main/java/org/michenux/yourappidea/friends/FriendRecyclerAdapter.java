package org.michenux.yourappidea.friends;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.michenux.drodrolib.db.CursorRecyclerAdapter;
import org.michenux.drodrolib.db.utils.CursorUtils;
import org.michenux.drodrolib.resources.ResourceUtils;
import org.michenux.yourappidea.R;

public class FriendRecyclerAdapter extends CursorRecyclerAdapter<FriendRecyclerAdapter.FriendViewHolder> {

    public FriendRecyclerAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public void onBindViewHolderCursor(FriendViewHolder holder, Cursor cursor) {
        String faceName = CursorUtils.getString(FriendContentProvider.FACE_COLUMN, cursor);
        String friendName = CursorUtils.getString(FriendContentProvider.NAME_COLUMN, cursor);
        Drawable face = ResourceUtils.getDrawableByName(faceName, holder.itemView.getContext());
        face.setBounds( 0, 0, 70, 70 );

        TextView textView = holder.getNameView();
        textView.setText(friendName);
        textView.setCompoundDrawablePadding(10);
        textView.setCompoundDrawables(face, null,	null, null);
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlistitem, parent, false);
        FriendViewHolder vh = new FriendViewHolder(v);
        return vh;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            mNameView = (TextView) itemView.findViewById(R.id.friend_name);
        }

        public TextView getNameView() {
            return mNameView;
        }
    }
}
