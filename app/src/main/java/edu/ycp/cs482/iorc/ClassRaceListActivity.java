package edu.ycp.cs482.iorc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;

import edu.ycp.cs482.iorc.dummy.DummyContent;
import edu.ycp.cs482.iorc.dummy.MyApolloClient;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * An activity representing a list of ClassesRaces. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ClassRaceDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ClassRaceListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private SimpleItemRecyclerViewAdapter mSimpleAdapter;
    private boolean mTwoPane;
    private boolean showRace;
    private String ARG_BOOL_KEY = "RACE_SWITCH";
    private List<RaceVersionQuery.GetRacesByVersion> RaceResponseData;
    private HashMap<String, String> raceDetailMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrace_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        getRaces();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();*/
//                startActivity(new Intent(ClassRaceListActivity.this, AlignmentReligionListActivity.class));
//            }
//        });
        //get our bundle from when a user is finished selecting class that enables the user to then select race inside the same M/V flow
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            if(extra.getBoolean(ARG_BOOL_KEY)){
                //indicate a switch in values
                showRace = true;
                getIntent().removeExtra(ARG_BOOL_KEY);
            }
        }

        if (findViewById(R.id.classrace_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        mSimpleAdapter = new SimpleItemRecyclerViewAdapter(this, DummyContent.CLASSES, RaceResponseData, raceDetailMap, mTwoPane, showRace);
        View recyclerView = findViewById(R.id.classrace_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void getRaces(){
        MyApolloClient.getMyApolloClient().query(
            RaceVersionQuery.builder().version("4e").build()).enqueue(new ApolloCall.Callback<RaceVersionQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<RaceVersionQuery.Data> response) {
                RaceResponseData = response.data().getRacesByVersion();
                //Log.d("RESPONSE:","" + RaceResponseData);
                ClassRaceListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RaceResponseData = RaceResponseData;
                        for(int i = 0; i < RaceResponseData.size(); i++){
                            raceDetailMap.put(RaceResponseData.get(i).fragments().raceData.id(), (new Gson()).toJson(RaceResponseData.get(i).fragments().raceData()));
                        }
                        Log.d("RESPONSE:","" + raceDetailMap);
                        refreshView();
                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.d("No Response:","No acknowledgment from server");
            }
        });
    }

    //todo create class query after class data is created
    /*private void getClasses(){
        MyApolloClient.getMyApolloClient().query(
                Class
        )
    }*/

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.CLASSES, RaceResponseData, , mTwoPane, showRace));
        //divide items in list
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL); //this should probably get the layoutManager's preference.
        recyclerView.addItemDecoration(itemDecor);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final ClassRaceListActivity mParentActivity;
        private final List<DummyContent.DummyClass> mValues;
        private final List<RaceVersionQuery.GetRacesByVersion> amValues;
        private final boolean mTwoPane;
        private final boolean showRace;
        private final String ARG_EXTRA_NAME = "isRace";

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkClassRace(view);
            }
        };

        SimpleItemRecyclerViewAdapter(ClassRaceListActivity parent,
                                      List<DummyContent.DummyClass> items, List<RaceVersionQuery.GetRacesByVersion> raceItems, HashMap<String, String> raceMap,
                                      boolean twoPane, boolean showRace) {
            mValues = items;
            amValues = raceItems;
            mParentActivity = parent;
            mTwoPane = twoPane;
            this.showRace = showRace;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.classrace_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if(!showRace){
                holder.mIdView.setText(mValues.get(position).id);
                holder.mContentView.setText(mValues.get(position).name);

                holder.itemView.setTag(mValues.get(position));
            }
            else{
                holder.mIdView.setText(amValues.get(position).fragments().raceData.id());
                holder.mContentView.setText(amValues.get(position).fragments().raceData.name());

                holder.itemView.setTag(amValues.get(position));
            }
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            //count the number of class or race items in the list
            int itemCount = 0;
            if(showRace) {
                itemCount = amValues.size();
            }else if(!showRace){
                itemCount = mValues.size();
            }
            return itemCount;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }

        public void checkClassRace(View view){
            if(showRace){
                DummyContent.DummyRace raceItem = (DummyContent.DummyRace) view.getTag();
                raceTwoPanes(view, raceItem);
            }else if(!showRace){
                DummyContent.DummyClass classItem = (DummyContent.DummyClass) view.getTag();
                classTwoPanes(view, classItem);
            }
        }

        public void classTwoPanes(View view, DummyContent.DummyClass item){
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(ClassRaceDetailFragment.ARG_ITEM_ID, item.id);
                ClassRaceDetailFragment fragment = new ClassRaceDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.classrace_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ClassRaceDetailActivity.class);
                intent.putExtra(ClassRaceDetailFragment.ARG_ITEM_ID, item.id);
                intent.putExtra(ARG_EXTRA_NAME, false);

                context.startActivity(intent);
            }
        }

        public void raceTwoPanes(View view, DummyContent.DummyRace item){
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(ClassRaceDetailFragment.ARG_ITEM_ID, item.id);
                ClassRaceDetailFragment fragment = new ClassRaceDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.classrace_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ClassRaceDetailActivity.class);
                intent.putExtra(ClassRaceDetailFragment.ARG_ITEM_ID, item.id);
                intent.putExtra(ARG_EXTRA_NAME, true);

                context.startActivity(intent);
            }
        }
    }

    public void refreshView(){
        mSimpleAdapter.notifyDataSetChanged();
    }
}
