package com.cyt.ieasy.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.cyt.ieasy.mobilelingliao.R;

/**
 * Created by jin on 2015.11.12.
 */
public class EmportyUtils {

    public static View createLoadingView(Context context) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_failue, null);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setVisibility(View.GONE);
        return linearLayout;
    }

    public static View createFailView(Context context) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_failue, null);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setVisibility(View.GONE);
        return linearLayout;
    }

    public static View netFailView(Context context) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_nonetwork, null);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        return linearLayout;
    }
}
