package com.tiago.algoritmos;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Test;

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
    private List<AlgorithmStep> algorithmSteps;
    private TextView tvInfo;
    private List<TextView> algorithmCodeLines;

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

        rootView = view;

        tvInfo = (TextView)rootView.findViewById(R.id.tv_info);

        setHasOptionsMenu(true);

        ViewGroup codeContainer = (ViewGroup)view.findViewById(R.id.code_container);

        algorithmCodeLines = new ArrayList<>(10);

        for (int k=0; k < 10; k++)
        {
            TextView tvCodeLine = new TextView(view.getContext());
            algorithmCodeLines.add(tvCodeLine);
            tvCodeLine.setTypeface(Typeface.createFromAsset(view.
                    getContext().getAssets(),"FiraMono-Medium.otf"));
            int stringId = getResources().getIdentifier("bubble_line_"+k,
                    "string", getContext().getPackageName());
            tvCodeLine.setText(Html.fromHtml(getString(stringId)));
            codeContainer.addView(tvCodeLine);
        }

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_menu, menu);
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
            if(currentStep >= algorithmSteps.size())
            {
                finished = true;
                return;
            }
            AlgorithmStep step = algorithmSteps.get(currentStep++);
            animate(step);
        }

        private void runPreviousStep()
        {
            if(currentStep < 1)
            {
                return;
            }
            AlgorithmStep step = algorithmSteps.get(--currentStep);
            animate(step);
        }

        private void runAuto()
        {
            for(AlgorithmStep step : algorithmSteps)
            {
                try
                {
                    synchronized (this)
                    {
                        while (suspended)
                            this.wait();
                    }
                    animate(step);
                    Thread.sleep(500);
                }
                catch (Exception e)
                {
                    return;
                }
                //setBarSortedOk(step.getBarOk());
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
            algorithmSteps = new ArrayList<>();
            AlgorithmStep step = null;
            //algorithmSteps.add(new AlgorithmStep(getResources().getString(R.string.bubble_intro_0)));
            //algorithmSteps.add(new AlgorithmStep(getResources().getString(R.string.bubble_intro_1)));
            //algorithmSteps.add(new AlgorithmStep(getResources().getString(R.string.bubble_intro_2)));
            //algorithmSteps.add(new AlgorithmStep(getResources().getString(R.string.bubble_intro_3)));
            step = new AlgorithmStep(-1, -1, false);
            step.setCodeLine(0);
            algorithmSteps.add(step);

            step = new AlgorithmStep(-1, -1, false);
            step.setCodeLine(1);
            algorithmSteps.add(step);

            step = new AlgorithmStep(-1, -1, false);
            step.setCodeLine(2);
            algorithmSteps.add(step);

            int size = vet.length, aux;
            for(int i = 0; i < size; i++)
            {
                for(int j = 0; j < size-1-i; j++)
                {
                    if(vet[j] > vet[j+1])
                    {
                        step = new AlgorithmStep(vet[j], vet[j+1], false);
                        step.setStepDescription("Comparando valores " + vet[j] + " e " + vet[j+1]);
                        algorithmSteps.add(step);
                        step = new AlgorithmStep(vet[j], vet[j+1], true);
                        step.setStepDescription("Como " + vet[j] + " > " + vet[j+1] + ", trocam-se os valores no vetor");
                        algorithmSteps.add(step);
                        step.setCodeLine(4);
                        aux = vet[j];
                        vet[j] = vet[j+1];
                        vet[j+1] = aux;
                    }
                    else
                    {
                        step = new AlgorithmStep(vet[j], vet[j+1], false);
                        step.setStepDescription("Comparando valores " + vet[j] + " e " + vet[j+1]);
                        step.setCodeLine(3);
                        algorithmSteps.add(step);
                    }
                }
                if(step != null)
                    step.setBarOk(vet[size-i-1]);
            }
        }

        private void animate(AlgorithmStep step)
        {
            showTextInfo(step);
            if (step.getPosition1() != -1)
                animateBars(step);
            if (step.getCodeLine() != -1)
                animateCode(step);
        }

        private void animateBars(final AlgorithmStep step)
        {
            if(!step.getAnimate()) return;
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
                        float x1 = b1.getX();
                        float x2 = b2.getX();
                        b1.animate()
                                .x(x2)
                                .setStartDelay(1000)
                                .setDuration(500);

                        b2.animate()
                                .x(x1)
                                .setStartDelay(1000)
                                .setDuration(500);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                mp.start();
                            }
                        }, 1000);
                    }
                    //tvInfo.setText(step.getStepDescription());
                    //setBarSortedOk(step.getBarOk());
                }
            });
        }

        private void showTextInfo(final AlgorithmStep step)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvInfo.setText(step.getStepDescription());
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

    private void animateCode(AlgorithmStep step)
    {
        int lineNumber = step.getCodeLine();
        if (lineNumber != -1)
        {
            final TextView tvLineCode = algorithmCodeLines.get(lineNumber);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvLineCode.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                }
            });
        }
    }
}
