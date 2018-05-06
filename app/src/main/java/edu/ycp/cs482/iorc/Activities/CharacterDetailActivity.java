package edu.ycp.cs482.iorc.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.view.ViewGroup;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import edu.ycp.cs482.iorc.Apollo.MyApolloClient;
import edu.ycp.cs482.iorc.Apollo.Query.Exception.AuthQueryException;
import edu.ycp.cs482.iorc.Apollo.Query.Exception.QueryException;
import edu.ycp.cs482.iorc.Apollo.Query.QueryControllerProvider;
import edu.ycp.cs482.iorc.Apollo.Query.QueryData;
import edu.ycp.cs482.iorc.CharacterVersionQuery;

import edu.ycp.cs482.iorc.CreateCharacterMutation;
import edu.ycp.cs482.iorc.EquipItemMutation;
import edu.ycp.cs482.iorc.Fragments.MasterFlows.CharacterDetailFragment;
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.EquipmentFragment;
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.MagicFragment;
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.SkillsFragment;
import edu.ycp.cs482.iorc.Fragments.MasterFlows.ItemDetailFragment;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.VersionSheetQuery;
import edu.ycp.cs482.iorc.fragment.CharacterData;
import edu.ycp.cs482.iorc.fragment.ClassData;
import edu.ycp.cs482.iorc.fragment.ItemData;
import edu.ycp.cs482.iorc.fragment.RaceData;
import edu.ycp.cs482.iorc.fragment.VersionSheetData;


/**
 * An activity representing a single Character detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CharacterListActivity}.
 */
public class CharacterDetailActivity extends AppCompatActivity implements EquipmentFragment.OnListFragmentInteractionListener {


    private static final String DO_DELETE = "DO_DELETE";
    private static final String DEL_ID = "DEL_ID";
    //private CharacterVersionQuery.GetCharactersByVersion mItem;
    private CharacterData mCharacterData;
    private String CHARCTER_ID = "";
    private VersionSheetData versionData;
    private HashMap<String, Double> charStatMap;
    private List<VersionSheetData.Stat> skillList = new ArrayList<>();
    private HashMap<String, Double> skillValueMap = new HashMap<>();
    private static final String CREATION_DATA = "CREATION_DATA";

    //FIXME: Store only QueryData object in saved instance state/bundles
    public static final String CHAR_ID = "CHAR_ID";

    private HashMap<String, String> creationData;
    private String char_id;
    private HashMap<String, String> defenseTableData = new HashMap<>();


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



        Intent extra = getIntent();

        //get characterdata from list
        if(extra.getSerializableExtra(CharacterDetailFragment.ARG_ITEM_ID) != null){
            //get character data to be used in fragments
            String serializedCharData = (String) extra
                    .getSerializableExtra(CharacterDetailFragment.ARG_ITEM_ID);
            mCharacterData = deserializeCharData(serializedCharData);
            getVersionSheet();
        }

        //Display Character name on Toolbar
        Bundle char_Arguments = new Bundle();
        //char_Arguments.putSerializable(V_DATA, getIntent().getSerializableExtra(V_DATA));
        char_Arguments.putString(CharacterDetailFragment.ARG_ITEM_ID,
                getIntent().getStringExtra(CharacterDetailFragment.ARG_ITEM_ID));
        char_Arguments.putSerializable(CharacterDetailFragment.ARG_MAP_ID,
                getIntent().getSerializableExtra(CharacterDetailFragment.ARG_MAP_ID));
        CharacterDetailFragment char_Fragment = new CharacterDetailFragment();
        char_Fragment.setArguments(char_Arguments);

        //HashMap<String, String> charMap =(HashMap<String, String>)char_Arguments.getSerializable(CharacterDetailFragment.ARG_MAP_ID);
        //give characterdata serializable here
        //et item as characterdata object (just use mCharacterData instead)
        //mItem = (new Gson()).fromJson(charObj, CharacterVersionQuery.GetCharactersByVersion.class);

