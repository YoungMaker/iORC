package edu.ycp.cs482.iorc.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import edu.ycp.cs482.iorc.CharacterUserQuery;
import edu.ycp.cs482.iorc.CharacterVersionQuery;
import edu.ycp.cs482.iorc.CreateCharacterMutation;
import edu.ycp.cs482.iorc.DeleteCharacterMutation;
import edu.ycp.cs482.iorc.Fragments.MasterFlows.CharacterDetailFragment;
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.SkillsFragment;
import edu.ycp.cs482.iorc.Apollo.MyApolloClient;
import edu.ycp.cs482.iorc.Apollo.RandAbilityGenerator;
import edu.ycp.cs482.iorc.LoginMutation;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.SkillVersionQuery;
import edu.ycp.cs482.iorc.UserDataQuery;
import edu.ycp.cs482.iorc.VersionSheetQuery;
import edu.ycp.cs482.iorc.fragment.CharacterData;
import edu.ycp.cs482.iorc.type.AbilityInput;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String LOGOUT_BOOL = "LOGOUT_BOOL";
    private static final String DO_DELETE = "DO_DELETE";
    private static final String DEL_ID = "DEL_ID";
    private static final String POP_ERROR = "ERR_POP";
    private static final String USER_EMAIL = "USER_EMAIL";
    private String PREFS_FILE = "iorctkfile";
    private boolean mTwoPane;
    private String mText;
    private SimpleItemRecyclerViewAdapter mSimpleAdapter;
    private List <CharacterData> characterResponses = new ArrayList<>();
    private List <CharacterUserQuery.GetUsersCharacter> characterDataResponse;

    //FIXME: Store only QueryData in intents/savedinstancestate. Deserialize
    private static final String CREATION_DATA = "CREATION_DATA";
    private String abilGenExpression = "3d6";
    private View mLoadingView;
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_character_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mLoadingView = findViewById(R.id.loadingPanel);
        mEmptyView = findViewById(R.id.empty_view);
        //get extras
        Bundle extras = getIntent().getExtras();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a hashmap where each field that is required to create a character will be stored
                //as the user progresses through the flow the selected race, class, etc. will be stored in the map with the type of data as the key
                //when the character creation is finalized the data from the hash map will be taken out and inserted into the character object
                HashMap<String, String> characterCreationData = new HashMap<>();
                characterCreationData.put("version", "4e");
                //pass current character to next stage in flow, continue until hitting the end
                Intent intent = new Intent(CharacterListActivity.this,ClassRaceListActivity.class);
                intent.putExtra(CREATION_DATA, characterCreationData);
                startActivity(intent);
            }
        });


        //get users characters
        getChars();


        //check if character is being deleted
        if(extras != null) {
            if (extras.containsKey(DO_DELETE) && extras.getBoolean(DO_DELETE)) {
                //trigger delete muation
                String toDel = extras.getString(DEL_ID);
                deleteCharacter(toDel);
                getIntent().removeExtra(DO_DELETE);
                getIntent().removeExtra(DEL_ID);
                //Log.d("DELETION ACTION", "DELETE CHARACTER WITH ID: " + toDel);
            } else {
                //get character list if now character is being deleted
                HttpCachePolicy.Policy policy = HttpCachePolicy.CACHE_FIRST;
                //getIds(policy);
            }
        }
        //TODO: put uname into savedinstancestate so that it does not have to pull this every time you reload.
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        if(prefs.contains(USER_EMAIL)){
            fetchUserData(prefs.getString(USER_EMAIL, ""));
        }


        //Log.d("AFTER ID", "THIS LINE IS AFTER THE GET IDS FUNCTION");
        if (findViewById(R.id.character_detail_container) != null) {
        
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }



        mSimpleAdapter = new SimpleItemRecyclerViewAdapter(this, characterResponses, mTwoPane);
        View recyclerView = findViewById(R.id.character_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


        if(extras != null) {
            if(extras.getBoolean("SET_CHAR_NAME")) {
                popInputDialog("Character Name:");
                getIntent().removeExtra("SET_CHAR_NAME");
            }
        }
    }


    private void fetchUserData(final String email){
        if(!email.isEmpty()){
            try {
                QueryControllerProvider.getInstance().getQueryController().userInfoQuery(email, getApplicationContext())
                        .enqueue(new ApolloCall.Callback<UserDataQuery.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<UserDataQuery.Data> response) {
                                try {
                                    String gsonData = QueryControllerProvider.getInstance().getQueryController().parseUserInfoQuery(email, response).getGsonData();
                                    parseUserData(gsonData);
                                } catch (AuthQueryException e) {
                                    Log.e("SESSION_EXPIRED", "active Session has expired!");
                                    returnToLogin();
                                }catch (QueryException e){
                                    Log.e("QUERY_FAILED", "User Query Failed");
                                }
                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {
                                Log.e("QUERY_FAILED", "Communication error");
                            }
                        });
            } catch (AuthQueryException e) {
                Log.e("NO_TOKEN", "No active user session");
                returnToLogin();
            }
        }
    }

    private void parseUserData(String gsonData){
        UserDataQuery.GetUserInfo data =  new Gson().fromJson(gsonData, UserDataQuery.GetUserInfo.class);
        setUserTitleBar(data.fragments().userData().uname());
    }

    private void setUserTitleBar(final String uname){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(uname + "'s characters");
                }else {
                    Log.e("TITLE_BAR", "Cannot get title bar");
                }
            }
        });
    }

    private void getChars(){
        try {
            QueryControllerProvider.getInstance().getQueryController().userCharactersQuery( getApplicationContext())
                    .enqueue(new ApolloCall.Callback<CharacterUserQuery.Data>() {
                        private QueryData queryData;
                        @Override
                        public void onResponse(@Nonnull Response<CharacterUserQuery.Data> response) {
                            try{
                                queryData = QueryControllerProvider.getInstance().getQueryController().parseUserCharactersQuery(getApplicationContext(), response);
                                processQueryData(queryData);
                            }catch(AuthQueryException e) {
                                Log.e("SESSION_EXPIRED", "user session has expired");
                                returnToLogin();
                            }catch (QueryException e){
                                popQueryError();
                            }
                            Log.d("WORKED", "got chars");
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            popCommError();
                            Log.e("failed to get response", e.getMessage());
                        }
                    });
        } catch (AuthQueryException e) {
            Log.e("NO_TOKEN", "no active user session");
            returnToLogin();
        }
    }

 //   deprecated
