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
    private static final String V_DATA = "VERSION_DATA";
    /**
     * The dummy content this fragment is presenting.
     */
    private CharacterVersionQuery.GetCharactersByVersion mItem;
    private VersionSheetQuery.GetVersionSheet versionData;
    private HashMap<String, Double> charStatMap;

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
        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey(ARG_MAP_ID)) {
            Bundle bundle = getArguments();
            //Log.d("CHAR_ARGUMENTS", getArguments().toString());
            HashMap<String, String> charMap =(HashMap<String, String>)bundle.getSerializable(ARG_MAP_ID);
            String charObj = "";
            if(charMap != null){
                //Log.d("CHAR_OBJ", charMap.get(bundle.getString(ARG_ITEM_ID)));
                charObj = charMap.get(bundle.getString((ARG_ITEM_ID)));
            }
            mItem = (new Gson()).fromJson(charObj, CharacterVersionQuery.GetCharactersByVersion.class);
            //Log.d("mItem CHECK: ", "" + mItem);
        }

        if(getArguments().containsKey(ARG_CHAR_STAT_DATA)){
            charStatMap = (HashMap<String, Double>) getArguments().getSerializable(ARG_CHAR_STAT_DATA);
        }
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
        private TextView mCharacterRace;
        private TextView mCharacterClass;


        private CharacterView(View rootView){
            mCharacterDetailInitiative = (TextView)rootView.findViewById(R.id.character_detail_initiative);
            mCharacterDetailHp = (TextView)rootView.findViewById(R.id.character_detail_hp);
            mCharacterDetailWill = rootView.findViewById(R.id.character_abil_wil);
            mCharacterDetailAC = rootView.findViewById(R.id.character_abil_ac);
            mCharacterDetailFort = rootView.findViewById(R.id.character_abil_fort);
            mCharacterDetailRef = rootView.findViewById(R.id.character_abil_ref);
            mCharacterDetailSpd = (TextView)rootView.findViewById(R.id.character_detail_spd);
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
            //TODO find better way to create and assign values for textviews
            //convert the long values (of each ability score) to strings that can be shown as the character ability score values
            mCharacterAbilStr.setText(String.valueOf(charStatMap.get("str")));
            mCharacterAbilCon.setText(String.valueOf(charStatMap.get("con")));
            mCharacterAbilDex.setText(String.valueOf(charStatMap.get("dex")));
            mCharacterAbilInt.setText(String.valueOf(charStatMap.get("int")));
            mCharacterAbilWis.setText(String.valueOf(charStatMap.get("wis")));
            mCharacterAbilCha.setText(String.valueOf(charStatMap.get("cha")));

            mCharacterDetailAC.setText(String.valueOf(charStatMap.get("ac")));
            mCharacterDetailFort.setText(String.valueOf(charStatMap.get("fort")));
            mCharacterDetailRef.setText(String.valueOf(charStatMap.get("ref")));
            mCharacterDetailWill.setText(String.valueOf(charStatMap.get("will")));
            mCharacterDetailHp.setText(String.valueOf(charStatMap.get("hp")));
            mCharacterDetailSpd.setText(String.valueOf(charStatMap.get("speed")));
            mCharacterDetailInitiative.setText(String.valueOf(charStatMap.get("initiative")));

            CharacterData.Race Races = item.fragments().characterData().race();
            CharacterData.Classql Classes = item.fragments().characterData().classql();
            //Log.d("RACE", Races.fragments().raceData().name());
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
}


