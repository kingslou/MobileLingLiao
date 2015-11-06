package com.cyt.ieasy.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyt.ieasy.mobilelingliao.R;

/**
 * Created by jin on 2015.11.06.
 */
public class StockFragment extends Fragment {

    private String TAG = "fragment_stock";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dept, container, false);

        Log.i(TAG, "onCreateView");

        return view;
    }
}
