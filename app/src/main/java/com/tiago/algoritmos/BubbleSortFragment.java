package com.tiago.algoritmos;

import android.annotation.TargetApi;
import android.content.Context;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.florent37.viewtooltip.ViewTooltip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tiago.algoritmos.R.raw.swap;

public class BubbleSortFragment extends Fragment
{
    private static final int MILLIS_DELAY = 2000;
    private LinearLayout barsContainer;
    private ViewGroup container;
    private int vet[], curI, curJ;//current value of i and j
    private View rootView;
    private BubbleSortThread bubbleSortThread;
    private MediaPlayer mp;
    private List<AlgorithmStep> algorithmSteps;
    private TextView tvInfo;
    private List<TextView> algorithmCodeLines;
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_bubble, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        getActivity().setTitle("BubbleSort");

        this.container = container;

        barsContainer = (LinearLayout) view.findViewById(R.id.bars_container);

        addBars(8);

        rootView = view;

        tvInfo = (TextView)rootView.findViewById(R.id.tv_info);

        setHasOptionsMenu(true);

        //cardCode = (CardView) rootView.findViewById(R.id.card_code);

        ViewGroup codeContainer = view.findViewById(R.id.code_container);

        algorithmCodeLines = new ArrayList<>(7);

        for (int k=0; k < 7; k++)
        {
            TextView tvCodeLine = new TextView(view.getContext());
            if (k == 5)//call swap function in bubble sort
            {
                tvCodeLine.setClickable(true);
                tvCodeLine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        layoutInflater = (LayoutInflater)rootView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        if (layoutInflater != null) {
                            ViewGroup popupContainer = (ViewGroup) layoutInflater.inflate(R.layout.bubble_swap_func, null);
                            setupPopupWindow(popupContainer, v);
                        }
                        //cardCode.setBackgroundColor(getResources().getColor(R.color.lightGrayBackgroundColor));
                    }

                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    private void setupPopupWindow(ViewGroup popupContainer, View v)
                    {
                        TextView tvFuncCodeLine;
                        ViewGroup codeFuncContainer = popupContainer.findViewById(R.id.func_container);
                        for (int p=0; p < 5; p++)
                        {
                            tvFuncCodeLine = new TextView(rootView.getContext());
                            tvFuncCodeLine.setTypeface(Typeface.createFromAsset(rootView.
                                    getContext().getAssets(),"FiraMono-Medium.otf"));
                            int stringId = getResources().getIdentifier("swap_func_"+p,
                                    "string", getContext().getPackageName());
                            tvFuncCodeLine.setText(Html.fromHtml(getString(stringId)));
                            codeFuncContainer.addView(tvFuncCodeLine);
                        }
                        popupWindow = new PopupWindow(popupContainer, codeFuncContainer.getLayoutParams().width,
                                codeFuncContainer.getLayoutParams().height, true);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            popupWindow.setElevation(4f);

                        /*popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                cardCode.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                        });*/

                        popupWindow.showAsDropDown(v,
                                (int)v.getX(), codeFuncContainer.getLayoutParams().height,
                                Gravity.CENTER_HORIZONTAL);
                    }
                });
            }
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
        ImageView playPause = rootView.findViewById(R.id.play_pause);
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
                        ((ImageView)view).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pause));
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
            AlgorithmStep step = algorithmSteps.get(currentStep), prevStep=null;
            if (currentStep > 0)
                prevStep = algorithmSteps.get(currentStep-1);
            currentStep++;
            animate(step, prevStep);
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
            for(int k = 0; k < algorithmSteps.size(); k++)
            {
                try
                {
                    synchronized (this)
                    {
                        while (suspended)
                            this.wait();
                    }
                    if ( k > 0)
                        animate(algorithmSteps.get(k), algorithmSteps.get(k-1));
                    else
                        animate(algorithmSteps.get(0), null);

                    Thread.sleep(MILLIS_DELAY);
                }
                catch (Exception e)
                {
                    return;
                }
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
            int size = vet.length, aux;
            for(int i = 0; i < size; i++)
            {
                step = new AlgorithmStep(i, -1);
                step.setCodeLine(2);
                algorithmSteps.add(step);
                for(int j = 0; j < size-1-i; j++)
                {
                    step = new AlgorithmStep(i, j);
                    step.setCodeLine(3);
                    algorithmSteps.add(step);
                    if(vet[j] > vet[j+1])
                    {
                        step = new AlgorithmStep(vet[j], vet[j+1], false);
                        step.setStepDescription("Comparando valores " + vet[j] + " e " + vet[j+1]);
                        step.setCodeLine(4);
                        algorithmSteps.add(step);

                        step = new AlgorithmStep(vet[j], vet[j+1], true);
                        step.setStepDescription("Como " + vet[j] + " > " + vet[j+1] + ", trocam-se os valores no vetor");
                        algorithmSteps.add(step);
                        step.setCodeLine(5);
                        aux = vet[j];
                        vet[j] = vet[j+1];
                        vet[j+1] = aux;
                    }
                    else
                    {
                        step = new AlgorithmStep(vet[j], vet[j+1], false);
                        step.setStepDescription("Comparando valores " + vet[j] + " e " + vet[j+1]);
                        step.setCodeLine(4);
                        algorithmSteps.add(step);
                    }
                }
            }
            step = new AlgorithmStep(-1, -1, false);
            step.setCodeLine(6);
            algorithmSteps.add(step);
        }

        private void animate(AlgorithmStep step)
        {
            animate(step, null);
        }

        private void animate(AlgorithmStep step, AlgorithmStep prevStep)
        {
            showTextInfo(step);
            if (step.getArrValue1() != -1)
                animateBars(step);
            if (step.getCodeLine() != -1)
                animateCode(step, prevStep);
        }

        private void animateBars(final AlgorithmStep step)
        {
            if(!step.getAnimate()) return;
            if (!isAdded()) return;

            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    final View b1 = barsContainer.findViewWithTag(step.getArrValue1());
                    final View b2 = barsContainer.findViewWithTag(step.getArrValue2());
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
                                .setStartDelay(250)
                                .setDuration(750);

                        b2.animate()
                                .x(x1)
                                .setStartDelay(250)
                                .setDuration(750);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                mp.start();
                            }
                        }, 500);

                        handler.postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                b1.findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.gray));
                                b2.findViewById(R.id.bar).setBackgroundColor(getResources().getColor(R.color.gray));
                            }
                        }, 1300);

                    }
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

    private void animateCode(final AlgorithmStep step, final AlgorithmStep previous)
    {
        final int lineNumber = step.getCodeLine();
        if (lineNumber != -1)
        {
            final TextView tvLineCode = algorithmCodeLines.get(lineNumber);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    if (previous!=null)
                    {
                        TextView tvLineCodePrev = algorithmCodeLines.get(previous.getCodeLine());
                        tvLineCodePrev.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    tvLineCode.setBackgroundColor(getResources().getColor(R.color.lightCodeHighlight));
                    if(lineNumber == 3)
                    {
                        ViewTooltip
                                .on(tvLineCode)
                                .autoHide(true, 2000)
                                .position(ViewTooltip.Position.TOP)
                                .text("j = " + step.getJ())
                                .color(getResources().getColor(R.color.colorPrimaryLight))
                                .textTypeFace(Typeface.defaultFromStyle(Typeface.BOLD))
                                .textColor(getResources().getColor(R.color.primaryText))
                                .show();
                    }
                    else if(lineNumber == 2)
                    {
                        ViewTooltip
                                .on(tvLineCode)
                                .autoHide(true, 2000)
                                .position(ViewTooltip.Position.TOP)
                                .text("i = " + step.getI())
                                .color(getResources().getColor(R.color.colorPrimaryLight))
                                .textColor(getResources().getColor(R.color.primaryText))
                                .textTypeFace(Typeface.defaultFromStyle(Typeface.BOLD))
                                .show();
                    }
                    else if(lineNumber == 4)
                    {
                        ViewTooltip
                                .on(tvLineCode)
                                .autoHide(true, 2000)
                                .position(ViewTooltip.Position.TOP)
                                .text(step.getArrValue1() + " > " + step.getArrValue2() + " ?")
                                .color(getResources().getColor(R.color.colorPrimaryLight))
                                .textColor(getResources().getColor(R.color.primaryText))
                                .textTypeFace(Typeface.defaultFromStyle(Typeface.BOLD))
                                .show();
                    }
                }
            });
        }
    }
}
