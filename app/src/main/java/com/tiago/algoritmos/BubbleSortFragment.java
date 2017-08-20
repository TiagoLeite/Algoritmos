package com.tiago.algoritmos;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class BubbleSortFragment extends Fragment
{
    private LinearLayout barsContainer;
    private ViewGroup container;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_bubble, container, false);

        getActivity().setTitle("BubbleSort");

        this.container = container;

        barsContainer = (LinearLayout) view.findViewById(R.id.bars_container);

        addBars(12);

        Button b = (Button)view.findViewById(R.id.btplay);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random random = new Random();

                int pos = Math.abs(random.nextInt()%12);
                int op = 11-pos;

                View b1 = barsContainer.findViewWithTag(pos+"");
                View b2 = barsContainer.findViewWithTag(op+"");

                float x1 = b1.getX();
                float x2 = b2.getX();

                b1.animate()
                        .x(x2)
                        .setDuration(500);

                b2.animate()
                        .x(x1)
                        .setDuration(500);
            }
        });

        return view;
    }

    private void addBars(int numBar)
    {
        LayoutInflater barInflater = LayoutInflater.from(getActivity());
        View barView;

        for(int i = 0; i < numBar; i++)
        {
            barView = barInflater.inflate(R.layout.bar_layout, container, false);
            barView.findViewById(R.id.bar).setLayoutParams(new LinearLayout.LayoutParams(40, i*20));
            ((TextView)barView.findViewById(R.id.value)).setText(String.valueOf(i));
            barView.setTag(String.valueOf(i));
            barsContainer.addView(barView);
        }
    }

}
