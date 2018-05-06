package edu.ycp.cs482.iorc.Fragments.MasterFlows;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ycp.cs482.iorc.Activities.CharacterDetailActivity;
import edu.ycp.cs482.iorc.Activities.CharacterListActivity;
import edu.ycp.cs482.iorc.CharacterVersionQuery;
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.SkillsFragment;
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
    public static final String ARG_CHAR_STAT_DATA = "CHAR_STAT_DATA";
    public static final String ARG_DEF_TABLE_DATA = "DEF_TABLE";
    private static final String V_DATA = "VERSION_DATA";
    /**
     * The dummy content this fragment is presenting.
     */
    private CharacterData mItem;
    private VersionSheetData versionData;
    private HashMap<String, Double> charStatMap;
    private HashMap<String, String> acAddModTab = new HashMap<>();
    private HashMap<String, String> fortAddModTab = new HashMap<>();
    private HashMap<String, String> refAddModTab = new HashMap<>();
    private HashMap<String, String> willAddModTab = new HashMap<>();
    private final String currencyType = "currency";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharacterDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //receive map from character list activity
        /*if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey(ARG_MAP_ID)) {
            Bundle bundle = getArguments();
            //Log.d("CHAR_ARGUMENTS", getArguments().toString());
            HashMap<String, String> charMap =(HashMap<String, String>)bundle.getSerializable(ARG_MAP_ID);
            String charObj = "";
            if(charMap != null){
                //Log.d("CHAR_OBJ", charMap.get(bundle.getString(ARG_ITEM_ID)));
                charObj = charMap.get(bundle.getString((ARG_ITEM_ID)));
            }
            //mItem = (new Gson()).fromJson(charObj, CharacterVersionQuery.GetCharactersByVersion.class);
            //Log.d("mItem CHECK: ", "" + mItem);
        }*/

       /* if(getArguments().containsKey(ARG_CHAR_STAT_DATA)){
            charStatMap = (HashMap<String, Double>) getArguments().getSerializable(ARG_CHAR_STAT_DATA);
            //Log.d("KEY SET", charStatMap.keySet().toString());
        }

        if(getArguments().containsKey(ARG_DEF_TABLE_DATA)){
            HashMap<String, String> defMap = (HashMap<String, String>) getArguments().getSerializable(ARG_DEF_TABLE_DATA);
            acAddModTab.put("abil", (defMap.get("ac_abil") != null) ? defMap.get("ac_abil") : "0");
            acAddModTab.put("class", (defMap.get("ac_class") != null) ? defMap.get("ac_class") : "0");
            acAddModTab.put("feat", (defMap.get("ac_feat") != null) ? defMap.get("ac_feat") : "0");
            acAddModTab.put("enh", (defMap.get("ac_enh") != null) ? defMap.get("ac_enh") : "0");

            fortAddModTab.put("abil", (defMap.get("fort_abil") != null) ? defMap.get("fort_abil") : "0");
            fortAddModTab.put("class", (defMap.get("fort_class") != null) ? defMap.get("fort_class") : "0");
            fortAddModTab.put("feat", (defMap.get("fort_feat") != null) ? defMap.get("fort_feat") : "0");
            fortAddModTab.put("enh", (defMap.get("fort_enh") != null) ? defMap.get("fort_enh") : "0");

            refAddModTab.put("abil", (defMap.get("ref_abil") != null) ? defMap.get("ref_abil") : "0");
            refAddModTab.put("class", (defMap.get("ref_class") != null) ? defMap.get("ref_class") : "0");
            refAddModTab.put("feat", (defMap.get("ref_feat") != null) ? defMap.get("ref_feat") : "0");
            refAddModTab.put("enh", (defMap.get("ref_enh") != null) ? defMap.get("ref_enh") : "0");

            willAddModTab.put("abil", (defMap.get("will_abil") != null) ? defMap.get("will_abil") : "0");
            willAddModTab.put("class", (defMap.get("will_class") != null) ? defMap.get("will_class") : "0");
            willAddModTab.put("feat", (defMap.get("will_feat") != null) ? defMap.get("will_feat") : "0");
            willAddModTab.put("enh", (defMap.get("will_enh") != null) ? defMap.get("will_enh") : "0");
        }*/

        //Log.d("mItem CHECK: ", "Finished Loading Map");
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
        //TODO find better way to create and assign values for textviews
        private TextView mCharacterDetailInitiative;
        private TextView mCharacterDetailHp;
        private TextView mCharacterDetailWill;
        private TextView mCharacterDetailFort;
        private TextView mCharacterDetailRef;
        private TextView mCharacterDetailAC;
        private TextView mCharacterDetailSpd;
        private TextView mCharacterAbilStr;
        private TextView mCharacterAbilCon;
        private TextView mCharacterAbilDex;
        private TextView mCharacterAbilInt;
        private TextView mCharacterAbilWis;
        private TextView mCharacterAbilCha;
        private TextView mCharacterAbilStrMod;
        private TextView mCharacterAbilConMod;
        private TextView mCharacterAbilDexMod;
        private TextView mCharacterAbilIntMod;
        private TextView mCharacterAbilWisMod;
        private TextView mCharacterAbilChaMod;

        private TextView mCharacterRace;
        private TextView mCharacterClass;
        private TextView mCharacterRaceDesc;
        private TextView mCharacterClassDesc;


        //TODO read up on iterating through view IDS so I can get rid of this mess of variables
        private List<View> mCharAcList = new ArrayList<>();
        private TextView mCharacterAcAbil;
        private TextView mCharacterAcClass;
        private TextView mCharacterAcFeat;
        private TextView mCharacterAcEnh;

        private TextView mCharacterFortAbil;
        private TextView mCharacterFortClass;
        private TextView mCharacterFortFeat;
        private TextView mCharacterFortEnh;

        private TextView mCharacterRefAbil;
        private TextView mCharacterRefClass;
        private TextView mCharacterRefFeat;
        private TextView mCharacterRefEnh;

        private TextView mCharacterWillAbil;
        private TextView mCharacterWillClass;
        private TextView mCharacterWillFeat;
        private TextView mCharacterWillEnh;

        private View mCharacterAcTab;
        private View mCharacterFortTab;
        private View mCharacterRefTab;
        private View mCharacterWillTab;

        private TextView mCharacterWealth;
        private TextView mCurrencyType;


        private List<View> mCharFortList = new ArrayList<>();
        private List<View> mCharRefList = new ArrayList<>();
        private List<View> mCharWillList = new ArrayList<>();


        private CharacterView(View rootView){

            //mCharacterAcTab = rootView.findViewById(R.id.ac_mod_table);
            mCharacterFortTab = rootView.findViewById(R.id.fort_mod_table);
            mCharacterRefTab = rootView.findViewById(R.id.ref_mod_table);
            mCharacterWillTab = rootView.findViewById(R.id.will_mod_table);

            //TODO read up on iterating through view IDS so I can get rid of this mess of variables
            /*for(int i = 0; i < mCharacterAcTab; i++){

            }*/

            mCharacterDetailInitiative = rootView.findViewById(R.id.character_detail_initiative);
            mCharacterDetailHp = rootView.findViewById(R.id.character_detail_hp);
            mCharacterDetailWill = rootView.findViewById(R.id.character_abil_wil);
            mCharacterDetailAC = rootView.findViewById(R.id.character_abil_ac);
            mCharacterDetailFort = rootView.findViewById(R.id.character_abil_fort);
            mCharacterDetailRef = rootView.findViewById(R.id.character_abil_ref);
            mCharacterDetailSpd = rootView.findViewById(R.id.character_detail_spd);
            //abils
            mCharacterAbilStr = rootView.findViewById(R.id.character_abil_str);
            mCharacterAbilCon = rootView.findViewById(R.id.character_abil_con);
            mCharacterAbilDex = rootView.findViewById(R.id.character_abil_dex);
            mCharacterAbilInt = rootView.findViewById(R.id.character_abil_int);
            mCharacterAbilWis = rootView.findViewById(R.id.character_abil_wis);
            mCharacterAbilCha = rootView.findViewById(R.id.character_abil_cha);
            //abilmods
            mCharacterAbilStrMod = rootView.findViewById(R.id.character_abil_str_mod);
            mCharacterAbilConMod = rootView.findViewById(R.id.character_abil_con_mod);
            mCharacterAbilDexMod = rootView.findViewById(R.id.character_abil_dex_mod);
            mCharacterAbilIntMod = rootView.findViewById(R.id.character_abil_int_mod);
            mCharacterAbilWisMod = rootView.findViewById(R.id.character_abil_wis_mod);
            mCharacterAbilChaMod = rootView.findViewById(R.id.character_abil_cha_mod);

            mCharacterRace = rootView.findViewById(R.id.character_race);
            mCharacterClass = rootView.findViewById(R.id.character_class);

            mCharacterRaceDesc = rootView.findViewById(R.id.character_race_desc);
            mCharacterClassDesc = rootView.findViewById(R.id.character_class_desc);
            mCharacterWealth = rootView.findViewById(R.id.money_amount);
            mCurrencyType = rootView.findViewById(R.id.currency_type);

            //mCharacterAcAbil = mCharacterAcTab.findViewById(R.id.mod_abil);
            //mCharacterAcClass = mCharacterAcTab.findViewById(R.id.mod_class);
            //mCharacterAcFeat = mCharacterAcTab.findViewById(R.id.mod_feat);
            //mCharacterAcEnh = mCharacterAcTab.findViewById(R.id.mod_enh);

            mCharacterFortAbil = mCharacterFortTab.findViewById(R.id.mod_abil);
            mCharacterFortClass = mCharacterFortTab.findViewById(R.id.mod_class);
            mCharacterFortFeat = mCharacterFortTab.findViewById(R.id.mod_feat);

            mCharacterRefAbil = mCharacterRefTab.findViewById(R.id.mod_abil);
            mCharacterRefClass = mCharacterRefTab.findViewById(R.id.mod_class);
            mCharacterRefFeat = mCharacterRefTab.findViewById(R.id.mod_feat);

            mCharacterWillAbil = mCharacterWillTab.findViewById(R.id.mod_abil);
            mCharacterWillClass = mCharacterWillTab.findViewById(R.id.mod_class);
            mCharacterWillFeat = mCharacterWillTab.findViewById(R.id.mod_feat);


        }

        private void updateCharView(CharacterData item) {
            //TODO find better way to create and assign values for textviews
            if (charStatMap != null) {

                //convert the long values (of each ability score) to strings that can be shown as the character ability score values
                mCharacterAbilStr.setText(String.valueOf(Math.round(charStatMap.get("str"))));
                mCharacterAbilCon.setText(String.valueOf(Math.round(charStatMap.get("con"))));
                mCharacterAbilDex.setText(String.valueOf(Math.round(charStatMap.get("dex"))));
                mCharacterAbilInt.setText(String.valueOf(Math.round(charStatMap.get("int"))));
                mCharacterAbilWis.setText(String.valueOf(Math.round(charStatMap.get("wis"))));
                mCharacterAbilCha.setText(String.valueOf(Math.round(charStatMap.get("cha"))));

                //abilmods
                mCharacterAbilStrMod.setText(String.valueOf(Math.round(charStatMap.get("str_mod"))));
                mCharacterAbilConMod.setText(String.valueOf(Math.round(charStatMap.get("con_mod"))));
                mCharacterAbilDexMod.setText(String.valueOf(Math.round(charStatMap.get("dex_mod"))));
                mCharacterAbilIntMod.setText(String.valueOf(Math.round(charStatMap.get("int_mod"))));
                mCharacterAbilWisMod.setText(String.valueOf(Math.round(charStatMap.get("wis_mod"))));
                mCharacterAbilChaMod.setText(String.valueOf(Math.round(charStatMap.get("cha_mod"))));

                mCharacterDetailAC.setText(String.valueOf(Math.round(charStatMap.get("ac"))));
                mCharacterDetailFort.setText(String.valueOf(Math.round(charStatMap.get("fort"))));
                mCharacterDetailRef.setText(String.valueOf(Math.round(charStatMap.get("ref"))));
                mCharacterDetailWill.setText(String.valueOf(Math.round(charStatMap.get("will"))));
                mCharacterDetailHp.setText(String.valueOf(Math.round(charStatMap.get("health points"))));
                mCharacterDetailSpd.setText(String.valueOf(Math.round(charStatMap.get("speed"))));
                mCharacterDetailInitiative.setText(String.valueOf(Math.round(charStatMap.get("initiative"))));
            }

            //TODO read up on iterating through view IDS so I can get rid of this mess of variables
//            mCharacterAcAbil.setText(String.valueOf(acAddModTab.get("abil")));
//            mCharacterAcClass.setText(String.valueOf(acAddModTab.get("class")));
//            mCharacterAcFeat.setText(String.valueOf(acAddModTab.get("feat")));
//            mCharacterAcEnh.setText(String.valueOf(acAddModTab.get("enh")));

            mCharacterFortAbil.setText(String.valueOf(Math.round(Float.parseFloat(fortAddModTab.get("abil")))));
            mCharacterFortClass.setText(String.valueOf(Math.round(Float.parseFloat(fortAddModTab.get("class")))));
            mCharacterFortFeat.setText(String.valueOf(Math.round(Float.parseFloat(fortAddModTab.get("feat")))));
            //mCharacterFortEnh.setText(String.valueOf(fortAddModTab.get("enh")));

            mCharacterRefAbil.setText(String.valueOf(Math.round(Float.parseFloat(refAddModTab.get("abil")))));
            mCharacterRefClass.setText(String.valueOf(Math.round(Float.parseFloat(refAddModTab.get("class")))));
            mCharacterRefFeat.setText(String.valueOf(Math.round(Float.parseFloat(refAddModTab.get("feat")))));
            //mCharacterRefEnh.setText(String.valueOf(refAddModTab.get("enh")));

            mCharacterWillAbil.setText(String.valueOf(Math.round(Float.parseFloat(willAddModTab.get("abil")))));
            mCharacterWillClass.setText(String.valueOf(Math.round(Float.parseFloat(willAddModTab.get("class")))));
            mCharacterWillFeat.setText(String.valueOf(Math.round(Float.parseFloat(willAddModTab.get("feat")))));
            //mCharacterWillEnh.setText(String.valueOf(willAddModTab.get("enh")));

            CharacterData.Race Races = item.race();
            CharacterData.Classql Classes = item.classql();
            //Log.d("RACE", Races.fragments().raceData().name());
            if(Races != null){
                //Log.d("RACE DATA", Races.toString());
                mCharacterRace.setText(getResources().getString(R.string.pref_race, Races.fragments().raceData().name()));
                //mCharacterRaceDesc.setText(getResources().getString(R.string.pref_race_desc, Races.fragments().raceData().description()));
            }

            if(Classes != null){
                mCharacterClass.setText(getResources().getString(R.string.pref_class, Classes.fragments().classData().name()));
                //mCharacterClassDesc.setText(getResources().getString(R.string.pref_class_desc, Classes.fragments().classData().description()));
            }

            //find out why character data does not have money
            //get currency from version
            String currencyName = "NONE";
            mCharacterWealth.setText(getString(R.string.money_title, String.valueOf(item.money())));
            if(versionData != null){
                for(int i = 0; i < versionData.infoList().size(); i++){
                    VersionSheetData.InfoList info = versionData.infoList().get(i);
                    if(info.type().equals(currencyType)){
                        currencyName = info.value();
                        //Log.d("CURRENCY_NAME", currencyName);
                        break;
                    }
                    //Log.d("LOOPY_BOI", info.type());
                }

            }
            mCurrencyType.setText(currencyName);
        }

        private String longToString(long longValue){
            return String.valueOf(longValue);
        }

    }

    public void loadVersionData(VersionSheetData versionSheetData){
        Log.d("TEST", "loadVersionData: " + versionSheetData.toString());
        versionData = versionSheetData;
    }

    public void loadCharacterData(CharacterData characterData, HashMap<String, Double> statMap, HashMap<String, String> defMap){
        Log.d("TEST", "loadCharacterData: " + characterData.toString());
        mItem = characterData;
        if(statMap != null){
            charStatMap = statMap;
            Log.d("STAT_MAP", statMap.toString());
        }


        acAddModTab.put("abil", (defMap.get("ac_abil") != null) ? defMap.get("ac_abil") : "0");
        acAddModTab.put("class", (defMap.get("ac_class") != null) ? defMap.get("ac_class") : "0");
        acAddModTab.put("feat", (defMap.get("ac_feat") != null) ? defMap.get("ac_feat") : "0");
        acAddModTab.put("enh", (defMap.get("ac_enh") != null) ? defMap.get("ac_enh") : "0");

        fortAddModTab.put("abil", (defMap.get("fort_abil") != null) ? defMap.get("fort_abil") : "0");
        fortAddModTab.put("class", (defMap.get("fort_class") != null) ? defMap.get("fort_class") : "0");
        fortAddModTab.put("feat", (defMap.get("fort_feat") != null) ? defMap.get("fort_feat") : "0");
        fortAddModTab.put("enh", (defMap.get("fort_enh") != null) ? defMap.get("fort_enh") : "0");

        refAddModTab.put("abil", (defMap.get("ref_abil") != null) ? defMap.get("ref_abil") : "0");
        refAddModTab.put("class", (defMap.get("ref_class") != null) ? defMap.get("ref_class") : "0");
        refAddModTab.put("feat", (defMap.get("ref_feat") != null) ? defMap.get("ref_feat") : "0");
        refAddModTab.put("enh", (defMap.get("ref_enh") != null) ? defMap.get("ref_enh") : "0");

        willAddModTab.put("abil", (defMap.get("will_abil") != null) ? defMap.get("will_abil") : "0");
        willAddModTab.put("class", (defMap.get("will_class") != null) ? defMap.get("will_class") : "0");
        willAddModTab.put("feat", (defMap.get("will_feat") != null) ? defMap.get("will_feat") : "0");
        willAddModTab.put("enh", (defMap.get("will_enh") != null) ? defMap.get("will_enh") : "0");
    }
}


