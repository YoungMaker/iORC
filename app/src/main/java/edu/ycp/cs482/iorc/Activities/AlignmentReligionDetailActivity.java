package edu.ycp.cs482.iorc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import java.util.HashMap;

import edu.ycp.cs482.iorc.Fragments.MasterFlows.AlignmentReligionDetailFragment;
import edu.ycp.cs482.iorc.R;

/**
 * An activity representing a single AlignmentReligion detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AlignmentReligionListActivity}.
 */
public class AlignmentReligionDetailActivity extends AppCompatActivity {
    private boolean showReligion;
    private static final String CREATION_DATA = "CREATION_DATA";
    private String ARG_BOOL_KEY = "isReligion";
    private String ARG_EXTRA_NAME = "RELIGION_SWITCH";
    private String ARG_FRAG_BOOL = "SHOW_RELIGION";
    private HashMap<String, String> creationData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alignmentreligion_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        final Bundle extra = getIntent().getExtras();
        if(extra != null && extra.getBoolean(ARG_BOOL_KEY)){
            showReligion = true;
        } else {
           showReligion = false;
        }

        //retrieve our character creation data
        creationData = (HashMap<String, String>) extra.getSerializable(CREATION_DATA);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //handle selection of alignment/religion being made
                Intent intent;
                String insertData = (String) extra.get(AlignmentReligionDetailFragment.ARG_ITEM);
                if(!showReligion){
                    creationData.put("ALIGNMENT", insertData);
                    intent = new Intent(AlignmentReligionDetailActivity.this, AlignmentReligionListActivity.class);
                    intent.putExtra(ARG_EXTRA_NAME, true);
                    intent.putExtra(CREATION_DATA, creationData);
                    startActivity(intent);

                }else if(showReligion){
                    intent = new Intent(AlignmentReligionDetailActivity.this, CharacterListActivity.class);
                    intent.putExtra("SET_CHAR_NAME", true); //this is so we can pop the dialog in the char activity
                    intent.putExtra(CREATION_DATA, creationData);
                    creationData.put("DEITY",insertData);
                    startActivity(intent);
                }
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
            arguments.putString(AlignmentReligionDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(AlignmentReligionDetailFragment.ARG_ITEM_ID));
            arguments.putString(AlignmentReligionDetailFragment.ARG_ITEM,
                    extra.getString(AlignmentReligionDetailFragment.ARG_ITEM));
            if(showReligion){
                arguments.putBoolean(ARG_FRAG_BOOL, true);
            }else{
                arguments.putBoolean(ARG_FRAG_BOOL, false);
            }
            AlignmentReligionDetailFragment fragment = new AlignmentReligionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.alignmentreligion_detail_container, fragment)
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
            //pass our creation data and alignment/religion boolean to the parent activity
            Intent intent = new Intent(this, AlignmentReligionListActivity.class);
            intent.putExtra(CREATION_DATA, creationData);
            if(showReligion){
                intent.putExtra(ARG_EXTRA_NAME, true);
            }
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
