package org.michenux.yourappidea.friends;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.michenux.drodrolib.db.CursorRecyclerViewAdapter;
import org.michenux.drodrolib.db.utils.CursorUtils;
import org.michenux.drodrolib.resources.ResourceUtils;
import org.michenux.yourappidea.R;

public class FriendRecyclerAdapter extends CursorRecyclerViewAdapter<FriendRecyclerAdapter.FriendViewHolder> {
    public FriendRecyclerAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlistitem, parent, false);
        return new FriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder viewHolder, Cursor cursor) {
        String faceName = CursorUtils.getString(FriendContentProvider.FACE_COLUMN, cursor);
        String friendName = CursorUtils.getString(FriendContentProvider.NAME_COLUMN, cursor);
        viewHolder.nameView.setText(friendName);
        viewHolder.imageView.setImageDrawable(ResourceUtils.getDrawableByName(faceName, viewHolder.itemView.getContext()));
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public ImageView imageView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.friend_name);
            imageView = (ImageView) itemView.findViewById(R.id.friend_icon);
        }
    }
}
