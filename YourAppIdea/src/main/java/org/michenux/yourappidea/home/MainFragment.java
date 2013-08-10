package org.michenux.yourappidea.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.michenux.android.ui.animation.LiveButton;
import org.michenux.android.ui.navdrawer.NavDrawerSelectItemRunnable;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.android.ui.navdrawer.AbstractNavDrawerActivity;

import javax.inject.Inject;

public class MainFragment extends Fragment {

    @Inject
    LiveButton liveButton ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getActivity().getApplication()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater
                .inflate(R.layout.main_fragment, container, false);

        Button button1 = (Button) mainView.findViewById(R.id.mainmenu_button1);
        liveButton.setupLiveAnimOnButton(button1, new NavDrawerSelectItemRunnable((AbstractNavDrawerActivity)this.getActivity(), 1));

        Button button2 = (Button) mainView.findViewById(R.id.mainmenu_button2);
        liveButton.setupLiveAnimOnButton(button2, new NavDrawerSelectItemRunnable((AbstractNavDrawerActivity)this.getActivity(), 2));

        Button button3 = (Button) mainView.findViewById(R.id.mainmenu_button3);
        liveButton.setupLiveAnimOnButton(button3, new NavDrawerSelectItemRunnable((AbstractNavDrawerActivity)this.getActivity(), 3));

        return mainView;
    }
}
