package com.tiago.algoritmos;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
        DrawerItem[] myDataset = new DrawerItem[]
        {
            new DrawerItem("Recursão", DrawerItem.TYPE_TITLE),
            new DrawerItem("Fibonacci", DrawerItem.TYPE_ITEM),
            new DrawerItem("Fatorial", DrawerItem.TYPE_ITEM),

            new DrawerItem("Ordenação", DrawerItem.TYPE_TITLE),
            new DrawerItem("BubbleSort", DrawerItem.TYPE_ITEM),
            new DrawerItem("InsertionSort", DrawerItem.TYPE_ITEM),
            new DrawerItem("SelectionSort", DrawerItem.TYPE_ITEM),
            new DrawerItem("QuickSort", DrawerItem.TYPE_ITEM),

            new DrawerItem("Grafos", DrawerItem.TYPE_TITLE),
            new DrawerItem("GraphFragment", DrawerItem.TYPE_ITEM)
        };
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

        fragmentMap.put("Fatorial", new FatorialFragment());
        fragmentMap.put("BubbleSort", new BubbleSortFragment());
        fragmentMap.put("QuickSort", new QuickSortFragment());
        fragmentMap.put("InsertionSort", new InsertionSortFragment());
        fragmentMap.put("SelectionSort", new SelectionSortFragment());
        fragmentMap.put("GraphFragment", new GraphFragment());

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

    private class RecyclerDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        private DrawerItem[] mDataset;
        private View lastClicked;

        RecyclerDrawerAdapter(DrawerItem[] myDataset) {
            mDataset = myDataset;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
            // create a new view
            View view;
            switch (viewType)
            {
                case DrawerItem.TYPE_ITEM:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.drawer_row_topic, parent, false);
                    return new ViewHolderTopic(view);
                default://title
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.drawer_row_title, parent, false);
                    return new ViewHolderTitle(view);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mDataset[position].getItemType();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            int pos = holder.getAdapterPosition();
            short type = mDataset[pos].getItemType();

            switch (type)
            {
                case DrawerItem.TYPE_ITEM:
                    addItemTypeTopic(holder, pos);
                    break;
                case DrawerItem.TYPE_TITLE:
                    addItemTypeTitle(holder, pos);
                    break;
            }
        }

        private void addItemTypeTitle(RecyclerView.ViewHolder viewHolder, int position)
        {
            final String itemText = mDataset[position].getItemText();
            ViewHolderTitle holder = (ViewHolderTitle)viewHolder;
            holder.mTextView.setText(itemText);
        }

        private void addItemTypeTopic(RecyclerView.ViewHolder viewHolder, int position)
        {
            final String itemText = mDataset[position].getItemText();
            ViewHolderTopic holder = (ViewHolderTopic)viewHolder;
            holder.mTextView.setText(itemText);
            holder.mTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(lastClicked != null)
                    {
                        ((TextView)lastClicked).setTextColor(getResources().getColor(R.color.colorPrimary));
                        ((TextView)lastClicked).setTypeface(Typeface.DEFAULT);
                        lastClicked.setBackgroundColor(getResources().getColor(R.color.transparent));
                    }
                    lastClicked = view;
                    replaceFragment(fragmentMap.get(itemText));
                    drawerLayout.closeDrawers();
                    ((TextView)view).setTextColor(getResources().getColor(R.color.colorPrimary));
                    ((TextView)view).setTypeface(Typeface.DEFAULT_BOLD);
                    view.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        class ViewHolderTopic extends RecyclerView.ViewHolder
        {
            TextView mTextView;
            ViewHolderTopic(View v)
            {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.tv_row_topic);
            }
        }

        class ViewHolderTitle extends RecyclerView.ViewHolder
        {
            TextView mTextView;
            ViewHolderTitle(View v)
            {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.tv_title);
            }
        }
    }

    private class DrawerItem
    {
        public static final short TYPE_TITLE = 0;
        public static final short TYPE_ITEM = 1;
        private String itemText;
        private short itemType;

        public DrawerItem (String text, short type)
        {
            this.itemText = text;
            this.itemType = type;
        }

        public short getItemType() {
            return itemType;
        }

        public String getItemText() {
            return itemText;
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