//    private void getIds(HttpCachePolicy.Policy policy){
//        final View loadingView = findViewById(R.id.loadingPanel);
//        MyApolloClient.getCharacterApolloClient().query(
//                //Groot:   58ff414b-f945-44bd-b20f-4a2ad3440254
//                //Boii:    b9704025-b811-426b-af3a-461dd40866e3
//                CharacterVersionQuery.builder().version("4e").build())
//                .httpCachePolicy(policy)
//                .enqueue(new ApolloCall.Callback<CharacterVersionQuery.Data>() {
//                    @Override
//                    public void onResponse(@Nonnull Response<CharacterVersionQuery.Data> response) {
//
//                        characterResponseData = response.data().getCharactersByVersion();
//                        //Log.d("BEFORE UI THREAD","Line before new runnable");
//                        CharacterListActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //characterResponses.add(characterResponseData);
//                                // Log.d("TAG","ON RESPONSE: " + response.data().getCharacterById());
//                                //Log.d("OUR TYPENAME: ","REPSONSE TYPENAME := " + characterResponseData.characterData().name());
//                                //clear list of characters so that when the query is called for a list update duplicate characters do not appear
//                                characterResponses.clear();
//                                //add each character into map and list
//                                for(int i = 0; i < characterResponseData.size(); i++){
//                                    characterResponses.add(characterResponseData.get(i));
//                                    characterDetailMap.put(
//                                            characterResponseData.get(i).fragments().characterData().id(),
//                                            (new Gson()).toJson(characterResponseData.get(i)));
//                                }
//                                refreshView();
//                                loadingView.setVisibility(View.GONE);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(@Nonnull ApolloException e) {
//                        Snackbar.make(findViewById(R.id.frameLayout), "Error communicating with server" , Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                        Log.e("ERROR: ", e.toString());
//                    }
//                });
//
//
//    }

    private void returnToLogin(){

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra(POP_ERROR, true);
        startActivity(intent);
        finish();
    }

    private void popCommError(){
        CharacterListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(CharacterListActivity.this).create();
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
        CharacterListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(CharacterListActivity.this).create();
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

    private void processQueryData(QueryData queryData){
        final String data = queryData.getGsonData();

        //Log.d("GSON_CHAR_DATA", data);
        CharacterListActivity.this.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                if(characterResponses != null) {
                    characterResponses.clear();
                }
                if(characterDataResponse!= null) {
                    characterDataResponse.clear();
                }

                Type listType = new TypeToken<List<CharacterUserQuery.GetUsersCharacter>>(){}.getType();
                characterDataResponse = new Gson().fromJson(data, listType);

                for (int i = 0; i < characterDataResponse.size(); i++) {
                    CharacterUserQuery.GetUsersCharacter data = characterDataResponse.get(i);
                    characterResponses.add(data.fragments().characterData());
                }

                //Log.d("RESPONSE_DATA", characterDataResponse.toString());
                refreshView();
                mLoadingView.setVisibility(View.GONE);
            }
        });

    }

    private void createCharacter(HashMap<String, String> creationData){
        final String charName = creationData.get("Name");
        final String charRaceID = creationData.get("RACE ID");
        final String charClassID = creationData.get("CLASS ID");
        final String charVersion = creationData.get("version");

        RandAbilityGenerator randAbils = new RandAbilityGenerator();
        AbilityInput.Builder abilityScores = AbilityInput.builder();
        abilityScores.str(randAbils.generateRoll(abilGenExpression));
        abilityScores.con(randAbils.generateRoll(abilGenExpression));
        abilityScores.dex(randAbils.generateRoll(abilGenExpression));
        abilityScores._int(randAbils.generateRoll(abilGenExpression));
        abilityScores.wis(randAbils.generateRoll(abilGenExpression));
        abilityScores.cha(randAbils.generateRoll(abilGenExpression));
        AbilityInput staticAbil = abilityScores.build();
        try{
            QueryControllerProvider.getInstance().getQueryController().createCharacterMutation(
                    charName, charRaceID, charClassID, charVersion, getApplicationContext(), staticAbil)
                    .enqueue(new ApolloCall.Callback<CreateCharacterMutation.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<CreateCharacterMutation.Data> response) {
                            try{
                                QueryControllerProvider.getInstance().getQueryController().parseCreateCharacterMutation(charVersion, response);
                                //get the updated list of characters
                                getChars();

                            }catch(AuthQueryException e){
                                Log.e("SESSION_EXPIRED", "user session has expired");
                                returnToLogin();
                            }catch(QueryException e){
                                popQueryError();
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            popCommError();
                            Log.e("failed to get response", e.getMessage());
                        }
                    });
        }catch (AuthQueryException e){
            Log.e("NO_TOKEN", "no active user session");
            returnToLogin();
        }


