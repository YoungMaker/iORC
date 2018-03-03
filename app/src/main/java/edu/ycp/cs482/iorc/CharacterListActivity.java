package edu.ycp.cs482.iorc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;

import edu.ycp.cs482.iorc.dummy.DummyContent;
import edu.ycp.cs482.iorc.dummy.MyApolloClient;
import edu.ycp.cs482.iorc.fragment.CharacterData;
//import fragment.CharacterData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * An activity representing a list of Characters. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CharacterDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CharacterListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private String mText;
    private SimpleItemRecyclerViewAdapter mSimpleAdapter;
    private List<CharacterVersionQuery.GetCharactersByVersion> characterResponseData;
    private List <CharacterVersionQuery.GetCharactersByVersion> characterResponses = new ArrayList<CharacterVersionQuery.GetCharactersByVersion>();
    private HashMap<String, String> characterDetailMap = new HashMap<String, String>();
    private static final String CREATION_DATA = "CREATION_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a hashmap where each field that is required to create a character will be stored
                //as the user progresses through the flow the selected race, class, etc. will be stored in the map with the type of data as the key
                //when the character creation is finalized the data from the hash map will be taken out and inserted into the character object
                HashMap<String, String> characterCreationData = new HashMap<String, String>();
                characterCreationData.put("version", "4e");
                //pass current character to next stage in flow, continue until hitting the end
                Intent intent = new Intent(CharacterListActivity.this,ClassRaceListActivity.class);
                intent.putExtra(CREATION_DATA, characterCreationData);
                startActivity(intent);
            }
        });

        getIds();
        Log.d("AFTER ID", "THIS LINE IS AFTER THE GET IDS FUNCTION");
        if (findViewById(R.id.character_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        mSimpleAdapter = new SimpleItemRecyclerViewAdapter(this, characterResponses, characterDetailMap, mTwoPane);
        View recyclerView = findViewById(R.id.character_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.getBoolean("SET_CHAR_NAME")) {
                popInputDialog("Character Name:");
                getIntent().removeExtra("SET_CHAR_NAME");
            }
        }
    }

    //test query
    private void getIds(){

        MyApolloClient.getMyApolloClient().query(
                //Groot:   58ff414b-f945-44bd-b20f-4a2ad3440254
                //Boii:    b9704025-b811-426b-af3a-461dd40866e3
                CharacterVersionQuery.builder().version("4e").build()).enqueue(new ApolloCall.Callback<CharacterVersionQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<CharacterVersionQuery.Data> response) {

                characterResponseData = response.data().getCharactersByVersion;
                //Log.d("BEFORE UI THREAD","Line before new runnable");
                CharacterListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //characterResponses.add(characterResponseData);
                        // Log.d("TAG","ON RESPONSE: " + response.data().getCharacterById());
                        //Log.d("OUR TYPENAME: ","REPSONSE TYPENAME := " + characterResponseData.characterData().name());
                        //add each character into map and list
                        for(int i = 0; i < characterResponseData.size(); i++){
                            characterResponses.add(characterResponseData.get(i));
                            characterDetailMap.put(characterResponseData.get(i).fragments().characterData.id(),(new Gson()).toJson(characterResponseData.get(i)));
                        }
                        refreshView();
                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR: ", e.toString());
            }
        });

    }

    private void createCharacter(){
        //MyApolloClient.getMyApolloClient().mutate()
    }

    private void popInputDialog(String title ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mText = input.getText().toString(); //TODO set char name?
                //TODO: Move this to when the network response has been received.
                Snackbar.make(findViewById(R.id.frameLayout), "Character \"" + mText + "\" created" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                HashMap<String, String> creationData = (HashMap<String, String>) getIntent().getSerializableExtra(CREATION_DATA);
                creationData.put("Name", mText);
                Log.d("CHARACTER CREATION DATA","DATA: " + creationData);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //TODO: what should happen when we cancel?
            }
        });

        builder.show();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mSimpleAdapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL); //this should probably get the layoutManager's preference.
        recyclerView.addItemDecoration(itemDecor);
    }

    //Create the menu button on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_character_list_activity,menu);
        return true;
    }

    //Select in menu button to move to another activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.dice:
                Intent diceIntent = new Intent(CharacterListActivity.this, DiceWidgetActivity.class);
                startActivity(diceIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CharacterListActivity mParentActivity;
        private final List<CharacterVersionQuery.GetCharactersByVersion> mValues;
        private final HashMap<String, String> mMap;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharacterVersionQuery.GetCharactersByVersion item = (CharacterVersionQuery.GetCharactersByVersion) view.getTag();

                //TODO bundle the queried character data and pass it on to the detail activity
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(CharacterDetailFragment.ARG_ITEM_ID, item.fragments().characterData.id());
                    CharacterDetailFragment fragment = new CharacterDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.character_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, CharacterDetailActivity.class);
                    intent.putExtra(CharacterDetailFragment.ARG_ITEM_ID, item.fragments().characterData.id());
                    intent.putExtra(CharacterDetailFragment.ARG_MAP_ID, mMap);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(CharacterListActivity parent,
                                      List<CharacterVersionQuery.GetCharactersByVersion> items,
                                      HashMap<String, String> characterDetailMap, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            mMap = characterDetailMap;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //holder.mIdView.setText(mValues.get(position).characterData.id());
            holder.mContentView.setText(mValues.get(position).fragments().characterData.name());

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
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    public void refreshView(){
        mSimpleAdapter.notifyDataSetChanged();
    }
}
