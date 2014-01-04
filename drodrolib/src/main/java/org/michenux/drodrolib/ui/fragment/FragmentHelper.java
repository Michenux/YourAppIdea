package org.michenux.drodrolib.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @author Michenux
 *
 */
public class FragmentHelper {

	/**
	 * @param frag
	 * @param container
	 * @param fm
	 */
	public static void initFragment( Fragment frag, int container, FragmentManager fm ) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(container, frag);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();		
	}
	
	/**
	 * @param frag
	 * @param container
	 * @param fm
	 */
	public static void initFragmentWithBackstack( Fragment frag, int container, FragmentManager fm ) {
		FragmentTransaction ft = fm.beginTransaction();
        ft.add(container, frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
	}
	
	/**
	 * @param container1
	 * @param container2
	 * @param fm
	 */
	public static void swapFragment( int container1, int container2, FragmentManager fm ) {
		
		Fragment f1 = fm.findFragmentById(container1);
		Fragment f2 = fm.findFragmentById(container2);

		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(f1);
		ft.remove(f2);
		ft.commit();
		fm.executePendingTransactions();
			
		ft = fm.beginTransaction();
		ft.add(container1, f2);
		ft.add(container2, f1);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			
		ft.commit();
	}
}
