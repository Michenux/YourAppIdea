package org.michenux.yourappidea.aroundme;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lucasr.twowayview.ItemClickSupport;
import org.lucasr.twowayview.widget.DividerItemDecoration;
import org.lucasr.twowayview.widget.TwoWayView;
import org.michenux.drodrolib.db.utils.CursorUtils;
import org.michenux.yourappidea.R;

public class CityListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickSupport.OnItemClickListener {

    public static final String CITY_NAME = "city_name";
    public static final String CITY_COUNTRY = "city_country";
    public static final String CITY_LONGITUDE = "city_longitude";
    public static final String CITY_LATITUDE = "city_latitude";

    private CityRecyclerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        this.mAdapter = new CityRecyclerAdapter(null);
        this.getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_fragment, container, false);
        TwoWayView recyclerView = (TwoWayView) view.findViewById(R.id.city_listview);
        recyclerView.setAdapter(this.mAdapter);
        recyclerView.setHasFixedSize(true);
        final Drawable divider = getResources().getDrawable(R.drawable.divider);
        recyclerView.addItemDecoration(new DividerItemDecoration(divider));
        final ItemClickSupport itemClick = ItemClickSupport.addTo(recyclerView);
        itemClick.setOnItemClickListener(this);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = { CityContentProvider.ID_COLUMN,
                CityContentProvider.NAME_COLUMN, CityContentProvider.COUNTRY_COLUMN, CityContentProvider.LATITUDE_COLUMN, CityContentProvider.LONGITUDE_COLUMN };
        CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
                CityContentProvider.CONTENT_URI, projection, null, null, CityContentProvider.NAME_COLUMN);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        this.mAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        this.mAdapter.changeCursor(null);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Intent returnIntent = new Intent();
        Cursor cursor = this.mAdapter.getCursor();
        cursor.moveToPosition(position);
        returnIntent.putExtra(CITY_NAME, CursorUtils.getString(CityContentProvider.NAME_COLUMN, cursor));
        returnIntent.putExtra(CITY_COUNTRY, CursorUtils.getString(CityContentProvider.COUNTRY_COLUMN, cursor));
        returnIntent.putExtra(CITY_LONGITUDE, CursorUtils.getDouble(CityContentProvider.LONGITUDE_COLUMN, cursor));
        returnIntent.putExtra(CITY_LATITUDE, CursorUtils.getDouble(CityContentProvider.LATITUDE_COLUMN, cursor));
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().supportFinishAfterTransition();
    }
}
