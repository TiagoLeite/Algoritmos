package com.tiago.algoritmos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InsertionSortFragment extends Fragment
{
    private View rootView;
    private View container;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_insertion, container, false);
        this.container = container;
        this.rootView = rootView;
        return rootView;
    }
}
