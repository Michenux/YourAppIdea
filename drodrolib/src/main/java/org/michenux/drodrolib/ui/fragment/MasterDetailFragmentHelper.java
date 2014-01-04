package org.michenux.drodrolib.ui.fragment;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class MasterDetailFragmentHelper {

	public static MasterDetailFragments getCurrentFragments( int masterView, int detailView, Class<? extends Fragment> detailClass,
			FragmentManager fm ) {
		MasterDetailFragments masterDetailFragments = new MasterDetailFragments();
		
		List<Fragment> fragmentToRemove = new ArrayList<>();
		
		Fragment frag1 = fm.findFragmentById(masterView);
		if ( frag1 != null ) {
			if ( detailClass.isAssignableFrom(frag1.getClass())) {
				masterDetailFragments.detail = frag1;

				fm.popBackStackImmediate();
				fragmentToRemove.add(frag1);

				frag1 = fm.findFragmentById(masterView);
				masterDetailFragments.master = frag1;
				fragmentToRemove.add(frag1);
			}
			else {
				masterDetailFragments.master = frag1;
				fragmentToRemove.add(frag1);
			}
		}
		
		Fragment frag2 = fm.findFragmentById(detailView);
		if ( frag2 != null ) {
			fragmentToRemove.add(frag2);
			if ( masterDetailFragments.detail == null ) {
				masterDetailFragments.detail = frag2 ;
			}
		}	

		FragmentTransaction ft = fm.beginTransaction();
		for( Fragment f : fragmentToRemove ) {
			ft.remove(f);
		}
		ft.commit();
		fm.executePendingTransactions();
		
		return masterDetailFragments ;
	}

	public static void initFragments( MasterDetailFragments fragments, int masterView, int detailView, Configuration configuration, FragmentManager fm ) {
		boolean dualPanel = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;

		if (dualPanel) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(masterView, fragments.master);
			if ( fragments.detail != null ) {
				ft.add(detailView, fragments.detail);
			}
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.commit();
		} else {
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(masterView, fragments.master);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.commit();

			if ( fragments.detail != null ) {
				ft = fm.beginTransaction();
				ft.add(masterView, fragments.detail);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}
}
