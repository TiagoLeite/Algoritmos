package com.tiago.algoritmos;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SelectionSortFragment extends Fragment
{
    private View rootView;
    private ViewGroup container;
    private LinearLayout barsContainer;
    private int vet[];
    private SelectionSortThread selectionSortThread;
    private MediaPlayer mp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_selection, container, false);
        this.container = container;
        getActivity().setTitle("SelectionSort");
        return rootView;
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
                b1.findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                b2.findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.colorAccent));

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

    private class SelectionSortThread extends Thread
    {
        private boolean suspended = false;
        private boolean skip = false;
        @Override
        public void run()
        {
            try
            {
                int size = vet.length;
                int temp;
                for (int i = 1; i < size; i++)
                {
                    skip = false;
                    for(int j = i ; j > 0 ; j--)
                    {
                        synchronized (this)
                        {
                            while (suspended)
                                this.wait();
                        }

                        if(vet[j] < vet[j-1])
                        {
                            temp = vet[j];
                            vet[j] = vet[j-1];
                            vet[j-1] = temp;
                            animateBars(vet[j],  vet[j-1], true);
                        }
                        else
                        {
                            animateBars(vet[j],  vet[j-1], false);
                            skip = true;
                        }
                        try
                        {
                            Thread.sleep(700);
                        }
                        catch (Exception e)
                        {
                            return;
                        }
                        if(skip)
                            break;
                    }
                    //setBarSortedOk(vet[j-1]);
                }
            }


                /*for(int i = 0; i < size; i++)
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
                }*/
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
