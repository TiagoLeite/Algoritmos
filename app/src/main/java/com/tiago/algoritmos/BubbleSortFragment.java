package com.tiago.algoritmos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BubbleSortFragment extends Fragment
{
    private LinearLayout barsContainer;
    private ViewGroup container;
    private int vet[];
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_bubble, container, false);

        getActivity().setTitle("BubbleSort");

        this.container = container;

        barsContainer = (LinearLayout) view.findViewById(R.id.bars_container);

        addBars(9);

        ImageView b = (ImageView)view.findViewById(R.id.bt_play);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                bubbleSort();
            }
        });

        WebView webview = (WebView)view.findViewById(R.id.code_algorithm);
        String summary = "<html><body>You scored <b>192</b> points.</body></html>";
        webview.loadData(summary, "text/html", null);

        rootView = view;

        return view;
    }


    private void animateBars(int pos1, int pos2, boolean swap)
    {
        View b1 = barsContainer.findViewWithTag(pos1);
        View b2 = barsContainer.findViewWithTag(pos2);
        b1.findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        b2.findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        for(int i = 0; i < barsContainer.getChildCount(); i++)
        {
            View v = barsContainer.getChildAt(i);
            if(v.equals(b1) || v.equals(b2))
                continue;
            v.findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.gray));
        }

        if(swap)
        {
            float x1 = b1.getX();
            float x2 = b2.getX();
            b1.animate()
                    .x(x2)
                    .setDuration(900);

            b2.animate()
                    .x(x1)
                    .setDuration(900);
        }
    }

    private void addBars(int numBar)
    {
        LayoutInflater barInflater = LayoutInflater.from(getActivity());
        View barView;
        vet = new int[numBar];
        int j = 0;
        List<Integer> list = new ArrayList<>(numBar);
        for(int i = numBar; i > 0 ; i--)
            list.add(i);
        Collections.shuffle(list);
        for(int i = numBar; i > 0 ; i--)
        {
            barView = barInflater.inflate(R.layout.bar_layout, container, false);
            barView.findViewById(R.id.bar).
                    setLayoutParams(new LinearLayout.LayoutParams(
                            (int) getActivity().getResources().getDimension(R.dimen.bar_length),
                            list.get(i-1)*(int) getActivity().getResources().getDimension(R.dimen.bar_height)));
            ((TextView)barView.findViewById(R.id.value)).setText(String.valueOf(list.get(i-1)));
            barView.setTag(list.get(i-1));
            barsContainer.addView(barView);
            vet[j++] = list.get(i-1);
        }


    }

    private void bubbleSort()
    {
        final BubbleSortThread thread = new BubbleSortThread();
        thread.start();

        ImageView pause = (ImageView)rootView.findViewById(R.id.bt_pause);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if(thread.suspended)
                        thread.resumir();
                    else
                        thread.suspender();
                }
                catch (Exception e)
                {
                    return;
                }
            }
        });
    }

    private class BubbleSortThread extends Thread
    {
        private boolean suspended = false;
        @Override
        public void run()
        {
            try
            {
                int size = vet.length, aux;
                for(int i = 0; i < size; i++)
                {
                    for(int j = 0; j < size-1-i; j++)
                    {
                        synchronized (this)
                        {
                            while (suspended)
                                this.wait();
                        }

                        if(vet[j] > vet[j+1])
                        {
                            final int tag = vet[j];
                            final int tag2 = vet[j+1];
                            aux = vet[j];
                            vet[j] = vet[j+1];
                            vet[j+1] = aux;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    animateBars(tag, tag2, true);
                                }
                            });
                        }
                        else
                        {
                            final int tag = vet[j];
                            final int tag2 = vet[j+1];
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    animateBars(tag, tag2, false);
                                }
                            });
                        }
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (Exception e)
                        {
                            return;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                return;
            }
        }

        void suspender()
        {
            this.suspended = true;
        }

        synchronized void resumir()
        {
            this.suspended = false;
            notify();
        }

        synchronized boolean isSuspended() {
            return suspended;
        }
    }
}
