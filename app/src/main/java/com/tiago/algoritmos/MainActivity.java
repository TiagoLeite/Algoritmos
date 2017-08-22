package com.tiago.algoritmos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDrawer;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private Map<String, Fragment> fragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewDrawer = (RecyclerView) findViewById(R.id.drawer_recycler_view);
        recyclerViewDrawer.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDrawer.setLayoutManager(llm);
        String[] myDataset = new String[]{"BubbleSort", "InsertionSort", "SelectSort"};
        RecyclerDrawerAdapter mAdapter = new RecyclerDrawerAdapter(myDataset);
        recyclerViewDrawer.setAdapter(mAdapter);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        fragmentMap = new HashMap<>();

        fragmentMap.put("BubbleSort", new BubbleSortFragment());
        fragmentMap.put("InsertionSort", new InsertionSortFragment());

    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private class RecyclerDrawerAdapter extends RecyclerView.Adapter<RecyclerDrawerAdapter.ViewHolder>
    {
        private String[] mDataset;
        private View lastClicked;

        RecyclerDrawerAdapter(String[] myDataset) {
            mDataset = myDataset;
        }

        @Override
        public RecyclerDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drawer_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final int pos = holder.getAdapterPosition();
            holder.mTextView.setText(mDataset[position]);
            holder.mTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    switch (pos)
                    {
                        case 0:
                            if(lastClicked != null)
                            {
                                ((TextView)lastClicked).setTextColor(getResources().getColor(R.color.colorPrimary));
                                lastClicked.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }
                            lastClicked = view;
                            replaceFragment(fragmentMap.get("BubbleSort"));
                            drawerLayout.closeDrawers();
                            ((TextView)view).setTextColor(getResources().getColor(R.color.colorPrimary));
                            view.setBackgroundColor(getResources().getColor(R.color.gray));
                            Log.d("debug", pos+"");
                            break;
                        case 1:
                            if(lastClicked != null)
                            {
                                ((TextView)lastClicked).setTextColor(getResources().getColor(R.color.colorPrimary));
                                lastClicked.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }
                            lastClicked = view;
                            replaceFragment(fragmentMap.get("InsertionSort"));
                            drawerLayout.closeDrawers();
                            ((TextView)view).setTextColor(getResources().getColor(R.color.colorPrimary));
                            view.setBackgroundColor(getResources().getColor(R.color.gray));
                            Log.d("debug", pos+"");
                            break;
                        default:
                            break;

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView mTextView;
            ViewHolder(View v)
            {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.tv_row);
            }
        }
    }
}

/*
    final Button b1 = (Button) findViewById(R.id.button);
    final Button b2 = (Button) findViewById(R.id.button2);

    final int x1 = b1.getTop();
    final int x2 = b2.getTop();


        b1.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Animation anim = new TranslateAnimation(Animation.ABSOLUTE, Animation.ABSOLUTE,
        Animation.ABSOLUTE, b2.getBottom());
        anim.setDuration(1000);
        anim.setInterpolator(getApplicationContext(), android.R.anim.anticipate_overshoot_interpolator);
        anim.setFillAfter(true);

        Animation anim2 = new TranslateAnimation(Animation.ABSOLUTE, Animation.ABSOLUTE,
        Animation.ABSOLUTE, -b1.getBottom());

        anim2.setDuration(1000);
        anim2.setInterpolator(getApplicationContext(), android.R.anim.anticipate_overshoot_interpolator);
        anim2.setFillAfter(true);

        view.startAnimation(anim);
        b2.startAnimation(anim2);
        }
        });
*/
