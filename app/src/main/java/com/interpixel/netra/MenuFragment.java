package com.interpixel.netra;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MenuFragment extends Fragment {

    final int imageId;

    public MenuFragment(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(imageId);
        return view;
    }

}
