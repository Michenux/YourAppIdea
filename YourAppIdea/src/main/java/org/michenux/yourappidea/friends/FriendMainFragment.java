package org.michenux.yourappidea.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.michenux.drodrolib.ui.fragment.MasterDetailFragmentHelper;
import org.michenux.drodrolib.ui.fragment.MasterDetailFragments;
import org.michenux.yourappidea.R;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class FriendMainFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setRetainInstance(false);
        setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends, container, false);

        MasterDetailFragments currentFragments = MasterDetailFragmentHelper
                .getCurrentFragments(R.id.friendmain_fragment,
                        R.id.frienddetail_fragment, FriendDetailFragment.class,
                        getChildFragmentManager());
        if ( currentFragments.master == null ) {
            currentFragments.master = FriendListFragment.newInstance();
        }

        MasterDetailFragmentHelper.initFragments(currentFragments, R.id.friendmain_fragment,
                R.id.frienddetail_fragment, getResources().getConfiguration(), getChildFragmentManager());

		return view ;
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friends_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.friends_menu_info:
                SimpleDialogFragment.createBuilder(this.getActivity(), this.getActivity().getSupportFragmentManager())
                        .setMessage(Html.fromHtml(getString(R.string.friends_info_details)))
                        .setTitle(R.string.friends_info_title)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
