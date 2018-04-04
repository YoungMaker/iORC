package edu.ycp.cs482.iorc.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import edu.ycp.cs482.iorc.CharacterVersionQuery;
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.dummy.DummyContent;
import edu.ycp.cs482.iorc.Fragments.MasterFlows.CharacterDetailFragment;
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.EquipmentFragment;
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.MagicFragment;
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.SkillsFragment;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.fragment.CharacterData;
import edu.ycp.cs482.iorc.fragment.ItemData;


/**
 * An activity representing a single Character detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CharacterListActivity}.
 */
public class CharacterDetailActivity extends AppCompatActivity implements EquipmentFragment.OnListFragmentInteractionListener{


    private static final String DO_DELETE = "DO_DELETE";
    private static final String DEL_ID = "DEL_ID";
    private static final String V_DATA = "VERSION_DATA";
    private String CHARCTER_ID = "";
    private static final String CREATION_DATA = "CREATION_DATA";
    private HashMap<String, String> creationData;



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
            public void onClick(View view) { //TODO toggle editing
                Log.d("IMPLEMENT CHAR EDITING", "Edit Character");
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
            CHARCTER_ID = getIntent().getStringExtra(CharacterDetailFragment.ARG_ITEM_ID);
            arguments.putString(CharacterDetailFragment.ARG_ITEM_ID,
                    CHARCTER_ID);
            arguments.putSerializable(V_DATA, getIntent().getSerializableExtra(V_DATA));
            arguments.putSerializable(CharacterDetailFragment.ARG_MAP_ID,
                    getIntent().getSerializableExtra(CharacterDetailFragment.ARG_MAP_ID));
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
                break;

            case R.id.itemIntent:
                Intent itemIntent = new Intent(CharacterDetailActivity.this, ItemListActivity.class);
                itemIntent.putExtra(CREATION_DATA, creationData);
                startActivity(itemIntent);
                break;

            case R.id.deleteCheck:
                confirmDeleteBox();
                break;
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
                    arguments.putSerializable(V_DATA, getIntent().getSerializableExtra(V_DATA));
                    Log.d("V_DATA", arguments.toString());
                    arguments.putString(CharacterDetailFragment.ARG_ITEM_ID,
                            getIntent().getStringExtra(CharacterDetailFragment.ARG_ITEM_ID));
                    arguments.putSerializable(CharacterDetailFragment.ARG_MAP_ID,
                            getIntent().getSerializableExtra(CharacterDetailFragment.ARG_MAP_ID));
                    CharacterDetailFragment fragment = new CharacterDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.character_detail_container, fragment)
                            .commit();
                    break;

                case R.id.action_skills:
                    //TODO: Please use newInstance method if at all possible
                    Bundle skillArguments = new Bundle();
                    skillArguments.putSerializable(V_DATA, getIntent().getSerializableExtra(V_DATA));
                    //skillArguments.putString(SkillsFragment.ARG_ITEM_ID,
                            //getIntent().getStringExtra(SkillsFragment.ARG_ITEM_ID));
                    //skillArguments.putSerializable(SkillsFragment.ARG_MAP_ID, getIntent().getSerializableExtra(SkillsFragment.ARG_MAP_ID));
                    SkillsFragment skillFragment = new SkillsFragment();
                    skillFragment.setArguments(skillArguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.character_detail_container, skillFragment)
                            .commit();
                    break;

                case R.id.action_equipment:
                    //get key for character map
                    String itemKey = getIntent().getStringExtra(CharacterDetailFragment.ARG_ITEM_ID);
                    //get character map
                    HashMap<String, String> charMap =
                            (HashMap<String, String>) getIntent()
                                    .getSerializableExtra(CharacterDetailFragment.ARG_MAP_ID);
                    //unwrap character data
                    CharacterData character = (new Gson()).fromJson(charMap.get(itemKey),
                            CharacterVersionQuery.GetCharactersByVersion.class).fragments().characterData();

                    //get items from character inventory and put into map, key is just i for now
                    //value is just name for now
                    HashMap<String, String> invMap = new HashMap<>();
                    for(int i = 0; i < character.inventory().size(); i++){
                        CharacterData.Inventory invItem = character.inventory().get(i);
                        String itemData = new Gson().toJson(invItem.fragments().itemData());
                        invMap.put(String.valueOf(i), itemData);
                        Log.d("INVENTORY_ITEM", itemData);
                    }
                    //TODO: inject character inventory into fragment
//                    EquipmentFragment fragment3 = EquipmentFragment.Companion.newInstance(invMap);
//                    android.support.v4.app.FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction3.replace(R.id.character_detail_container, fragment3, "FragmentName");
//                    fragmentTransaction3.commit();
//                    break;

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

    @Override
    public void onListFragmentInteraction(@NotNull ItemData item) {
        Log.d("ITEM_CLICKED", item.name());
    }
}
