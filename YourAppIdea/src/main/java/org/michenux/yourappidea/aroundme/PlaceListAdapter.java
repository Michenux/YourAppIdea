package org.michenux.yourappidea.aroundme;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.michenux.android.ui.widget.MCXArrayAdapter;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.airport.Flight;

import java.util.List;

public class PlaceListAdapter extends ArrayAdapter<Place> {

    private List<Place> mPlaces ;

    public PlaceListAdapter(Context context, int textViewResourceId,
                          List<Place> objects) {
        super(context, textViewResourceId, objects);
        mPlaces = objects;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {

        View updateView ;
        ViewHolder viewHolder ;
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

            updateView.setTag(viewHolder);
        } else {
            updateView = view;
            viewHolder =  (ViewHolder) updateView.getTag();
        }

        Place place = (Place) getItem(position);
        viewHolder.placeNameView.setText(place.getName());
        viewHolder.placeCountryView.setText(place.getCountry());
        viewHolder.placeDistanceView.setText(this.getContext().getString(R.string.aroundme_placedistance, (int)(place.getDistance() / 1000)));

        return updateView;
    }

    public List<Place> getPlaces() {
        return mPlaces;
    }

    private static class ViewHolder {
        public TextView placeNameView ;
        public TextView placeDistanceView ;
        public TextView placeCountryView ;
    }
}
