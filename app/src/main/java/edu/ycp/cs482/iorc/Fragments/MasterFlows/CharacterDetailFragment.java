package edu.ycp.cs482.iorc.Fragments.MasterFlows;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ycp.cs482.iorc.Activities.CharacterDetailActivity;
import edu.ycp.cs482.iorc.Activities.CharacterListActivity;
import edu.ycp.cs482.iorc.CharacterVersionQuery;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.VersionSheetQuery;
import edu.ycp.cs482.iorc.fragment.CharacterData;
import edu.ycp.cs482.iorc.fragment.ClassData;
import edu.ycp.cs482.iorc.fragment.RaceData;
import edu.ycp.cs482.iorc.fragment.VersionSheetData;

/**
 * A fragment representing a single Character detail screen.
 * This fragment is either contained in a {@link CharacterListActivity}
 * in two-pane mode (on tablets) or a {@link CharacterDetailActivity}
 * on handsets.
 */
public class CharacterDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_MAP_ID = "map_id";
    private static final String V_DATA = "VERSION_DATA";
    /**
     * The dummy content this fragment is presenting.
     */
    private CharacterVersionQuery.GetCharactersByVersion mItem;
    private VersionSheetQuery.GetVersionSheet versionData;
    private HashMap<String, Double> charStatMap = new HashMap<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharacterDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO receive map from character list activity
        Log.d("mItem CHECK: ", "Loading Map");
        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey(ARG_MAP_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.CHARACTER_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Bundle bundle = getArguments();
            Log.d("CHAR_ARGUMENTS", getArguments().toString());
            HashMap<String, String> charMap =(HashMap<String, String>)bundle.getSerializable(ARG_MAP_ID);
            String charObj = "";
            Log.d("CHAR_OBJ", charMap.get(bundle.getString(ARG_ITEM_ID)));
            if(charMap != null){
                charObj = charMap.get(bundle.getString((ARG_ITEM_ID)));
            }
            mItem = (new Gson()).fromJson(charObj, CharacterVersionQuery.GetCharactersByVersion.class);
            Log.d("mItem CHECK: ", "" + mItem);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null && mItem != null) {
                appBarLayout.setTitle(mItem.fragments().characterData().name());
            }

            if(bundle.containsKey(V_DATA)){
                HashMap<String, String> vDataMap = (HashMap<String, String>)bundle.getSerializable(V_DATA);
                if(vDataMap != null){
                    versionData = (new Gson()).fromJson(vDataMap
                            .get(V_DATA), VersionSheetQuery.GetVersionSheet.class);
                }
            }

        }
        if(versionData != null){
            generateCharacterStats();
        }

        Log.d("mItem CHECK: ", "Finished Loading Map");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.character_detail, container, false);

        CharacterView mCharView = new CharacterView(rootView);
        if(mItem != null){
            mCharView.updateCharView(mItem);
        }
        return rootView;
    }


    private class CharacterView {

//        private TextView mCharacterDetailInitiative;
//        private TextView mCharacterDetailHp;
        //private TextView mCharacterDetailFort;
        //private TextView mCharacterDetailRef;
       // private TextView mCharacterDetailSpd;
        private TextView mCharacterAbilStr;
        private TextView mCharacterAbilCon;
        private TextView mCharacterAbilDex;
        private TextView mCharacterAbilInt;
        private TextView mCharacterAbilWis;
        private TextView mCharacterAbilCha;
        private TextView mCharacterRace;
        private TextView mCharacterClass;


        private CharacterView(View rootView){
//            mCharacterDetailInitiative = (TextView)rootView.findViewById(R.id.character_detail_initiative);
           // mCharacterDetailHp = (TextView)rootView.findViewById(R.id.character_abil_hp);
            //mCharacterDetailFort = rootView.findViewById(R.id.character_abil_fort);
            //mCharacterDetailRef = rootView.findViewById(R.id.character_abil_ref);
           // mCharacterDetailSpd = (TextView)rootView.findViewById(R.id.character_detail_spd);
            mCharacterAbilStr = rootView.findViewById(R.id.character_abil_str);
            mCharacterAbilCon = rootView.findViewById(R.id.character_abil_con);
            mCharacterAbilDex = rootView.findViewById(R.id.character_abil_dex);
            mCharacterAbilInt = rootView.findViewById(R.id.character_abil_int);
            mCharacterAbilWis = rootView.findViewById(R.id.character_abil_wis);
            mCharacterAbilCha = rootView.findViewById(R.id.character_abil_cha);
            mCharacterRace = rootView.findViewById(R.id.character_race);
            mCharacterClass = rootView.findViewById(R.id.character_class);
        }

        private void updateCharView(CharacterVersionQuery.GetCharactersByVersion item) {
            //convert the long values (of each ability score) to strings that can be shown as the character ability score values
            CharacterData.AbilityPoints abilityPoints = item.fragments().characterData().abilityPoints();
            mCharacterAbilStr.setText(getResources().getString(R.string.pref_str, longToString(abilityPoints.str())));
            mCharacterAbilCon.setText(getResources().getString(R.string.pref_con, longToString(abilityPoints.con())));
            mCharacterAbilDex.setText(getResources().getString(R.string.pref_dex, longToString(abilityPoints.dex())));
            mCharacterAbilInt.setText(getResources().getString(R.string.pref_int, longToString(abilityPoints.int_())));
            mCharacterAbilWis.setText(getResources().getString(R.string.pref_wis, longToString(abilityPoints.wis())));
            mCharacterAbilCha.setText(getResources().getString(R.string.pref_cha, longToString(abilityPoints.cha())));

            CharacterData.Race Races = item.fragments().characterData().race();
            CharacterData.Classql Classes = item.fragments().characterData().classql();
            Log.d("RACE", Races.fragments().raceData().name());
            if(Races != null){
                //Log.d("RACE DATA", Races.toString());
                mCharacterRace.setText(getResources().getString(R.string.pref_race, Races.fragments().raceData().name()));
            }

            if(Classes != null){
                mCharacterClass.setText(getResources().getString(R.string.pref_class, Classes.fragments().classData().name()));
            }

        }

        private String longToString(long longValue){
            return String.valueOf(longValue);
        }

    }

    public void generateCharacterStats(){
        //TODO unspaghet

        List<VersionSheetData.Stat> stats = versionData.fragments().versionSheetData().stats();
        List<String> abilityList = new ArrayList<>();
        List<String> modifierList = new ArrayList<>();
        CharacterData charData = mItem.fragments().characterData();
        HashMap<String, VersionSheetData.Stat> statObjMap = new HashMap<>();

        Log.d("VERSION_DATA", versionData.fragments().versionSheetData().stats().toString());

        CharacterData.Classql charClass = charData.classql();
        CharacterData.Race charRace = charData.race();
        CharacterData.AbilityPoints abilityScores = mItem.fragments().characterData().abilityPoints();

        //loop through stats list and add to hashmap with the key being the name of the stat
        for(int i = 0; i < stats.size(); i++){

            //get version sheet stats and add each one to a map
            VersionSheetData.Stat stat = stats.get(i);
            String key = keyFilter(stat);
            statObjMap.put(key, stat);
            charStatMap.put(key, 0d);

        }

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
                Double mapItem = charStatMap.get(mod.key());
                mapItem += mod.value();
                charStatMap.put(mod.key(), mapItem);
            }
        }

        //add ability points from character data
        for(Method m: abilityScores.getClass().getMethods()){
            String abilKey = m.getName().replaceAll("_", "");
            if(charStatMap.containsKey(abilKey)){
                Log.d("ADD_FROM_METHOD", abilKey);
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
                //get the ability modifier for each ability and
                for(VersionSheetData.Modifier mod : statMods){
                    if(abilityList.contains(mod.key())){
                        //int abilKeyIndex = abilityList.indexOf(mod.key());
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
                        charStatMap.put(statName.toLowerCase(), skillValue);
                    }

                    //create defense values
                    if(mod.key().contains("||")){
                        String[] options = mod.key().split("\\|\\|");

                        Log.d("OPTIONS", options[0] + " " + options[1]);
                        Log.d("DEFENSE", statName.toLowerCase());

                        String highestMod = selectDefMod(options);
                        Double value =  charStatMap.get(highestMod);
                        charStatMap.put(statName.toLowerCase(), value);

                    }
                    //TODO create health stats (move to ability mod loop?)

                }
            }
        }



        //Log.d("CHARACTER_ABILITIES", charStatMap.toString());
        //log map values
        printMapValues(charStatMap);

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

    public void printMapValues(HashMap<String, Double> map){
        for(String key : map.keySet()){
            Log.d("MAP_VALUE", key + " " + map.get(key).toString());
        }
    }

}


