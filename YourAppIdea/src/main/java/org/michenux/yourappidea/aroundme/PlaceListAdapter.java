package org.michenux.yourappidea.aroundme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.michenux.yourappidea.R;

import java.util.List;

public class PlaceListAdapter extends ArrayAdapter<Place> {

    private List<Place> mPlaces;

    public PlaceListAdapter(Context context, int textViewResourceId, List<Place> objects) {
        super(context, textViewResourceId, objects);
        mPlaces = objects;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {

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
            viewHolder.placeImageView = (ImageView) updateView
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
        ImageLoader.getInstance().displayImage(place.getImage(), viewHolder.placeImageView);

        return updateView;
    }

    private static class ViewHolder {
        public TextView placeNameView;
        public TextView placeDistanceView;
        public TextView placeCountryView;
        public ImageView placeImageView;
    }
}
