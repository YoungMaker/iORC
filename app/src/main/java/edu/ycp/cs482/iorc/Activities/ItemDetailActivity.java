package edu.ycp.cs482.iorc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.HashMap;

import edu.ycp.cs482.iorc.Fragments.MasterFlows.ItemDetailFragment;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.fragment.ItemData;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    private static final String CREATION_DATA = "CREATION_DATA";
    private ItemData mItem;
    private HashMap<String, String> creationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        //get extras from item list activity
        final Bundle extra = getIntent().getExtras();

        if(extra != null){
            if(extra.containsKey(CREATION_DATA)){
                creationData = (HashMap<String, String>) extra.getSerializable(CREATION_DATA);
            }
            if(extra.containsKey(ItemDetailFragment.ARG_ITEM)){
                mItem = (new Gson()).fromJson( extra.getString(ItemDetailFragment.ARG_ITEM), ItemData.class);
            }
        }


        //FAB button press triggers result of selection to be sent to list activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //TODO: this should add the item in detail to the character
                Snackbar.make(view, "TODO: This should add an item to the character. ", Snackbar.LENGTH_LONG)
                        .setAction("Add", null).show();
                Intent itemSelected = new Intent();
                itemSelected.putExtra("result", mItem.id());
                setResult(RESULT_OK, itemSelected);
                finish();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, ItemListActivity.class);
            intent.putExtra(CREATION_DATA, creationData);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
