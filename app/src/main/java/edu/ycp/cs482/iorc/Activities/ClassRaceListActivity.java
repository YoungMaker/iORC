package edu.ycp.cs482.iorc.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.ycp.cs482.iorc.Apollo.Query.Exception.AuthQueryException;
import edu.ycp.cs482.iorc.Apollo.Query.Exception.QueryException;
import edu.ycp.cs482.iorc.Apollo.Query.QueryControllerProvider;
import edu.ycp.cs482.iorc.Apollo.Query.QueryData;
import edu.ycp.cs482.iorc.ClassVersionQuery;
import edu.ycp.cs482.iorc.Fragments.MasterFlows.ClassRaceDetailFragment;
import edu.ycp.cs482.iorc.Apollo.MyApolloClient;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.RaceVersionQuery;
import edu.ycp.cs482.iorc.fragment.ClassData;
import edu.ycp.cs482.iorc.fragment.RaceData;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private static final String CREATION_DATA = "CREATION_DATA";
    //private List<RaceVersionQuery.GetRacesByVersion> raceResponseData;
    private List<ClassVersionQuery.GetClassesByVersion> classResponseData;
    private List<RaceData> raceResponses = new ArrayList<>();
    private List<ClassData> classResponses = new ArrayList<>();
    private HashMap<String, String> raceDetailMap = new HashMap<String, String>();
    private HashMap<String, String> classDetailMap = new HashMap<String, String>();
    private List<RaceVersionQuery.GetRacesByVersion> raceResponseData;

    //private QueryData raceQueryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrace_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        View loadingView = findViewById(R.id.loadingIcon);
        loadingView.setVisibility(View.GONE);
       // getRaces();

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
        //this will also contain our character creation data
        Bundle extra = getIntent().getExtras();
        if(extra != null && extra.getBoolean(ARG_BOOL_KEY)){
            //indicate a switch in values
            showRace = true;
            setTitle(getResources().getString(R.string.title_race));
            getRaces("4e");
            getIntent().removeExtra(ARG_BOOL_KEY);
        }
        else {
            setTitle(getResources().getString(R.string.title_class));
            getClasses("4e");
        }

        if (findViewById(R.id.classrace_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        HashMap<String, String> creationMap = (HashMap<String, String>) extra.getSerializable(CREATION_DATA);
        Log.d("CHARACTER CREATION DATA","DATA: " + creationMap);
        mSimpleAdapter = new SimpleItemRecyclerViewAdapter(this, classResponses, raceResponses, classDetailMap,raceDetailMap, mTwoPane, showRace, creationMap);
        View recyclerView = findViewById(R.id.classrace_list);
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
            Intent intent = new Intent( ClassRaceListActivity.this, CharacterListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getRaces(final String version){
        try{
            QueryControllerProvider.getInstance().getQueryController().versionRacesQuery(version, getApplicationContext())
                    .enqueue(new ApolloCall.Callback<RaceVersionQuery.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<RaceVersionQuery.Data> response) {
                            try{
                                QueryData raceQueryData = QueryControllerProvider.getInstance().getQueryController().parseRacesQuery(version, getApplicationContext(), response);
                                processQueryData(raceQueryData, "Race");
                            }catch(AuthQueryException e){
                                Log.d("AUTH_ERROR", "Invalid token during query parser");
                            }catch(QueryException e){
                                Log.d("QUERY_EXCEPTION", "Error during Query");
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            Log.d("COMM_ERROR", "onFailure: Error communicating with server");
                        }
                    });
        }catch(AuthQueryException e){
            Log.d("AUTH_ERROR", "Invalid token on query");
        }
    }

    private void getClasses(final String version){
        try{
            QueryControllerProvider.getInstance().getQueryController().versionClassesQuery(version, getApplicationContext())
                    .enqueue(new ApolloCall.Callback<ClassVersionQuery.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<ClassVersionQuery.Data> response) {
                            try {
                                QueryData classQueryData = QueryControllerProvider.getInstance().getQueryController().parseClassesQuery(version, getApplicationContext(), response);
                                processQueryData(classQueryData, "Class");
                            }catch(AuthQueryException e){
                                Log.d("AUTH_ERROR", "Invalid token during query parser");
                            }catch(QueryException e){
                                Log.d("QUERY_EXCEPTION", "Error during Query");
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            Log.d("COMMS_ERROR", "Error Communicating with server");
                        }
                    });
        }catch(AuthQueryException e){
            Log.d("AUTH_ERROR", "Invalid token on query");
        }
    }

    private void processQueryData(QueryData queryData, final String queryType){
        //final View loadingView = findViewById(R.id.loadingPanel);
        final String data = queryData.getGsonData();
        //Log.d("GSON_CHAR_DATA", data);
        ClassRaceListActivity.this.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                if(queryType.equals("Race")){
                    Type listType = new TypeToken<List<RaceVersionQuery.GetRacesByVersion>>(){}.getType();
                    raceResponseData = new Gson().fromJson(data, listType);

                    for (int i = 0; i < raceResponseData.size(); i++) {
                        RaceVersionQuery.GetRacesByVersion data = raceResponseData.get(i);
                        raceResponses.add(data.fragments().raceData());
                    }
                }else if(queryType.equals("Class")){
                    Type listType = new TypeToken<List<ClassVersionQuery.GetClassesByVersion>>(){}.getType();
                    classResponseData = new Gson().fromJson(data, listType);

                    for (int i = 0; i < classResponseData.size(); i++) {
                        ClassVersionQuery.GetClassesByVersion data = classResponseData.get(i);
                        classResponses.add(data.fragments().classData());
                    }
                }

                //Log.d("RESPONSE_DATA", characterDataResponse.toString());
                refreshView();
//                loadingView.setVisibility(View.GONE);
            }
        });

    }
