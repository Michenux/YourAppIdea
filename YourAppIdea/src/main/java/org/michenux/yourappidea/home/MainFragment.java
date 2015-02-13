package org.michenux.yourappidea.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.michenux.drodrolib.ui.navdrawer.NavigationDrawerFragment;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

public class MainFragment extends Fragment implements View.OnClickListener {

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

        Button button0 = (Button) mainView.findViewById(R.id.mainmenu_button0);
        button0.setOnClickListener(this);

        Button button1 = (Button) mainView.findViewById(R.id.mainmenu_button1);
        button1.setOnClickListener(this);

        Button button2 = (Button) mainView.findViewById(R.id.mainmenu_button2);
        button2.setOnClickListener(this);

        Button button3 = (Button) mainView.findViewById(R.id.mainmenu_button3);
        button3.setOnClickListener(this);

        Button button4 = (Button) mainView.findViewById(R.id.mainmenu_button4);
        button4.setOnClickListener(this);

        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        NavigationDrawerFragment fragment = ((YourAppMainActivity)this.getActivity()).findNavDrawerFragment();
        switch (v.getId()) {
            case R.id.mainmenu_button0:
                fragment.selectItem(1, false);
                break;
            case R.id.mainmenu_button1:
                fragment.selectItem(2, false);
                break;
            case R.id.mainmenu_button2:
                fragment.selectItem(3, false);
                break;
            case R.id.mainmenu_button3:
                fragment.selectItem(4, false);
                break;
            case R.id.mainmenu_button4:
                fragment.selectItem(5, false);
                break;
        }
    }
}
