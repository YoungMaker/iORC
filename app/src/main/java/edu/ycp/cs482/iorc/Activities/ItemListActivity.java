package edu.ycp.cs482.iorc.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.gson.reflect.TypeToken;

import edu.ycp.cs482.iorc.AddItemToCharMutation;
import edu.ycp.cs482.iorc.Apollo.Query.Exception.AuthQueryException;
import edu.ycp.cs482.iorc.Apollo.Query.Exception.QueryException;
import edu.ycp.cs482.iorc.Apollo.Query.QueryControllerProvider;
import edu.ycp.cs482.iorc.Apollo.Query.QueryData;
import edu.ycp.cs482.iorc.Fragments.MasterFlows.ItemDetailFragment;
import edu.ycp.cs482.iorc.Apollo.MyApolloClient;
import edu.ycp.cs482.iorc.PurchaseItemMutation;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.VersionItemsByTypeQuery;
import edu.ycp.cs482.iorc.VersionItemsQuery;
import edu.ycp.cs482.iorc.fragment.ItemData;
import edu.ycp.cs482.iorc.type.ObjType;

import java.lang.reflect.Type;
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
    private static final String POP_ERROR = "ERR_POP";
    private boolean mTwoPane;
    private HashMap<String, String> creationMap;
    private VersionItemsQuery.Data versionItemQueryData;
    private List<ItemData> itemList = new ArrayList<>();
    public static final String CHAR_ID = "CHAR_ID";
    private static final int ITEM_SELECTION_REQ_CODE = 1;
    private static String charIDVal;
    private static String version = "4e";
    private static ObjType type = ObjType.ITEM;
    private List<VersionItemsByTypeQuery.GetVersionItemType> versionItemTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        //create a bundle of extras
        Bundle extra = getIntent().getExtras();

        //create our character creation data map
        if(extra != null){
            if(extra.containsKey(CREATION_DATA)){
                creationMap = (HashMap<String, String>) extra.getSerializable(CREATION_DATA);
            }
            if(extra.containsKey(CHAR_ID)){
                charIDVal = extra.getString(CHAR_ID);
            }
        }

        Log.d("CHARACTER CREATION DATA","DATA: " + creationMap);

        getVersionItemTypes(version, type);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        //getAllItems();

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

    private void getVersionItemTypes(final String version, ObjType type){
        try{
            QueryControllerProvider.getInstance().getQueryController().getVersionItemsByType(type, version, getApplicationContext())
                    .enqueue(new ApolloCall.Callback<VersionItemsByTypeQuery.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<VersionItemsByTypeQuery.Data> response) {
                            try{
                                QueryData queryData = QueryControllerProvider.getInstance().getQueryController().parseGetVersionItemsByType(version, response);
                                processItemTypes(queryData);
                            }catch(AuthQueryException e){
                                returnToLogin();
                            }catch(QueryException e){
                                popQueryError();
                            }

                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            popCommError();
                        }
                    });
        }catch(AuthQueryException e){
            returnToLogin();
        }

    }

    private void processItemTypes(QueryData queryData){
        final String data = queryData.getGsonData();
        ItemListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Type typeList = new TypeToken<List<VersionItemsByTypeQuery.GetVersionItemType>>(){}.getType();
                versionItemTypes = new Gson().fromJson(data, typeList);

                for(int i = 0; i < versionItemTypes.size(); i++){
                    VersionItemsByTypeQuery.GetVersionItemType itemdata = versionItemTypes.get(i);
                    itemList.add(itemdata.fragments().itemData());
                }

                refreshView();
            }
        });

    }

    private void returnToLogin(){

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(POP_ERROR, true);
        startActivity(intent);
        finish();
    }

    private void popCommError(){
        ItemListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(ItemListActivity.this).create();
                alertDialog.setTitle("Get Characters Failed");
                alertDialog.setMessage("Get characters attempt failed: Communication Failed");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void popQueryError(){
        ItemListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(ItemListActivity.this).create();
                alertDialog.setTitle("Get Characters Failed");
                alertDialog.setMessage("Get characters attempt failed: Query Failed");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    //all items
//    private void getAllItems(){
//        MyApolloClient.getMyApolloClient().query(
//            VersionItemsQuery.builder().version("4e").build())
//                .enqueue(new ApolloCall.Callback<VersionItemsQuery.Data>() {
//                    @Override
//                    public void onResponse(@Nonnull Response<VersionItemsQuery.Data> response) {
//
//                        versionItemQueryData = response.data();
//
//                        ItemListActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                int numItems = versionItemQueryData.getVersionItems().size();
//                                List<VersionItemsQuery.GetVersionItem> versionItems =
//                                        versionItemQueryData.getVersionItems();
//                                for(int i = 0; i < numItems; i++){
//                                    itemList.add(versionItems.get(i).fragments().itemData());
//                                }
//                                refreshView();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(@Nonnull ApolloException e) {
//
//                    }
//                });
//    }

    private void purchaseItemforChar(String itemID){
        MyApolloClient.getMyApolloClient().mutate(
            PurchaseItemMutation.builder().itemID(itemID).id(charIDVal).build())
               .enqueue(new ApolloCall.Callback<PurchaseItemMutation.Data>() {
                   @Override
                   public void onResponse(@Nonnull Response<PurchaseItemMutation.Data> response) {
                       Log.d("Purchase_ITEM_RESPONSE", "COMPLETE");
                       //TODO reload character data so that updated inventory is available
                   }

                   @Override
                   public void onFailure(@Nonnull ApolloException e) {
                       Log.d("Purchase_ITEM_RESPONSE", "ERROR");
                   }
               });
    }

    private void addItemtoCharacter(String itemID){
        MyApolloClient.getMyApolloClient().mutate(
                AddItemToCharMutation.builder().itemId(itemID).charID(charIDVal).build())
                .enqueue(new ApolloCall.Callback<AddItemToCharMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<AddItemToCharMutation.Data> response) {
                        Log.d("ADD_ITEM_RESPONSE", "COMPLETE");
                        //TODO reload character data so that updated inventory is available
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
            purchaseItemforChar(dataReturn);
        }
    }
}
