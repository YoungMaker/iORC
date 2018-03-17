package edu.ycp.cs482.iorc;

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

import java.util.HashMap;

import edu.ycp.cs482.iorc.fragment.CharacterData;

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



    /**
     * The dummy content this fragment is presenting.
     */
    private CharacterVersionQuery.GetCharactersByVersion mItem;

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
                appBarLayout.setTitle(mItem.fragments().characterData.name());
            }
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
            //mCharacterRace = rootView.findViewById(R.id.character_race);
            //mCharacterClass = rootView.findViewById(R.id.character_class);
        }

        private void updateCharView(CharacterVersionQuery.GetCharactersByVersion item) {
            //convert the long values (of each ability score) to strings that can be shown as the character ability score values
            CharacterData.AbilityPoints abilityPoints = item.fragments().characterData.abilityPoints();
            mCharacterAbilStr.setText(getResources().getString(R.string.pref_str, longToString(abilityPoints.str())));
            mCharacterAbilCon.setText(getResources().getString(R.string.pref_con, longToString(abilityPoints.con())));
            mCharacterAbilDex.setText(getResources().getString(R.string.pref_dex, longToString(abilityPoints.dex())));
            mCharacterAbilInt.setText(getResources().getString(R.string.pref_int, longToString(abilityPoints.int_())));
            mCharacterAbilWis.setText(getResources().getString(R.string.pref_wis, longToString(abilityPoints.wis())));
            mCharacterAbilCha.setText(getResources().getString(R.string.pref_cha, longToString(abilityPoints.cha())));

            //CharacterData.Race Races = item.fragments().characterData.race();
            //mCharacterRace.setText(getResources().getString(R.string.pref_race, Races.__typename()));

            //CharacterData.Classql Classes = item.fragments().characterData.classql();
            //mCharacterClass.setText(getResources().getString(R.string.pref_class, Classes.__typename()));
            //mCharacterDetailRef.setText(getResources().getString(R.string.pref_ref, item.ref));


            //mCharacterDetailFort.setText(getResources().getString(R.string.pref_fort, item.fort));
           // mCharacterDetailSpd.setText(getResources().getString(R.string.pref_cha, item.sp));

        }

        private String longToString(long longValue){
            return String.valueOf(longValue);
        }
    }

}


