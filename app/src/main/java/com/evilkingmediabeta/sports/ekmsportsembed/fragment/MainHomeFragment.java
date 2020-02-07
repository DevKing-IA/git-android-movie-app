package com.evilkingmediabeta.sports.ekmsportsembed.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.evilkingmediabeta.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainHomeFragment extends Fragment {

    private ImageButton btnMovies,btnSeries;
    private FloatingActionButton btnHome;
    private Fragment fragment=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnHome=view.findViewById(R.id.home);
        btnSeries=view.findViewById(R.id.series);
        btnMovies=view.findViewById(R.id.movies);

        btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        btnMovies.setColorFilter(getActivity().getResources().getColor(R.color.grey_40));
        btnSeries.setColorFilter(getActivity().getResources().getColor(R.color.grey_40));

        btnMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new MovieFragment();
                loadFragment(fragment);

                btnMovies.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary));
                btnSeries.setColorFilter(getActivity().getResources().getColor(R.color.grey_40));
                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_40)));


            }
        });

        btnSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new TvSerieFragment();
                loadFragment(fragment);

                btnSeries.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary));
                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_40)));
                btnMovies.setColorFilter(getActivity().getResources().getColor(R.color.grey_40));
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new HomeFragment();
                loadFragment(fragment);

                btnHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                btnMovies.setColorFilter(getActivity().getResources().getColor(R.color.grey_40));
                btnSeries.setColorFilter(getActivity().getResources().getColor(R.color.grey_40));
            }
        });

        loadFragment(new HomeFragment());

    }


    //----load fragment----------------------
    private boolean loadFragment(Fragment fragment){

        if (fragment!=null){

            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();

            return true;
        }
        return false;

    }
}
