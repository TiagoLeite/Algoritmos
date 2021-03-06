package com.tiago.algoritmos;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class GraphFragment extends Fragment
{
    private View rootView;
    private ViewGroup container;
    private LinearLayout barsContainer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_graph, container, false);
        this.container = container;
        getActivity().setTitle("GraphFragment");

        Button reset = (Button) rootView.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GraphView)rootView.findViewById(R.id.graph)).resetLines();
            }
        });
        return rootView;
    }
}
