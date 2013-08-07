package org.michenux.yourappidea.airport;

import java.util.List;

import org.michenux.yourappidea.R;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AirportAdapter extends ArrayAdapter<Flight> {
	
	private java.text.DateFormat df ;
	private java.text.DateFormat tf ;
	
	public AirportAdapter(Context context, int textViewResourceId,
			List<Flight> objects) {
		super(context, textViewResourceId, objects);
		
		df = DateFormat.getDateFormat(this.getContext());
		tf = DateFormat.getTimeFormat(this.getContext());
	}

	public View getView(int position, View view, ViewGroup viewGroup) {

		View updateView ;
		ViewHolder viewHolder ;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			updateView = inflater.inflate(R.layout.airport_listitem, null);
			
			viewHolder = new ViewHolder();
			
			//Name
			viewHolder.flightNameView = (TextView) updateView
					.findViewById(R.id.flight_name);
			//From
			viewHolder.flightFromView = (TextView) updateView
					.findViewById(R.id.flight_from);
			// ETA
			viewHolder.flightEtaView = (TextView) updateView
					.findViewById(R.id.flight_eta);
			// Type			
			viewHolder.flightTypeView = (TextView) updateView
					.findViewById(R.id.flight_type);
			//Speed
			viewHolder.flightSpeedView = (TextView) updateView
					.findViewById(R.id.flight_speed);
			//Alt
			viewHolder.flightAltView = (TextView) updateView
					.findViewById(R.id.flight_alt);			
			updateView.setTag(viewHolder);
		} else {
			updateView = view;
			viewHolder =  (ViewHolder) updateView.getTag();
		}

		Flight flight = (Flight) getItem(position);
		viewHolder.flightNameView.setText(flight.getFlight());
		viewHolder.flightFromView.setText(flight.getName());
		viewHolder.flightEtaView.setText(this.getContext().getString(R.string.airport_eta, df.format(flight.getEta()) + " " + tf.format(flight.getEta())));
		viewHolder.flightTypeView.setText(flight.getType());
		viewHolder.flightSpeedView.setText(this.getContext().getString(R.string.airport_speed, Double.toString( Math.round(flight.getSpeed() * 1.852 ))));
		viewHolder.flightAltView.setText(this.getContext().getString(R.string.airport_altitude, Double.toString( Math.round(flight.getAltitude() / 3.2808))));
		
		return updateView;
	}
	
	
	private static class ViewHolder {
		public TextView flightNameView ;
		public TextView flightFromView ;
		public TextView flightEtaView ;
		public TextView flightTypeView ;
		public TextView flightSpeedView ;
		public TextView flightAltView ;
	}
}
