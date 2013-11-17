package org.michenux.yourappidea.friends;

import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.michenux.android.db.utils.CursorUtils;
import org.michenux.android.resources.ResourceUtils;
import org.michenux.android.ui.fragment.FragmentHelper;
import org.michenux.yourappidea.R;

/**
 * @author Michenux
 * 
 */
public class FriendListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	/**
	 * 
	 */
	private SimpleCursorAdapter adapter;

	/**
	 * 
	 */
	private boolean dualPanel;

	/**
	 * @return
	 */
	public static FriendListFragment newInstance() {
		FriendListFragment frag = new FriendListFragment();
		// Bundle args = new Bundle();
		// frag.setArguments(args);
		return frag;
	}

    /**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.friendlist, container, false);
		fillData();
		return mainView;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.dualPanel = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	/**
	 * 
	 */
	private void fillData() {
		String[] from = new String[] { FriendContentProvider.NAME_COLUMN};
		int[] to = new int[] { R.id.friend_name };

		this.getLoaderManager().initLoader(0, null, this);
		this.adapter = new SimpleCursorAdapter(this.getActivity(),
				R.layout.friendlistitem, null, from, to, 0);

		this.adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			public boolean setViewValue(View paramView, Cursor paramCursor,
					int paramInt) {

				if (paramView.getId() == R.id.friend_name) {

					String faceName = CursorUtils.getString(FriendContentProvider.FACE_COLUMN, paramCursor);
					String friendName = CursorUtils.getString(FriendContentProvider.NAME_COLUMN, paramCursor);
					Drawable face = ResourceUtils.getDrawableByName( faceName, FriendListFragment.this.getActivity());
					face.setBounds( 0, 0, 70, 70 );

					TextView textView = (TextView) paramView;
					textView.setText(friendName);
					textView.setCompoundDrawablePadding(10);
					textView.setCompoundDrawables(face, null,	null, null);
					return true;
				}
				return false;
			}
		});

		this.setListAdapter(this.adapter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int,
	 *      android.os.Bundle)
	 */
	public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
		String[] projection = { FriendContentProvider.ID_COLUMN,
                FriendContentProvider.NAME_COLUMN, FriendContentProvider.FACE_COLUMN };
		CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
				FriendContentProvider.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader,
	 *      Object)
	 */
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		this.adapter.swapCursor(cursor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	public void onLoaderReset(Loader<Cursor> cursor) {
		this.adapter.swapCursor(null);
	}

	@Override
	public void onResume() {
		super.onResume();
		if ( this.getListView().getSelectedView() != null ) {
			ensureVisible(this.getListView(), 
				this.getListView().getSelectedView());
		}
	}
	
	 private void ensureVisible(ListView parent, View view) {
		 parent.smoothScrollToPosition(parent.getSelectedItemPosition());
//		    Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
//		    parent.requestChildRectangleOnScreen(view, rect, false);
		}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView,
	 *      android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		
		super.onListItemClick(listView, view, position, id);

		Uri detailUri = Uri.parse(FriendContentProvider.CONTENT_URI + "/" + id);
		FriendDetailFragment detailFragment = FriendDetailFragment
				.newInstance(detailUri);
		if (!this.dualPanel) {
			FragmentHelper.initFragmentWithBackstack(detailFragment,
					R.id.friendmain_fragment, this.getParentFragment().getChildFragmentManager());
		} else {
			FragmentHelper.initFragment(detailFragment,
					R.id.frienddetail_fragment, this.getParentFragment().getChildFragmentManager());
		}
	}
}
