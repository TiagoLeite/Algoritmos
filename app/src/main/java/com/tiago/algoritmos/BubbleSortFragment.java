package com.tiago.algoritmos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BubbleSortFragment extends Fragment
{
    private LinearLayout barsContainer;
    private ViewGroup container;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_bubble, container, false);

        getActivity().setTitle("BubbleSort");

        this.container = container;

        barsContainer = (LinearLayout) view.findViewById(R.id.bars_container);

        addBars(13);

        return view;
    }

    private void addBars(int numBar)
    {
        LayoutInflater barInflater = LayoutInflater.from(getActivity());
        View barView;

        for(int i = 1; i < numBar; i++)
        {
            barView = barInflater.inflate(R.layout.bar_layout, container, false);
            barView.findViewById(R.id.bar).setLayoutParams(new LinearLayout.LayoutParams(40, i*20));
            ((TextView)barView.findViewById(R.id.value)).setText(String.valueOf(i));
            barsContainer.addView(barView);
        }
    }
}
