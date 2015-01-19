package org.michenux.yourappidea.aroundme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.michenux.yourappidea.R;

import java.util.List;

public class PlaceRecyclerAdapter extends RecyclerView.Adapter<PlaceRecyclerAdapter.PlaceViewHolder>{

    private List<Place> places ;

    public PlaceRecyclerAdapter(List<Place> places) {
        super();
        this.places = places;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View updateView = inflater.inflate(R.layout.aroundme_listitem, null);
        return new PlaceViewHolder(updateView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder viewHolder, int position) {
        Context context = viewHolder.itemView.getContext();
        Place place = (Place) this.places.get(position);
        viewHolder.getNameView().setText(place.getName());
        viewHolder.getCountryView().setText(place.getCountry());
        viewHolder.getDistanceView().setText(viewHolder.itemView.getContext().getString(R.string.aroundme_placedistance, (int) (place.getDistance() / 1000)));
        ImageLoader.getInstance().displayImage(place.getImage(), viewHolder.getImageView());
    }

    @Override
    public int getItemCount() {
        return this.places.size();
    }

    public void clear() {
        this.places.clear();
    }

    public void add(Place place) {
        this.places.add(place);
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView distanceView;
        private TextView countryView;
        private ImageView imageView;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            //Name
            nameView = (TextView) itemView
                    .findViewById(R.id.aroundme_placename);
            //Distance
            distanceView = (TextView) itemView
                    .findViewById(R.id.aroundme_placedistance);

            //Country
            countryView = (TextView) itemView
                    .findViewById(R.id.aroundme_placecountry);

            //Url
            imageView = (ImageView) itemView
                    .findViewById(R.id.aroundme_placeimage);
        }

        public TextView getNameView() {
            return nameView;
        }

        public TextView getDistanceView() {
            return distanceView;
        }

        public TextView getCountryView() {
            return countryView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
