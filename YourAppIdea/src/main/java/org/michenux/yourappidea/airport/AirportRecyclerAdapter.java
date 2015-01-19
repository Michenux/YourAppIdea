package org.michenux.yourappidea.airport;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.michenux.yourappidea.R;

import java.util.List;

public class AirportRecyclerAdapter extends RecyclerView.Adapter<AirportRecyclerAdapter.AirportViewHolder> {

    private java.text.DateFormat df ;
    private java.text.DateFormat tf ;

    private List<Flight> flights ;
    private String mode;

    public AirportRecyclerAdapter(List<Flight> flights, Context context, String currentMode) {
        super();
        df = DateFormat.getDateFormat(context);
        tf = DateFormat.getTimeFormat(context);
        this.flights = flights;
        this.mode = currentMode;
    }

    @Override
    public AirportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View updateView = inflater.inflate(R.layout.airport_listitem, null);
        return new AirportViewHolder(updateView);
    }

    @Override
    public void onBindViewHolder(AirportViewHolder viewHolder, int position) {
        Context context = viewHolder.itemView.getContext();
        Flight flight = (Flight) this.flights.get(position);
        viewHolder.flightNameView.setText(flight.getFlight());
        viewHolder.flightFromView.setText(flight.getName());
        if ( flight.getEta() != null ) {
            viewHolder.flightEtaView.setText(context.getString(R.string.airport_eta, df.format(flight.getEta()) + " " + tf.format(flight.getEta())));
        }
        else {
            viewHolder.flightEtaView.setText("");
        }
        viewHolder.flightTypeView.setText(flight.getType());
        viewHolder.flightSpeedView.setText(context.getString(R.string.airport_speed, Double.toString(Math.round(flight.getSpeed() * 1.852))));
        viewHolder.flightAltView.setText(context.getString(R.string.airport_altitude, Double.toString(Math.round(flight.getAltitude() / 3.2808))));

        if ( mode.equals("in")) {
            viewHolder.flightPicture.setImageResource(R.drawable.airport_landing);
        } else {
            viewHolder.flightPicture.setImageResource(R.drawable.airport_takeoff);
        }
    }

    @Override
    public int getItemCount() {
        return this.flights.size();
    }

    public void clear() {
        this.flights.clear();
    }

    public void add(Flight flight) {
        this.flights.add(flight);
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public static class AirportViewHolder extends RecyclerView.ViewHolder {

        private ImageView flightPicture;
        private TextView flightNameView;
        private TextView flightFromView;
        private TextView flightEtaView;
        private TextView flightTypeView;
        private TextView flightSpeedView;
        private TextView flightAltView;

        public AirportViewHolder(View itemView) {
            super(itemView);
            // Picture
            flightPicture = (ImageView) itemView
                    .findViewById(R.id.flight_picture);
            // Name
            flightNameView = (TextView) itemView
                    .findViewById(R.id.flight_name);
            //From
            flightFromView = (TextView) itemView
                    .findViewById(R.id.flight_from);
            // ETA
            flightEtaView = (TextView) itemView
                    .findViewById(R.id.flight_eta);
            // Type
            flightTypeView = (TextView) itemView
                    .findViewById(R.id.flight_type);
            //Speed
            flightSpeedView = (TextView) itemView
                    .findViewById(R.id.flight_speed);
            //Alt
            flightAltView = (TextView) itemView
                    .findViewById(R.id.flight_alt);
        }

        public TextView getFlightNameView() {
            return flightNameView;
        }

        public TextView getFlightFromView() {
            return flightFromView;
        }

        public TextView getFlightEtaView() {
            return flightEtaView;
        }

        public TextView getFlightTypeView() {
            return flightTypeView;
        }

        public TextView getFlightSpeedView() {
            return flightSpeedView;
        }

        public TextView getFlightAltView() {
            return flightAltView;
        }

        public ImageView getFlightPicture() {
            return flightPicture;
        }
    }
}
