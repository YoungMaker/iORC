package edu.ycp.cs482.iorc.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
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

import edu.ycp.cs482.iorc.AddItemToCharMutation;
import edu.ycp.cs482.iorc.Fragments.MasterFlows.ItemDetailFragment;
import edu.ycp.cs482.iorc.Apollo.MyApolloClient;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.VersionItemsQuery;
import edu.ycp.cs482.iorc.fragment.ItemData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private SimpleItemRecyclerViewAdapter mSimpleAdapter;
    private static final String CREATION_DATA = "CREATION_DATA";
    private boolean mTwoPane;
    private HashMap<String, String> creationMap;
    private VersionItemsQuery.Data versionItemQueryData;
    private List<ItemData> itemList = new ArrayList<>();
    public static final String CHAR_ID = "CHAR_ID";
    private static final int ITEM_SELECTION_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        //create a bundle of extras
        Bundle extra = getIntent().getExtras();

        //create our character creation data map
        creationMap = (HashMap<String, String>) extra.getSerializable(CREATION_DATA);
        Log.d("CHARACTER CREATION DATA","DATA: " + creationMap);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        getAllItems();

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        //create our simple item recycler adapter add to recycler view
        mSimpleAdapter = new SimpleItemRecyclerViewAdapter(this, itemList, mTwoPane, creationMap);
        View recyclerView = findViewById(R.id.item_list);
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
            Intent intent = new Intent( ItemListActivity.this, CharacterListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
        recyclerView.setAdapter(mSimpleAdapter);
        //divide items in list
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL); //this should probably get the layoutManager's preference.
        recyclerView.addItemDecoration(itemDecor);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<ItemData> mValues;
        private final boolean mTwoPane;
        private final HashMap<String, String> mCreationData;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemData item = (ItemData) view.getTag();
                String gsonItem = (new Gson()).toJson(item);
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM, gsonItem);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {

                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM, gsonItem);
                    intent.putExtra(CREATION_DATA, mCreationData);
                    mParentActivity.startActivityForResult(intent, ITEM_SELECTION_REQ_CODE);
                    //context.startActivityResult(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<ItemData> items,
                                      boolean twoPane, HashMap<String, String> creationData) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            mCreationData = creationData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).name());
            holder.mContentView.setText(mValues.get(position).price());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
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
    }

    //item queries

    //all items
    private void getAllItems(){
        MyApolloClient.getMyApolloClient().query(
            VersionItemsQuery.builder().version("4e").build())
                .enqueue(new ApolloCall.Callback<VersionItemsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<VersionItemsQuery.Data> response) {

                        versionItemQueryData = response.data();

                        ItemListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int numItems = versionItemQueryData.getVersionItems().size();
                                List<VersionItemsQuery.GetVersionItem> versionItems =
                                        versionItemQueryData.getVersionItems();
                                for(int i = 0; i < numItems; i++){
                                    itemList.add(versionItems.get(i).fragments().itemData());
                                }
                                refreshView();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
    }

    private void addItemtoChar(String itemID, String charID){
        MyApolloClient.getMyApolloClient().mutate(
            AddItemToCharMutation.builder().itemId(itemID).charID(charID).build())
               .enqueue(new ApolloCall.Callback<AddItemToCharMutation.Data>() {
                   @Override
                   public void onResponse(@Nonnull Response<AddItemToCharMutation.Data> response) {
                       Log.d("ADD_ITEM_RESPONSE", "COMPLETE");
                   }

                   @Override
                   public void onFailure(@Nonnull ApolloException e) {
                       Log.d("ADD_ITEM_RESPONSE", "ERROR");
                   }
               });
    }

    //TODO implment other queries for specific item lookups

    //TODO create method(s) for sorting list by different parameters

    //refresh recycler view
    public void refreshView(){
        mSimpleAdapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ITEM_SELECTION_REQ_CODE && resultCode == RESULT_OK){
            String dataReturn = data.getStringExtra("result");
            Log.d("DATA_RETURN_TEST", dataReturn);
        }
    }
}