//        MyApolloClient.getMyApolloClient().mutate(
//
//            CreateCharacterMutation.builder().name(creationData.get("Name")).version(creationData.get("version")).abil(staticAbil).raceid(creationData.get("RACE ID")).classid(creationData.get("CLASS ID")).build())
//                .enqueue(new ApolloCall.Callback<CreateCharacterMutation.Data>() {
//            @Override
//            public void onResponse(@Nonnull Response<CreateCharacterMutation.Data> response) {
//                Log.d("CHARACTER CREATED", "CHARACTER HAS BEEN CREATED");
//                //loadingView.setVisibility(View.GONE);
//                HttpCachePolicy.Policy policy = HttpCachePolicy.NETWORK_FIRST;
//                //getIds(policy);
//                //notify user the network response has been received.
//                Snackbar.make(findViewById(R.id.frameLayout), "Character \"" + mText + "\" created" , Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                //loadingView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(@Nonnull ApolloException e) {
//                Snackbar.make(findViewById(R.id.frameLayout), "Error communicating with server" , Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void deleteCharacter(final String toDel){
        try {
            QueryControllerProvider.getInstance().getQueryController().deleteCharacterMutation(toDel, getApplicationContext())
                    .enqueue(new ApolloCall.Callback<DeleteCharacterMutation.Data>() {
                //on character deletion get the character list
                @Override
                public void onResponse(@Nonnull Response<DeleteCharacterMutation.Data> response) {
                    //HttpCachePolicy.Policy policy = HttpCachePolicy.NETWORK_FIRST;
                    try {
                        QueryControllerProvider.getInstance().getQueryController().parseDeleteCharacterMutation(toDel, response);
                        getChars(); // redo the get chars query and re-parse the data out
                    } catch (AuthQueryException e) {
                        Log.e("SESSION_EXPIRED", "user session has expired");
                        returnToLogin();
                    } catch (QueryException e){
                        popQueryError();
                        Log.e("QUERY_ERROR", "Query failed");
                    }

                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    popCommError();
                    Log.e("failed to get response", e.getMessage());

                }
            });
        } catch (AuthQueryException e) {
            Log.e("NO_TOKEN", "no active user session");
            returnToLogin();
        }

    }


    //TODO implement this with the edit character interface
    //deprecated
    /*private void editCharacter(){
        MyApolloClient.getMyApolloClient().mutate(
            EditCharacterMutation.builder().id().name().abil().classid().raceid().build()).enqueue(new ApolloCall.Callback<EditCharacterMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<EditCharacterMutation.Data> response) {
                Log.d("CHARACTER EDITED", "CHARACTER HAS BEEN EDITED");
                getIds();
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }*/

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

                HashMap<String, String> creationData = (HashMap<String, String>) getIntent().getSerializableExtra(CREATION_DATA);
                creationData.put("Name", mText);
                Log.d("CHARACTER CREATION DATA","DATA: " + creationData);
                createCharacter(creationData);
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
                break;

            case R.id.login:
                Intent loginIntent = new Intent(CharacterListActivity.this, LoginActivity.class);
                loginIntent.putExtra(LOGOUT_BOOL, true);
                startActivity(loginIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CharacterListActivity mParentActivity;
        private final List<CharacterData> mValues;;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharacterData item = (CharacterData) view.getTag();

                //TODO bundle the queried character data and pass it on to the detail activity
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(CharacterDetailFragment.ARG_ITEM_ID, serializeCharData(item));

                    CharacterDetailFragment fragment = new CharacterDetailFragment();
                    fragment.setArguments(arguments);

                    SkillsFragment skillFragment = new SkillsFragment();

                    skillFragment.setArguments(arguments);

                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.character_detail_container, fragment)
                            .commit();

                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Skills_List, skillFragment)
                            .commit();
                } else {
                    //SkillVersionQuery.GetVersionSkills skillItem = (SkillVersionQuery.GetVersionSkills) view.getTag();
                    Context context = view.getContext();
                    Intent intent = new Intent(context, CharacterDetailActivity.class);
                    intent.putExtra(CharacterDetailFragment.ARG_ITEM_ID, serializeCharData(item));
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(CharacterListActivity parent,
                                      List<CharacterData> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        //serialize data for the selected character
        private String serializeCharData(CharacterData characterData){
            String serializedData = new Gson().toJson(characterData);
            return serializedData;
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
            holder.mContentView.setText(mValues.get(position).name());

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

    public void refreshView(){
        mSimpleAdapter.notifyDataSetChanged();
    }
}
