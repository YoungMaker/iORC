package edu.ycp.cs482.iorc.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;

import edu.ycp.cs482.iorc.Fragments.MasterFlows.AlignmentReligionDetailFragment;
import edu.ycp.cs482.iorc.Apollo.MyApolloClient;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.VersionInfoTypeQuery;
import edu.ycp.cs482.iorc.fragment.VersionInfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * An activity representing a list of AlignmentsReligions. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link AlignmentReligionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class AlignmentReligionListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private SimpleItemRecyclerViewAdapter mSimpleAdapter;
    private boolean mTwoPane;
    private boolean showReligion;
    private static final String CREATION_DATA = "CREATION_DATA";
    private String ARG_BOOL_KEY = "RELIGION_SWITCH";
    private VersionInfoTypeQuery.GetVersionInfoType versionInfoData;
    private List<VersionInfoData.InfoList> infoDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alignmentreligion_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();*/
//            }
//        });
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            if(extra.getBoolean(ARG_BOOL_KEY)){
                //indicate a switch in values
                showReligion = true;
                setTitle(getResources().getString(R.string.title_religion));
                getIntent().removeExtra(ARG_BOOL_KEY);
            }else {
                setTitle(getResources().getString(R.string.title_alignment));
            }
        }

        if(!showReligion){
            getAlignDeity("4e", "alignment");
        }else if(showReligion){
            getAlignDeity("4e", "deity");
        }


        if (findViewById(R.id.alignmentreligion_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        HashMap<String, String> creationMap = (HashMap<String, String>) extra.getSerializable(CREATION_DATA);
        Log.d("CHARACTER CREATION DATA","DATA: " + creationMap);

        //create new simple adapter for recycler view
        mSimpleAdapter = new SimpleItemRecyclerViewAdapter(this, infoDataList, mTwoPane, showReligion, creationMap);

        View recyclerView = findViewById(R.id.alignmentreligion_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    //Create the menu button on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.quit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.quit){
            Intent intent = new Intent( AlignmentReligionListActivity.this, CharacterListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    //query for getting alignment/deities
    private void getAlignDeity(String version, String type){
        MyApolloClient.getMyApolloClient().query(
                VersionInfoTypeQuery.builder().version(version).type(type).build())
                .enqueue(new ApolloCall.Callback<VersionInfoTypeQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<VersionInfoTypeQuery.Data> response) {
                        versionInfoData = response.data().getVersionInfoType();
                        AlignmentReligionListActivity.this.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {

                                 infoDataList.addAll(versionInfoData.fragments()
                                         .versionInfoData().infoList());
                                 Log.d("INFO_LIST", infoDataList.toString());
                                 refreshView();
                             }

                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mSimpleAdapter);
        //divide items in list
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL); //this should probably get the layoutManager's preference.
        recyclerView.addItemDecoration(itemDecor);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final AlignmentReligionListActivity mParentActivity;
        private final List<VersionInfoData.InfoList> mValues;
        private final boolean mTwoPane;
        private final boolean isReligion;
        private final String ARG_EXTRA_NAME = "isReligion";
        private final HashMap<String, String> mCreationData;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VersionInfoData.InfoList item = (VersionInfoData.InfoList) view.getTag();
                Panes(view, item);
            }
        };

        SimpleItemRecyclerViewAdapter(AlignmentReligionListActivity parent,
                                      List<VersionInfoData.InfoList> infoLists,
                                      boolean twoPane, boolean isReligion,
                                      HashMap<String, String> creationMap) {
            mValues = infoLists;
            mParentActivity = parent;
            mTwoPane = twoPane;
            this.isReligion = isReligion;
            mCreationData = creationMap;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.alignmentreligion_list_content, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mContentView.setText(mValues.get(position).name());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            Log.d("PASSED_LIST", "" + mValues.size());
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }

        public void Panes(View view, VersionInfoData.InfoList item){
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(AlignmentReligionDetailFragment.ARG_ITEM_ID, item.id());
                arguments.putString(AlignmentReligionDetailFragment.ARG_ITEM,
                        (new Gson()).toJson(item));
                if(isReligion){
                    arguments.putBoolean(ARG_EXTRA_NAME,true);
                }else{
                    arguments.putBoolean(ARG_EXTRA_NAME,false);
                }
                AlignmentReligionDetailFragment fragment = new AlignmentReligionDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.alignmentreligion_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, AlignmentReligionDetailActivity.class);
                intent.putExtra(AlignmentReligionDetailFragment.ARG_ITEM_ID, item.id());
                intent.putExtra(AlignmentReligionDetailFragment.ARG_ITEM,
                        (new Gson()).toJson(item));
                if(isReligion){
                    intent.putExtra(ARG_EXTRA_NAME,true);
                }else{
                    intent.putExtra(ARG_EXTRA_NAME,false);
                }

                intent.putExtra(CREATION_DATA, mCreationData);
                context.startActivity(intent);
            }
        }


    }

    public void refreshView(){
        mSimpleAdapter.notifyDataSetChanged();
    }
}
