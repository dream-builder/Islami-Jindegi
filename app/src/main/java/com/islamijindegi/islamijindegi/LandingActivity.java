package com.islamijindegi.islamijindegi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class LandingActivity extends Fragment {


    public FragmentResponse delegate=null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof FragmentResponse) {
            delegate = (FragmentResponse) context;
        } else {
            // Throw an error!
        }
    }

    public GridView iconGrid;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_landing,container,false);

        iconGrid=(GridView) view.findViewById(R.id.iconGrid);

        String[] web = {
                "কিতাব",
                "বয়ান",
                "প্রবন্ধ",
                "ইস্তেমা",
                "অফলাইন কিতাব",
                "বিশেষ আমল",
                "মেয়েদের জন্য",
                "English",
                "নামাযের সময়সূচী",

        } ;
        int[] imageId = {
                R.drawable.book,
                R.drawable.audio,
                R.drawable.article,
                R.drawable.ijtema,
                R.drawable.offline_books,
                R.drawable.munajat,
                R.drawable.female_icon,
                R.drawable.en,
                R.drawable.salat,

        };

        IconGridAdapter adapter = new IconGridAdapter(getContext(), web, imageId);
        iconGrid.setAdapter(adapter);

        iconGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.grid_text);
                delegate.onFragmentLoadCompleted(String.valueOf(position));
            }
        });

        return view;

    }
}
