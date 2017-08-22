package com.tiago.algoritmos;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BubbleSortFragment extends Fragment
{
    private LinearLayout barsContainer;
    private ViewGroup container;
    private int vet[];
    private View rootView;
    private BubbleSortThread bubbleSortThread;
    private MediaPlayer mp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_bubble, container, false);

        getActivity().setTitle("BubbleSort");

        this.container = container;

        barsContainer = (LinearLayout) view.findViewById(R.id.bars_container);

        addBars(10);

        /*WebView webview = (WebView)view.findViewById(R.id.code_algorithm);
        String summary = "<html><body>You scored <b>192</b> points.</body></html>";
        webview.loadData(summary, "text/html", null);*/

        rootView = view;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setup();
    }

    private void setup()
    {
        bubbleSortThread = new BubbleSortThread();

        ImageView playPause = (ImageView)rootView.findViewById(R.id.play_pause);

        mp = MediaPlayer.create(getActivity(), R.raw.swap);

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if(!bubbleSortThread.isAlive())
                    {
                        bubbleSortThread.start();
                        ((ImageView)view).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pause));
                    }
                    else if(bubbleSortThread.suspended)
                    {
                        ((ImageView)view).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pause));
                        bubbleSortThread.resumir();
                    }
                    else
                    {
                        ((ImageView)view).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.play));
                        bubbleSortThread.suspender();
                    }
                }
                catch (Exception e)
                {
                    return;
                }
            }
        });
    }

    private void animateBars(final int pos1, final int pos2, final boolean swap)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
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
                    mp.start();
                    float x1 = b1.getX();
                    float x2 = b2.getX();
                    b1.animate()
                            .x(x2)
                            .setDuration(500);

                    b2.animate()
                            .x(x1)
                            .setDuration(500);
                }
            }
        });
    }

    private void setBarSortedOk(final int pos)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                barsContainer.findViewWithTag(pos).findViewById(R.id.check_ok).setVisibility(View.VISIBLE);
            }
        });
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
                            aux = vet[j];
                            vet[j] = vet[j+1];
                            vet[j+1] = aux;
                            animateBars(vet[j],  vet[j+1], true);
                        }
                        else
                            animateBars(vet[j],  vet[j+1], false);
                        try
                        {
                            Thread.sleep(700);
                        }
                        catch (Exception e)
                        {
                            return;
                        }
                    }
                    setBarSortedOk(vet[size-i-1]);
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
    }
}
