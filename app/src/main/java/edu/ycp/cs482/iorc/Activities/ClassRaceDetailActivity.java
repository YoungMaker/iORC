package edu.ycp.cs482.iorc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.HashMap;

import edu.ycp.cs482.iorc.Fragments.MasterFlows.ClassRaceDetailFragment;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.fragment.ClassData;
import edu.ycp.cs482.iorc.fragment.RaceData;

/**
 * An activity representing a single ClassRace detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ClassRaceListActivity}.
 */

public class ClassRaceDetailActivity extends AppCompatActivity {
    private boolean showRace;
    private HashMap<String, String> creationData;
    private String ARG_BOOL_KEY = "isRace";
    private String ARG_EXTRA_NAME = "RACE_SWITCH";
    private String ARG_FRAG_BOOL = "SHOW_RACE";
    private static final String CREATION_DATA = "CREATION_DATA";
    private RaceData raceData;
    private ClassData classData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrace_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        final Bundle extra = getIntent().getExtras();
        if(extra != null){
            if(extra.getBoolean(ARG_BOOL_KEY)){
                showRace = true;
                raceData = new Gson()
                        .fromJson(extra.getString(ClassRaceDetailFragment.ARG_RACE), RaceData.class);
            }else{
                showRace = false;
                classData = new Gson()
                        .fromJson(extra.getString(ClassRaceDetailFragment.ARG_CLASS), ClassData.class);
            }

        }else {
            showRace = false;
        }


        //retrieve the character creation data
        creationData = (HashMap<String, String>) extra.getSerializable(ClassRaceDetailFragment.CREATION_DATA);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override //TODO: save the selected Class or Race.
            public void onClick(View view) {
                //handle selection of class/race being made
                if(!showRace){
                    creationData.put("CLASS ID", classData.id());
                    Intent intent = new Intent(ClassRaceDetailActivity.this, ClassRaceListActivity.class);
                    intent.putExtra(ARG_EXTRA_NAME, true);
                    intent.putExtra(ClassRaceDetailFragment.CREATION_DATA, creationData);
                    startActivity(intent);

                } else if(showRace){
                    creationData.put("RACE ID", raceData.id());
                    Intent intent = new Intent(ClassRaceDetailActivity.this, AlignmentReligionListActivity.class);
                    intent.putExtra(ClassRaceDetailFragment.CREATION_DATA, creationData);
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
            //arguments.putSerializable(ClassRaceDetailFragment.CREATION_DATA, getIntent().getSerializableExtra(ClassRaceDetailFragment.CREATION_DATA));
            ClassRaceDetailFragment fragment = new ClassRaceDetailFragment();
            arguments.putString(ClassRaceDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ClassRaceDetailFragment.ARG_ITEM_ID));
            if(showRace){
                arguments.putBoolean(ARG_FRAG_BOOL, true);
                arguments.putString(ClassRaceDetailFragment.ARG_RACE_MAP_ID, getIntent().getStringExtra(ClassRaceDetailFragment.ARG_RACE_MAP_ID));
                arguments.putSerializable(ClassRaceDetailFragment.ARG_RACE_MAP, getIntent().getSerializableExtra(ClassRaceDetailFragment.ARG_RACE_MAP));
                fragment.loadRaces(raceData);
            }else {
                arguments.putBoolean(ARG_FRAG_BOOL, false);
                arguments.putString(ClassRaceDetailFragment.ARG_CLASS_MAP_ID, getIntent().getStringExtra(ClassRaceDetailFragment.ARG_CLASS_MAP_ID));
                arguments.putSerializable(ClassRaceDetailFragment.ARG_CLASS_MAP, getIntent().getSerializableExtra(ClassRaceDetailFragment.ARG_CLASS_MAP));
                fragment.loadClasses(classData);
            }

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.classrace_detail_container, fragment)
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
            //pass our creation data and class/race boolean to the parent activity
            Intent intent = new Intent(this, ClassRaceListActivity.class);
            intent.putExtra(CREATION_DATA, creationData);
            if(showRace){
                intent.putExtra(ARG_EXTRA_NAME, true);
            }
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
