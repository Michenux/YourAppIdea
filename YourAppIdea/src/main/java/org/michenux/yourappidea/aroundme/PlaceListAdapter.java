package org.michenux.yourappidea.aroundme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import java.util.List;

public class PlaceListAdapter extends ArrayAdapter<Place> {

    private List<Place> mPlaces;

    private ImageLoader mImageLoader;

    public PlaceListAdapter(Context context, int textViewResourceId,
                            List<Place> objects, ImageLoader imageLoader) {
        super(context, textViewResourceId, objects);
        mPlaces = objects;
        mImageLoader = imageLoader;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.d(YourApplication.LOG_TAG, "PlaceListAdapter.getView - position: " + position);

        View updateView;
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            updateView = inflater.inflate(R.layout.aroundme_listitem, null);

            viewHolder = new ViewHolder();

            //Name
            viewHolder.placeNameView = (TextView) updateView
                    .findViewById(R.id.aroundme_placename);
            //Distance
            viewHolder.placeDistanceView = (TextView) updateView
                    .findViewById(R.id.aroundme_placedistance);

            //Country
            viewHolder.placeCountryView = (TextView) updateView
                    .findViewById(R.id.aroundme_placecountry);

            //Url
            viewHolder.placeImageView = (NetworkImageView) updateView
                    .findViewById(R.id.aroundme_placeimage);

            updateView.setTag(viewHolder);
        } else {
            updateView = view;
            viewHolder = (ViewHolder) updateView.getTag();
        }

        Place place = getItem(position);
        viewHolder.placeNameView.setText(place.getName());
        viewHolder.placeCountryView.setText(place.getCountry());
        viewHolder.placeDistanceView.setText(this.getContext().getString(R.string.aroundme_placedistance, (int) (place.getDistance() / 1000)));
        viewHolder.placeImageView.setImageUrl(place.getImage(), mImageLoader);

        return updateView;
    }

    private static class ViewHolder {
        public TextView placeNameView;
        public TextView placeDistanceView;
        public TextView placeCountryView;
        public NetworkImageView placeImageView;
    }
}
