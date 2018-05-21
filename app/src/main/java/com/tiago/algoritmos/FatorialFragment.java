package com.tiago.algoritmos;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class FatorialFragment extends Fragment
{
    private Activity activity;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private List<ViewPagerModel> contents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_fatorial, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        activity = getActivity();
        if (activity != null)
            activity.setTitle("Fatorial");

        viewPager = view.findViewById(R.id.fat_viewpager);
        contents = new ArrayList<>();
        for (int k = 0; k < 10; k++)
        {
            contents.add(new ViewPagerModel(k));
        }
        adapter = new ViewPagerAdapter(contents, activity);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ViewPagerStack());

        Button button = view.findViewById(R.id.bt_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                contents.add(new ViewPagerModel(contents.size()));
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(contents.size()-1);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class ViewPagerStack implements ViewPager.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position)
        {
            if (position >= 0)
            {
                page.setScaleX((float) (1f - 0.05*position));
                page.setScaleY(1f);
                page.setTranslationX(-page.getWidth()*position);
                page.setTranslationY(-10*position);
            }
        }
    }
}