//deprecated
//    private void getRaces(){
//        final View loadingView = findViewById(R.id.loadingIcon);
//        MyApolloClient.getRaceApolloClient().query(
//            RaceVersionQuery
//                    .builder().version("4e").build())
//                .httpCachePolicy(HttpCachePolicy.CACHE_FIRST)
//                .enqueue(new ApolloCall.Callback<RaceVersionQuery.Data>() {
//            @Override
//            public void onResponse(@Nonnull Response<RaceVersionQuery.Data> response) {
//                loadingView.setVisibility(View.GONE);
//                raceResponseData = response.data().getRacesByVersion();
//                //Log.d("RESPONSE:","" + RaceResponseData);
//                ClassRaceListActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        for(int i = 0; i < raceResponseData.size(); i++){
//                            raceResponses.add(raceResponseData.get(i));
//                            raceDetailMap.put(raceResponseData.get(i).fragments().raceData().id(), (new Gson()).toJson(raceResponseData.get(i)));
//                            //Log.d("ADDED TO RACE MAP:","" + raceDetailMap.get(raceResponseData.get(i)));
//                        }
//                        //Log.d("RESPONSE:","" + raceDetailMap);
//                        refreshView();
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(@Nonnull ApolloException e) {
//                Snackbar.make(findViewById(R.id.frameLayout), "Error communicating with server" , Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Log.d("No Response:","No acknowledgment from server");
//            }
//        });
//    }

    //todo create class query after class data is created
//    private void getClasses(){
//        final View loadingView = findViewById(R.id.loadingIcon);
//        MyApolloClient.getClassApolloClient().query(
//                ClassVersionQuery.builder().version("4e").build())
//                .httpCachePolicy(HttpCachePolicy.CACHE_FIRST)
//                .enqueue(new ApolloCall.Callback<ClassVersionQuery.Data>() {
//            @Override
//            public void onResponse(@Nonnull Response<ClassVersionQuery.Data> response) {
//                loadingView.setVisibility(View.GONE);
//                classResponseData = response.data().getClassesByVersion();
//                Log.d("CLASS RESPONSE:","" + classResponseData);
//                ClassRaceListActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        for(int i = 0; i < classResponseData.size(); i++){
//                            classResponses.add(classResponseData.get(i));
//                            classDetailMap.put(classResponseData.get(i).fragments().classData().id(), (new Gson()).toJson(classResponseData.get(i)));
//                            //Log.d("ADDED TO RACE MAP:","" + raceDetailMap.get(raceResponseData.get(i)));
//                        }
//                        //Log.d("RESPONSE:","" + raceDetailMap);
//                        refreshView();
//
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(@Nonnull ApolloException e) {
//                Snackbar.make(findViewById(R.id.frameLayout), "Error communicating with server" , Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Log.d("No Response:","No acknowledgment from server");
//            }
//        });
//    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.CLASSES, RaceResponseData, , mTwoPane, showRace));
        recyclerView.setAdapter(mSimpleAdapter);
        //divide items in list
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL); //this should probably get the layoutManager's preference.
        recyclerView.addItemDecoration(itemDecor);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final ClassRaceListActivity mParentActivity;
        private final List<ClassData> mValues;
        private final List<RaceData> amValues;
        private final boolean mTwoPane;
        private final boolean showRace;
        private final String ARG_EXTRA_NAME = "isRace";
        HashMap<String, String> mCreationData;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkClassRace(view);
            }
        };

        SimpleItemRecyclerViewAdapter(ClassRaceListActivity parent,
                                      List<ClassData> items, List<RaceData> raceItems,
                                      HashMap<String, String> classMap, HashMap<String, String> raceMap,
                                      boolean twoPane, boolean showRace,
                                      HashMap<String, String> creationData) {
            mValues = items;
            //Log.d("CLASSLIST LENGTH:","Size: " + items.size());
            //Log.d("RACELIST LENGTH:","Size: " + raceItems.size());
            amValues = raceItems;
            mParentActivity = parent;
            mTwoPane = twoPane;
            this.showRace = showRace;
            mCreationData = creationData;
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
                //holder.mIdView.setText(mValues.get(position).id);
                holder.mContentView.setText(mValues.get(position).name());

                holder.itemView.setTag(mValues.get(position));
            }
            else{
                //holder.mIdView.setText(amValues.get(position).fragments().raceData.id());
                holder.mContentView.setText(amValues.get(position).name());

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
            }else {
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
                RaceData raceItem = (RaceData) view.getTag();
                raceTwoPanes(view, raceItem);
            }else if(!showRace){
                ClassData classItem = (ClassData) view.getTag();
                classTwoPanes(view, classItem);
            }
        }

        public void classTwoPanes(View view, ClassData item){
            //Log.d("CHECK CLASS ID:","class ID: " + item.id());
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putSerializable(ClassRaceDetailFragment.CREATION_DATA, mCreationData);
                ClassRaceDetailFragment fragment = new ClassRaceDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.classrace_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ClassRaceDetailActivity.class);
                intent.putExtra(ClassRaceDetailFragment.CREATION_DATA, mCreationData);
                intent.putExtra(ClassRaceDetailFragment.ARG_CLASS, serializeClassData(item));
                intent.putExtra(ARG_EXTRA_NAME, false);

                context.startActivity(intent);
            }
        }

        public void raceTwoPanes(View view, RaceData item){
            //add our race map and selected id to the arguments
            //Log.d("CHECK RACE ID:","race ID: " + item.id());

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putSerializable(ClassRaceDetailFragment.CREATION_DATA, mCreationData);
                ClassRaceDetailFragment fragment = new ClassRaceDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.classrace_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ClassRaceDetailActivity.class);
                intent.putExtra(ClassRaceDetailFragment.CREATION_DATA, mCreationData);
                intent.putExtra(ClassRaceDetailFragment.ARG_RACE, serializeRaceData(item));
                intent.putExtra(ARG_EXTRA_NAME, true);

                context.startActivity(intent);
            }
        }

        //serialize data for the selected character
        private String serializeRaceData(RaceData raceData){
            return new Gson().toJson(raceData);
        }

        private String serializeClassData(ClassData classData){
            return new Gson().toJson(classData);
        }
    }

    public void refreshView(){
        mSimpleAdapter.notifyDataSetChanged();
    }
}
