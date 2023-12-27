package com.example.gesallprov;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class HomeFragment extends Fragment {

    // appens logga, används både som knapp och logga. knapp för att roteras vid ett knapptryck
    private ImageButton logo;
    // deklarerar view
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // initialiserar view och inflate layouten för det här fragmentet
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // initialiserar ImageButton som har appens logga som bild, hittar i layouten
        logo = view.findViewById(R.id.logo);

        // sätter en knapp-lyssnare på knappen, roterar vid knapptryck
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // laddar animationen från en resursfil från mappen anim
                Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);

                // sätter animationen på imageknappen
                logo.setAnimation(rotate);

                // starta animationen på imageknappen
                logo.startAnimation(rotate);
            }
        });

        // returnerar view
        return view;
    }
}