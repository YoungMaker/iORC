package edu.ycp.cs482.iorc;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * An activity representing a single Character detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CharacterListActivity}.
 */
public class CharacterDetailActivity extends AppCompatActivity {
    private TextView mTextMessage;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_sheet:
                    Bundle arguments = new Bundle();
                    arguments.putString(CharacterDetailFragment.ARG_ITEM_ID,
                            getIntent().getStringExtra(CharacterDetailFragment.ARG_ITEM_ID));
                    CharacterDetailFragment fragment = new CharacterDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.character_detail_container, fragment)
                            .commit();
                    return true;


                case R.id.action_skills:
                    SkillsFragment fragment2 = new SkillsFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.character_detail_container, fragment2, "FragmentName");
                    fragmentTransaction2.commit();
                    return true;

                case R.id.action_equipment:
                    EquipmentFragment fragment3 = new EquipmentFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.character_detail_container, fragment3, "FragmentName");
                    fragmentTransaction3.commit();
                    return true;

                case R.id.action_magic:
                    MagicFragment fragment4 = new MagicFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.character_detail_container, fragment4, "FragmentName");
                    fragmentTransaction4.commit();
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView Bottom_navigation_main = (BottomNavigationView) findViewById(R.id.character_bottom);
        Bottom_navigation_main.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
            arguments.putString(CharacterDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(CharacterDetailFragment.ARG_ITEM_ID));
            CharacterDetailFragment fragment = new CharacterDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.character_detail_container, fragment)
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
            navigateUpTo(new Intent(this, CharacterListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