        CollapsingToolbarLayout appBarLayout = findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && mCharacterData != null) {
            appBarLayout.setTitle(mCharacterData.name());

        }

        if(mCharacterData != null){
            char_id = mCharacterData.id();
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
            CharacterDetailFragment detailFragment = new CharacterDetailFragment();
            //detailFragment.setArguments(arguments);
            //if(versionData.fragments().versionSheetData() != null){
            if(versionData != null){
                detailFragment.loadVersionData(versionData);
            }

            detailFragment.loadCharacterData(mCharacterData, charStatMap, defenseTableData);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.character_detail_container, detailFragment)
                    .commit();
        }
    }

    //deserialize character data
    public CharacterData deserializeCharData(String serialData){
        return new Gson().fromJson(serialData, CharacterData.class);
    }

    //Create the menu button on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_character_detail_activity, menu);
        return true;
    }

    private void getVersionSheet(){
        try{
            QueryControllerProvider.getInstance().getQueryController().versionQuery(mCharacterData.version(), getApplicationContext())
                    .enqueue(new ApolloCall.Callback<VersionSheetQuery.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<VersionSheetQuery.Data> response) {
                            try {
                                QueryData versionQueryData = QueryControllerProvider.getInstance()
                                        .getQueryController()
                                        .parseVersionQuery(mCharacterData.version(), getApplicationContext(), response);
                                processVersionQueryData(versionQueryData);
                            }catch(AuthQueryException e){
                                Log.d("FAILED", "AUTH Query exception");
                            }catch(QueryException e){
                                Log.d("FAILED", "ERROR");
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {

                        }
                    });
        }catch(AuthQueryException e){
            Log.d("FAILED", "Invalid token");
        }
    }

    private void processVersionQueryData(QueryData versionQueryData){
        final String data = versionQueryData.getGsonData();
        CharacterDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                VersionSheetQuery.GetVersionSheet versionQuery = new Gson().fromJson(data, VersionSheetQuery.GetVersionSheet.class);
                versionData = versionQuery.fragments().versionSheetData();
                if(versionData != null){
                    generateCharacterStats();
                }
                refreshView();
            }
        });
    }

    private void refreshView(){

        CharacterDetailFragment detailFragment = new CharacterDetailFragment();
        //detailFragment.setArguments(arguments);
        //if(versionData.fragments().versionSheetData() != null){
        if(versionData != null){
            detailFragment.loadVersionData(versionData);
        }

        detailFragment.loadCharacterData(mCharacterData, charStatMap, defenseTableData);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.character_detail_container, detailFragment)
                .commit();
        ViewGroup view = findViewById(R.id.character_detail_container);
        view.refreshDrawableState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent Intent = new Intent(CharacterDetailActivity.this, CharacterListActivity.class);
                startActivity(Intent);
                break;

            case R.id.itemIntent:
                Intent itemIntent = new Intent(CharacterDetailActivity.this, ItemListActivity.class);
                itemIntent.putExtra(CHAR_ID, mCharacterData.id());
                itemIntent.putExtra(CREATION_DATA, creationData);
                startActivity(itemIntent);
                break;

            case R.id.deleteCheck:
                confirmDeleteBox();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_sheet:
                    Bundle arguments = new Bundle();
                    //getIntent().getSerializableExtra(V_DATA);
                    //Log.d("V_DATA", arguments.toString());
                    CharacterDetailFragment detailFragment = new CharacterDetailFragment();
                    detailFragment.setArguments(arguments);
                    if(versionData != null) {
                        detailFragment.loadVersionData(versionData);
                    }
                    detailFragment.loadCharacterData(mCharacterData, charStatMap, defenseTableData);
                    //}

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.character_detail_container, detailFragment)
                            .commit();
                    break;

                case R.id.action_skills:
                    //TODO: Please use newInstance method if at all possible
                    Bundle skillArguments = new Bundle();
                    SkillsFragment skillFragment = new SkillsFragment();
                    skillFragment.assignSkillList(skillList);
                    skillFragment.assignSkillMap(skillValueMap);
                    skillFragment.setArguments(skillArguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.character_detail_container, skillFragment)
                            .commit();
                    break;

                case R.id.action_equipment:
                    //get items from character inventory and put into map, key is just i for now
                    //value is just name for now
                    HashMap<String, String> invMap = new HashMap<>();
                    for (int i = 0; i < mCharacterData.inventory().size(); i++) {
                        CharacterData.Inventory invItem = mCharacterData.inventory().get(i);
                        ItemData itemDataobj = invItem.fragments().itemData();
                        String itemData = new Gson().toJson(itemDataobj);
                        invMap.put(String.valueOf(i), itemData);
                        Log.d("INVENTORY_ITEM", itemData);
                    }
                    //inject character inventory into fragment
                    EquipmentFragment fragment3 = EquipmentFragment.Companion.newInstance(invMap);
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

    private void confirmDeleteBox() {
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


    //Equipment item clicked
    @Override
    public void onListFragmentInteraction(@NotNull ItemData item) {
        Log.d("ITEM_CLICKED", item.name());

        String gsonItem = (new Gson()).toJson(item);
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_ITEM, gsonItem);
        startActivity(intent);
    }
    //Equipment item longpressed
    @Override
    public boolean onListFragmentLongpress(@NotNull final ItemData item) {
        //TODO: Equip item
        //Log.d("ITEM_LONGPRESSED", item.name());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Equip  " + item.name() + "?");
        // Set up the buttons
        builder.setPositiveButton("Equip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: handle equip items
                equipItem(item);
                Log.d("ITEM_EQUIP", item.name());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        return true;
    }

    private void equipItem(@NonNull final ItemData item) {
        MyApolloClient.getMyApolloClient().mutate(

                EquipItemMutation.builder().charId(char_id).itemId(item.id()).slotid("head").build())
                .enqueue(new ApolloCall.Callback<EquipItemMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<EquipItemMutation.Data> response) {
                        //loadingView.setVisibility(View.GONE);
                        HttpCachePolicy.Policy policy = HttpCachePolicy.NETWORK_FIRST;
                        //getIds(policy);
                        //notify user the network response has been received.

                        //loadingView.setVisibility(View.GONE);
                       if(response.data() == null) {
                           Log.d("ITEM_EQUIPPED", "Item " + item.id() + " has been equipped");
                           Snackbar.make(findViewById(R.id.character_detail_container), "Item \"" + item.name() + "\" equipped" , Snackbar.LENGTH_LONG)
                                   .setAction("Action", null).show();
                       }
                       else{
                           Snackbar.make(findViewById(R.id.character_detail_container), "Item \"" + item.name() + "\" failed to equip" , Snackbar.LENGTH_LONG)
                                   .setAction("Action", null).show();
                       }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Snackbar.make(findViewById(R.id.character_detail_container), "Error communicating with server" , Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
    }

    public void generateCharacterStats(){
        //TODO unspaghet
        charStatMap = new HashMap<>();
        List<VersionSheetData.Stat> stats = versionData.stats();
        List<String> abilityList = new ArrayList<>();
        List<String> modifierList = new ArrayList<>();
        CharacterData charData = mCharacterData;
        HashMap<String, VersionSheetData.Stat> statObjMap = new HashMap<>();
        List<String> defList = new ArrayList<>();

        //Log.d("VERSION_DATA", versionData.stats().toString());

        CharacterData.Classql charClass = charData.classql();
        CharacterData.Race charRace = charData.race();
        CharacterData.AbilityPoints abilityScores = charData.abilityPoints();

        //loop through stats list and add to hashmap with the key being the name of the stat
        for(int i = 0; i < stats.size(); i++){

            //get version sheet stats and add each one to a map
            VersionSheetData.Stat stat = stats.get(i);
            String key = keyFilter(stat);
            statObjMap.put(key, stat);
            charStatMap.put(key, 0d);

        }

        createDefList(defList, statObjMap);

        Log.d("CHARACTER_ABILITIES", charStatMap.toString());

        //add race values to map
        for(int i = 0; i < charRace.fragments().raceData().modifiers().size(); i++){

            //get racedata modifiers
            RaceData.Modifier mod = charRace.fragments().raceData().modifiers().get(i);

            //check if the modifier key is in the map if so add the value to the existing
            //value in the map
            if(charStatMap.containsKey(mod.key())){
                Double mapItem = charStatMap.get(mod.key());
                mapItem += mod.value();
                charStatMap.put(mod.key(), mapItem);
            }
        }

        //add class values to map
        for(int i = 0; i < charClass.fragments().classData().modifiers().size(); i++){

            //get racedata modifiers
            ClassData.Modifier mod = charClass.fragments().classData().modifiers().get(i);

            //check if the modifier key is in the map if so add the value to the existing
            //value in the map
            if(charStatMap.containsKey(mod.key())){
                if(defList.contains(mod.key())){
                    defenseTableData.put(mod.key() + "_class", String.valueOf(mod.value()));
                }
                Double mapItem = charStatMap.get(mod.key());
                mapItem += mod.value();
                charStatMap.put(mod.key(), mapItem);
            }
        }

        //add ability points from character data
        for(Method m: abilityScores.getClass().getMethods()){
            String abilKey = m.getName().replaceAll("_", "");
            if(charStatMap.containsKey(abilKey)){
                //Log.d("ADD_FROM_METHOD", abilKey);
                try {
                    long abilityVal = (long) m.invoke(abilityScores);
                    double val = charStatMap.get(abilKey);
                    val += abilityVal;
                    abilityList.add(abilKey);
                    charStatMap.put(abilKey, val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        //create ability modifiers
        for(String key : statObjMap.keySet()){
            //Log.d("KEY", key);
            VersionSheetData.Stat statObj = statObjMap.get(key);
            List<VersionSheetData.Modifier> statMods = statObj.modifiers();

            //modifier values
            Double abilModVal = null;
            Double mulVal = null;

            if(statMods != null){
                //get the ability modifier for each ability
                for(VersionSheetData.Modifier mod : statMods){
                    if(abilityList.contains(mod.key())){
                        Log.d("KEY", mod.key());
                        abilModVal = charStatMap.get(mod.key());
                        abilModVal += mod.value();
                        modifierList.add(key);
                    }
                    if(mod.key().equals("*")){
                        mulVal = mod.value();
                    }
                }

                //calculate ability modifier and add to the modifier value in the map
                if(abilModVal != null && mulVal != null){
                    abilModVal *= mulVal;
                    charStatMap.put(key, Math.floor(abilModVal));
                }

            }

        }

        for(String key : statObjMap.keySet()){
            VersionSheetData.Stat statObj = statObjMap.get(key);
            List<VersionSheetData.Modifier> statMods = statObj.modifiers();

            Double skillValue;

            if(statMods != null){
                String statName = statObj.name();
                for(VersionSheetData.Modifier mod : statMods){

                    //create skill values
                    if(modifierList.contains(mod.key())){
                        //Log.d("MOD_KEY", mod.key() + statName);
                        skillValue = charStatMap.get(statName.toLowerCase());
                        skillValue += charStatMap.get(mod.key());
                        //Log.d("SKILL_NAME", statName.toLowerCase());
                        charStatMap.put(statName.toLowerCase(), skillValue);
                    }

                    //create defense values
                    if(mod.key().contains("||")){
                        String[] options = mod.key().split("\\|\\|");

                        //Log.d("OPTIONS", options[0] + " " + options[1]);
                        //Log.d("DEFENSE", statName.toLowerCase());

                        String highestMod = selectDefMod(options);
                        //Log.d("DEF_MOD", highestMod);
                        Double defModVal = charStatMap.get(highestMod);
                        Double defModCurVal = charStatMap.get(statName.toLowerCase());
                        defenseTableData.put(statName.toLowerCase()+"_abil", defModVal.toString());
                        Double value =  defModCurVal + defModVal + 10;
                        //Log.d("DEF_VAL", value.toString());
                        charStatMap.put(statName.toLowerCase(), value);

                    }
                }
                //add skills to skill map
                if(statObj.skill()){
                    skillValueMap.put(key, charStatMap.get(key));
                    skillList.add(statObj);
                }
            }


        }
        //Log.d("CHARACTER_ABILITIES", charStatMap.toString());
        //log map values

        printMapValues(charStatMap);
        Log.d("PRINT_SKILLS", "HERE");
        printMapValues(skillValueMap);

    }

    public String keyFilter(VersionSheetData.Stat stat){
        String statName = stat.name();
        String statDescription = stat.description();
        String keyName;

        //abilities have themselves as the description
        //if a stat is an ability get the first 3 characters of the string
        //then make the substring the string name
        if(statName.equals(statDescription)){
            statName = statName.substring(0, 3);
        }

        //make sure the key is all lowercase
        keyName = statName.toLowerCase();

        return keyName;
    }

    public String selectDefMod(String[] options){

        String highest = "";
        Double highestVal = null;

        for(String option : options){
            Double value = charStatMap.get(option);
            if(highestVal == null || highestVal < value){
                highestVal = value;
                highest = option;
            }
        }

        return highest;
    }

    //print a map with the key and corresponding value to make it easier to read (for debugging/dev)
    public void printMapValues(HashMap<String, Double> map){
        for(String key : map.keySet()){
            Log.d("MAP_VALUE", key + " " + map.get(key).toString());
        }
    }

    public void createDefList(List<String> defList, HashMap<String, VersionSheetData.Stat> statMap){
        for(String key : statMap.keySet()){
            VersionSheetData.Stat statObj = statMap.get(key);
            List<VersionSheetData.Modifier> mods = statObj.modifiers();
            if(mods != null){
                for(VersionSheetData.Modifier mod : mods){
                    if(mod.key().contains("||")){
                        Log.d("DEFENSE_FOUND", statObj.name());
                        defList.add(statObj.name().toLowerCase());
                    }
                }
            }
        }
        Log.d("DEFENSES_FOUND", "Exiting");
    }


}
