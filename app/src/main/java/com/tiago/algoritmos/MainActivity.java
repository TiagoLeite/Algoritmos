package com.tiago.algoritmos;

import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDrawer;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
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
        String[] myDataset = new String[]{"BubbleSort", "MergeSort", "QuickSort"};
        RecyclerDrawerAdapter mAdapter = new RecyclerDrawerAdapter(myDataset);
        recyclerViewDrawer.setAdapter(mAdapter);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

            holder.mTextView.setText(mDataset[position]);
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(lastClicked != null)
                    {
                        ((TextView)lastClicked).setTextColor(getResources().getColor(R.color.colorPrimary));
                        lastClicked.setBackgroundColor(getResources().getColor(R.color.transparent));
                    }
                    lastClicked = view;
                    Toast.makeText(view.getContext(), "LALALALA", Toast.LENGTH_LONG).show();
                    ((TextView)view).setTextColor(getResources().getColor(R.color.colorPrimary));
                    view.setBackgroundColor(getResources().getColor(R.color.gray));
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
