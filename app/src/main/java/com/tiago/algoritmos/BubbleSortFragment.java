package com.tiago.algoritmos;

import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tiago.algoritmos.R.raw.swap;

public class BubbleSortFragment extends Fragment
{
    private LinearLayout barsContainer;
    private ViewGroup container;
    private int vet[];
    private View rootView;
    private BubbleSortThread bubbleSortThread;
    private MediaPlayer mp;
    private List<AlgorithmStep> steps;
    private TextView tvInfo;
    private GraphView graph;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_bubble, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        getActivity().setTitle("BubbleSort");

        this.container = container;

        barsContainer = (LinearLayout) view.findViewById(R.id.bars_container);

        addBars(10);

        /*WebView webview = (WebView)view.findViewById(R.id.code_algorithm);
        String summary = "<html><body>You scored <b>192</b> points.</body></html>";
        webview.loadData(summary, "text/html", null);*/

        rootView = view;

        tvInfo = (TextView)rootView.findViewById(R.id.tv_info);

        graph = (GraphView)rootView.findViewById(R.id.graph);

        graph.setPointA(new PointF(10, 100));
        graph.setPointB(new PointF(500, 400));

        graph.draw();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setup();
    }

    private void setup()
    {
        ImageView playPause = (ImageView)rootView.findViewById(R.id.play_pause);
        mp = MediaPlayer.create(getActivity(), swap);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ((ImageView)rootView.findViewById(R.id.next)).
                        setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.next_disabled));
                ((ImageView)rootView.findViewById(R.id.previous)).
                        setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.previous_disabled));
                try
                {
                    if(bubbleSortThread == null || bubbleSortThread.isFinished())
                    {

                        bubbleSortThread = new BubbleSortThread(BubbleSortThread.MODE_AUTO);
                        bubbleSortThread.start();
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

        ImageView next = (ImageView)rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(bubbleSortThread == null || bubbleSortThread.isFinished())
                {
                    bubbleSortThread = new BubbleSortThread(BubbleSortThread.MODE_STEP_BY_STEP);
                    bubbleSortThread.start();
                }
                else if(!bubbleSortThread.isFinished())
                {
                    bubbleSortThread.runNextStep();
                }
            }
        });

        ImageView previous = (ImageView)rootView.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(bubbleSortThread == null || bubbleSortThread.isFinished())
                {
                    bubbleSortThread = new BubbleSortThread(BubbleSortThread.MODE_STEP_BY_STEP);
                    bubbleSortThread.start();
                }
                else if(!bubbleSortThread.isFinished())
                {
                    bubbleSortThread.runPreviousStep();
                }
            }
        });
    }

    private void animateBars(final AlgorithmStep step)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                View b1 = barsContainer.findViewWithTag(step.getPosition1());
                View b2 = barsContainer.findViewWithTag(step.getPosition2());
                b1.findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                b2.findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                for(int i = 0; i < barsContainer.getChildCount(); i++)
                {
                    View v = barsContainer.getChildAt(i);
                    if(v.equals(b1) || v.equals(b2))
                        continue;
                    v.findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.gray));
                }

                if(step.isSwap())
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
                tvInfo.setText(step.getStateDescripton());
            }
        });
    }

    private void setBarSortedOk(final int pos)
    {
        if(pos < 0) return;
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
        public static final int MODE_AUTO = 0;
        public static final int MODE_STEP_BY_STEP = 1;
        private boolean suspended = false;
        private boolean finished = false;
        private int mode;
        private int currentStep;

        BubbleSortThread(int mode)
        {
            super();
            this.mode = mode;
            currentStep = 0;
        }

        @Override
        public void run()
        {
            runAlgorithm();
            switch (mode)
            {
                case MODE_AUTO:
                    runAuto();
                    break;
                case MODE_STEP_BY_STEP:
                    runNextStep();
            }
        }

        private void runNextStep()
        {
            if(currentStep >= steps.size())
            {
                finished = true;
                return;
            }
            AlgorithmStep step = steps.get(currentStep++);
            animateBars(step);
            setBarSortedOk(step.getBarOk());
        }

        private void runPreviousStep()
        {
            if(currentStep < 1)
            {
                return;
            }
            AlgorithmStep step = steps.get(--currentStep);
            animateBars(step);
            setBarSortedOk(step.getBarOk());
        }

        private void runAuto()
        {
            for(AlgorithmStep step : steps)
            {
                try
                {
                    synchronized (this)
                    {
                        while (suspended)
                            this.wait();
                    }
                    animateBars(step);
                    Thread.sleep(700);
                }
                catch (Exception e)
                {
                    return;
                }
                setBarSortedOk(step.getBarOk());
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

        private void runAlgorithm()
        {
            steps = new ArrayList<>();
            AlgorithmStep step = null;
            int size = vet.length, aux;
            for(int i = 0; i < size; i++)
            {
                for(int j = 0; j < size-1-i; j++)
                {
                    if(vet[j] > vet[j+1])
                    {
                        aux = vet[j];
                        vet[j] = vet[j+1];
                        vet[j+1] = aux;
                        step = new AlgorithmStep(vet[j], vet[j+1], true);
                        step.setStepDescripton("step " + bubbleSortThread.getCurrentStep());
                        steps.add(step);
                    }
                    else
                    {
                        step = new AlgorithmStep(vet[j], vet[j+1], false);
                        step.setStepDescripton("step " + bubbleSortThread.getCurrentStep());
                        steps.add(step);
                    }
                }
                step.setBarOk(vet[size-i-1]);//nao vai dar null pointer nao!
            }
        }

        public int getCurrentStep() {
            return currentStep;
        }

        public void setCurrentStep(int currentStep) {
            this.currentStep = currentStep;
        }

        public boolean isFinished() {
            return finished;
        }
    }
}
