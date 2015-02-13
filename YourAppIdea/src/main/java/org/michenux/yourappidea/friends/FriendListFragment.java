package org.michenux.yourappidea.friends;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
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
import org.lucasr.twowayview.ItemSelectionSupport;
import org.lucasr.twowayview.widget.TwoWayView;;
import org.michenux.drodrolib.ui.fragment.FragmentHelper;
import org.michenux.yourappidea.R;

public class FriendListFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor>, ItemClickSupport.OnItemClickListener {

	private FriendRecyclerAdapter mAdapter;

    private ItemSelectionSupport mItemSelectionSupport;

    private TwoWayView mRecyclerView ;

	private boolean dualPanel;

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
        mRecyclerView = (TwoWayView) mainView.findViewById(R.id.friend_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(false);
        final ItemClickSupport itemClick = ItemClickSupport.addTo(mRecyclerView);
        itemClick.setOnItemClickListener(this);
        mItemSelectionSupport = ItemSelectionSupport.addTo(mRecyclerView);
        mItemSelectionSupport.setChoiceMode(ItemSelectionSupport.ChoiceMode.SINGLE);
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

	private void fillData() {

		this.getLoaderManager().initLoader(0, null, this);
		this.mAdapter = new FriendRecyclerAdapter(null);
        mRecyclerView.setAdapter(this.mAdapter);
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
		this.mAdapter.swapCursor(cursor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	public void onLoaderReset(Loader<Cursor> cursor) {
		this.mAdapter.swapCursor(null);
	}

	@Override
	public void onResume() {
		super.onResume();
        int itemPosition = mItemSelectionSupport.getCheckedItemPosition();
        if ( itemPosition != -1 ) {
            mRecyclerView.smoothScrollToPosition(mItemSelectionSupport.getCheckedItemPosition());
        }
	}

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int row, long id) {
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
