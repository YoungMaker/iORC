package edu.ycp.cs482.iorc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;


/**
 * An activity representing a single Character detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CharacterListActivity}.
 */
public class CharacterDetailActivity extends AppCompatActivity {

    private static final String DO_DELETE = "DO_DELETE";
    private static final String DEL_ID = "DEL_ID";
    private String CHARCTER_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView Bottom_navigation_main = findViewById(R.id.character_bottom);
        Bottom_navigation_main.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        FloatingActionButton fab = findViewById(R.id.edit_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "TODO: Implement Editing", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
            CHARCTER_ID = getIntent().getStringExtra(CharacterDetailFragment.ARG_ITEM_ID);
            arguments.putString(CharacterDetailFragment.ARG_ITEM_ID,
                    CHARCTER_ID);
            arguments.putSerializable(CharacterDetailFragment.ARG_MAP_ID, getIntent().getSerializableExtra(CharacterDetailFragment.ARG_MAP_ID));
            CharacterDetailFragment fragment = new CharacterDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.character_detail_container, fragment)
                    .commit();
        }
    }

    //Create the menu button on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_character_detail_activity,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.home:
                Intent Intent = new Intent(CharacterDetailActivity.this, CharacterListActivity.class);
                startActivity(Intent);

            case R.id.deleteCheck:
                confirmDeleteBox();
        }
        //int id = item.getItemId();
        //if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
          //  navigateUpTo(new Intent(this, CharacterListActivity.class));
            //return true;
        //}
        //else if(id == R.id.deleteCheck){
          //  Intent deleteIntent = new Intent(CharacterDetailActivity.this, DeleteCheckActivity.class);
            //startActivity(deleteIntent);
        //}
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_sheet:
                    Bundle arguments = new Bundle();
                    arguments.putString(CharacterDetailFragment.ARG_ITEM_ID,
                            getIntent().getStringExtra(CharacterDetailFragment.ARG_ITEM_ID));
                    //Log.d("CHAR_ARG", arguments.toString());
                    arguments.putSerializable(CharacterDetailFragment.ARG_MAP_ID, getIntent().getSerializableExtra(CharacterDetailFragment.ARG_MAP_ID));
                    CharacterDetailFragment fragment = new CharacterDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.character_detail_container, fragment)
                            .commit();
                    break;

                case R.id.action_skills:
                    Bundle skillArguments = new Bundle();
                    skillArguments.putString(SkillsFragment.ARG_ITEM_ID,
                            getIntent().getStringExtra(SkillsFragment.ARG_ITEM_ID));
                    //Log.d("SKILL_ARG", skillArguments.toString());
                    skillArguments.putSerializable(SkillsFragment.ARG_MAP_ID, getIntent().getSerializableExtra(SkillsFragment.ARG_MAP_ID));
                    SkillsFragment skillFragment = new SkillsFragment();
                    skillFragment.setArguments(skillArguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.character_detail_container, skillFragment)
                            .commit();
                    break;

                case R.id.action_equipment:
                    EquipmentFragment fragment3 = new EquipmentFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.character_detail_container, fragment3, "FragmentName");
                    fragmentTransaction3.commit();
                    break;

                case R.id.action_magic:
                    MagicFragment fragment4 = new MagicFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.character_detail_container, fragment4, "FragmentName");
                    fragmentTransaction4.commit();
                    break;

            }
            return true;
        }
    };

    private void confirmDeleteBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete?");
        // Set up the buttons
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Log.d("DELETE CHARACTER","DELETEING CHARACTER");
                //TODO handle character deletion
                Intent intent = new Intent(CharacterDetailActivity.this, CharacterListActivity.class);
                intent.putExtra(DO_DELETE, true);
                intent.putExtra(DEL_ID, CHARCTER_ID);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
