package edu.ycp.cs482.iorc;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.apollographql.apollo.api.cache.http.HttpCachePolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import edu.ycp.cs482.iorc.fragment.SkillData;


/**
 * A simple {@link Fragment} subclass.
 */
public class SkillsFragment extends Fragment {

    public static final String ARG_ITEM_ID = "skill_id";
    public static final String ARG_MAP_ID = "skillmap_id";

    private SkillVersionQuery.GetVersionSkills mItem;

    public SkillsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO receive map from character list activity
        Log.d("mItem CHECK: ", "Loading Map");
        if(getArguments() != null){
            if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey(ARG_MAP_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                //mItem = DummyContent.CHARACTER_MAP.get(getArguments().getString(ARG_ITEM_ID));
                Bundle bundle = getArguments();
                Log.d("ARGUMENTS", getArguments().toString());
                HashMap<String, String> skillMap =(HashMap<String, String>)bundle.getSerializable(ARG_MAP_ID);
                String skillObj = "";
                Log.d("SKILL_OBJ", skillMap.get(bundle.getString(ARG_ITEM_ID)));
                if(skillMap != null){
                    //Log.d("SKILL_OBJ", skillMap.get(bundle.getString((ARG_ITEM_ID))).toString());
                    skillObj = skillMap.get(bundle.getString((ARG_ITEM_ID)));
                }
                mItem = (new Gson()).fromJson(skillObj, SkillVersionQuery.GetVersionSkills.class);
                Log.d("mItem CHECK: ", "" + mItem);
            }
        }
        Log.d("mItem CHECK: ", "Finished Loading Map");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.skill_list_content, container, false);

        SkillView mSkillView = new SkillView(rootView);
        if(mItem != null){
            mSkillView.updateSkillView(mItem);
        }
        return rootView;
    }

    private class SkillView {

        private TextView mCharacterSkillHis;
        private TextView mCharacterSkillAcro;
        private TextView mCharacterSkillArcana;
        private TextView mCharacterSkillAthl;
        private TextView mCharacterSkillBluff;

        private TextView mCharacterSkillHisDescription;
        private TextView mCharacterSkillAcroDescription;
        private TextView mCharacterSkillArcanaDescription;
        private TextView mCharacterSkillAthlDescription;
        private TextView mCharacterSkillBluffDescription;


        private SkillView(View rootView){

            //Log.d("SKILL_TEXT: ", mCharacterSkill.toString());
            mCharacterSkillHis = rootView.findViewById(R.id.skill_his_text);
            mCharacterSkillAcro = rootView.findViewById(R.id.skill_acro_text);
            mCharacterSkillArcana = rootView.findViewById(R.id.skill_arcana_text);
            mCharacterSkillAthl = rootView.findViewById(R.id.skill_athl_text);
            mCharacterSkillBluff = rootView.findViewById(R.id.skill_bluff_text);

            mCharacterSkillHisDescription = rootView.findViewById(R.id.skill_his_description);
            mCharacterSkillAcroDescription = rootView.findViewById(R.id.skill_acro_description);
            mCharacterSkillArcanaDescription = rootView.findViewById(R.id.skill_arcana_description);
            mCharacterSkillAthlDescription = rootView.findViewById(R.id.skill_athl_description);
            mCharacterSkillBluffDescription = rootView.findViewById(R.id.skill_bluff_description);
        }

        private void updateSkillView(SkillVersionQuery.GetVersionSkills item) {
            //convert the long values (of each ability score) to strings that can be shown as the character ability score values
            Log.d("THING: ", item.fragments().skillData.stats().toString());
            if(item.fragments().skillData.stats() != null){
                List<SkillData.Stat> SkillStats = item.fragments().skillData.stats();
                Log.d("SKILL_THING: ", SkillStats.toString());
                //SkillData.Modifier SkillMods = (SkillData.Modifier) item.fragments().skillData().stats();
                //CharacterData.AbilityPoints abilityPoints = item.fragments().characterData.abilityPoints();

                //mCharacterSkill.setText(SkillStats.toString());
//                for(int j = 0; j < SkillStats.size(); j++){
//                    Log.d("SKILL_NAME: ", SkillStats.get(j).name());
//                }

                //for(int i = 0; i < SkillStats.size(); i++){
                   // mCharacterSkill.setText(getResources().getString(R.string.pref_skill_history, SkillStats.get(i).name()));
                    //mCharacterSkillDescription.setText(getResources().getString(R.string.pref_skill__history_description, SkillStats.get(i).description()));
                //}
                mCharacterSkillHis.setText(getResources().getString(R.string.pref_skill_history, SkillStats.get(0).name()));
                mCharacterSkillHisDescription.setText(getResources().getString(R.string.pref_skill__history_description, SkillStats.get(0).description()));

                mCharacterSkillAcro.setText(getResources().getString(R.string.pref_skill_acrobatics, SkillStats.get(1).name()));
                mCharacterSkillAcroDescription.setText(getResources().getString(R.string.pref_skill_acrobatics_description, SkillStats.get(1).description()));

                mCharacterSkillArcana.setText(getResources().getString(R.string.pref_skill_arcana, SkillStats.get(2).name()));
                mCharacterSkillArcanaDescription.setText(getResources().getString(R.string.pref_skill_arcana_description, SkillStats.get(2).description()));

                mCharacterSkillAthl.setText(getResources().getString(R.string.pref_skill_athletics, SkillStats.get(3).name()));
                mCharacterSkillAthlDescription.setText(getResources().getString(R.string.pref_skill_athletics_description, SkillStats.get(3).description()));

                mCharacterSkillBluff.setText(getResources().getString(R.string.pref_skill_bluff, SkillStats.get(4).name()));
                mCharacterSkillBluffDescription.setText(getResources().getString(R.string.pref_skill_bluff_description, SkillStats.get(4).description()));


                //mCharacterSkillContent.setText(getResources().getString(R.string.pref_skill, SkillMods));
            }
            else {
                Log.d("NULL STAT: ", "STAT IS NULL");
            }



            //mCharacterAbilStr.setText(getResources().getString(R.string.pref_str, longToString(abilityPoints.str())));

        }

        private String longToString(long longValue){
            return String.valueOf(longValue);
        }
    }


}
