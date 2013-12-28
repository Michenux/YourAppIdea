package org.michenux.yourappidea.aroundme;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.michenux.android.db.utils.CursorUtils;
import org.michenux.yourappidea.R;

public class CityListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    public static final String CITY_NAME = "city_name";
    public static final String CITY_COUNTRY = "city_country";
    public static final String CITY_LONGITUDE = "city_longitude";
    public static final String CITY_LATITUDE = "city_latitude";

    private SimpleCursorAdapter mAdapter;

    private String[] mAdapterFromColumns = new String[] {CityContentProvider.NAME_COLUMN, CityContentProvider.COUNTRY_COLUMN};
    private int[] mAdapterToViews = new int[] { R.id.city_name, R.id.city_country};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        this.mAdapter = new SimpleCursorAdapter(this.getActivity(),
                R.layout.city_listitem, null, mAdapterFromColumns, mAdapterToViews, 0);
        this.getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_fragment, container, false);
        ListView listView = (ListView) view.findViewById(R.id.city_listview);
        listView.setAdapter(this.mAdapter);
        listView.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent returnIntent = new Intent();
        Cursor cursor = this.mAdapter.getCursor();
        cursor.moveToPosition(position);
        returnIntent.putExtra(CITY_NAME, CursorUtils.getString(CityContentProvider.NAME_COLUMN, cursor));
        returnIntent.putExtra(CITY_COUNTRY, CursorUtils.getString(CityContentProvider.COUNTRY_COLUMN, cursor));
        returnIntent.putExtra(CITY_LONGITUDE, CursorUtils.getDouble(CityContentProvider.LONGITUDE_COLUMN, cursor));
        returnIntent.putExtra(CITY_LATITUDE, CursorUtils.getDouble(CityContentProvider.LATITUDE_COLUMN, cursor));
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }
}
