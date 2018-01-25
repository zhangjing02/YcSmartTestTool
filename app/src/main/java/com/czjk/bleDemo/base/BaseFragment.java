package com.czjk.bleDemo.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Field;


public class BaseFragment extends Fragment implements View.OnClickListener {

    public Context mContext;
    public Resources mResources;
    public LayoutInflater mInflater;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mResources = mContext.getResources();
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
