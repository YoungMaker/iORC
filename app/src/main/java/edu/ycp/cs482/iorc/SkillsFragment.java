package edu.ycp.cs482.iorc;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
        View rootView = inflater.inflate(R.layout.fragment_skills, container, false);

        ListView listView = rootView.findViewById(R.id.skillList);

//        RecyclerView Recycler = rootView.findViewById(R.id.skillList);

        HashMap<String, String> nameDescription = new HashMap<>();

        for(int i = 0; i < mItem.fragments().skillData.stats().size(); i++){
            nameDescription.put(mItem.fragments().skillData.stats().get(i).name(), mItem.fragments().skillData.stats().get(i).description());
            //Log.d("SKILL_NAME_CHECK: ", mItem.fragments().skillData.stats().get(i).name());
            //Log.d("SKILL_DESCRIP_CHECK: ", mItem.fragments().skillData.stats().get(i).description());
        }

        //Log.d("HASHMAP_SKILL", nameDescription.toString());

        List<HashMap<String, String>> listItems = new ArrayList<>();


        Iterator it = nameDescription.entrySet().iterator();
        while (it.hasNext()){
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems, R.layout.skill_list_content,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.skill_his_text, R.id.skill_his_description});

        for(int i = 0; i < 17; i++){
            Log.d("SKILL_ADAPTER", adapter.getItem(i).toString());
        }

        listView.setAdapter(adapter);

//        Recycler.setAdapter(adapter);





       // SkillView mSkillView = new SkillView(rootView);
        //if(mItem != null){
       //     mSkillView.updateSkillView(mItem);
       // }

        return rootView;
//        return listView;
    }

    private class SkillView {

        private TextView mCharacterSkillHis;
        private TextView mCharacterSkillAcro;
        private TextView mCharacterSkillArcana;
        private TextView mCharacterSkillAthl;
        private TextView mCharacterSkillBluff;
        private TextView mCharacterSkillDiplo;
        private TextView mCharacterSkillEndur;
        private TextView mCharacterSkillHeal;
        private TextView mCharacterSkillInsight;
        private TextView mCharacterSkillIntim;
        private TextView mCharacterSkillNature;
        private TextView mCharacterSkillPercept;
        private TextView mCharacterSkillReligion;
        private TextView mCharacterSkillStealth;
        private TextView mCharacterSkillStreetw;
        private TextView mCharacterSkillThiev;
        private TextView mCharacterSkillDungeon;

        private TextView mCharacterSkillHisDescription;
        private TextView mCharacterSkillAcroDescription;
        private TextView mCharacterSkillArcanaDescription;
        private TextView mCharacterSkillAthlDescription;
        private TextView mCharacterSkillBluffDescription;
        private TextView mCharacterSkillDiploDescription;
        private TextView mCharacterSkillEndurDescription;
        private TextView mCharacterSkillHealDescription;
        private TextView mCharacterSkillInsightDescription;
        private TextView mCharacterSkillIntimDescription;
        private TextView mCharacterSkillNatureDescription;
        private TextView mCharacterSkillPerceptDescription;
        private TextView mCharacterSkillReligionDescription;
        private TextView mCharacterSkillStealthDescription;
        private TextView mCharacterSkillStreetwDescription;
        private TextView mCharacterSkillThievDescription;
        private TextView mCharacterSkillDungeonDescription;


        private SkillView(View rootView){

//            Log.d("SKILL_TEXT: ", mCharacterSkill.toString());

//            mCharacterSkillHis = rootView.findViewById(R.id.skill_his_text);
//            mCharacterSkillAcro = rootView.findViewById(R.id.skill_acro_text);
//            mCharacterSkillArcana = rootView.findViewById(R.id.skill_arcana_text);
//            mCharacterSkillAthl = rootView.findViewById(R.id.skill_athl_text);
//            mCharacterSkillBluff = rootView.findViewById(R.id.skill_bluff_text);
//            mCharacterSkillDiplo = rootView.findViewById(R.id.skill_diplo_text);
//            mCharacterSkillEndur = rootView.findViewById(R.id.skill_endur_text);
//            mCharacterSkillHeal = rootView.findViewById(R.id.skill_heal_text);
//            mCharacterSkillInsight = rootView.findViewById(R.id.skill_insight_text);
//            mCharacterSkillIntim = rootView.findViewById(R.id.skill_intim_text);
//            mCharacterSkillNature = rootView.findViewById(R.id.skill_nature_text);
//            mCharacterSkillPercept = rootView.findViewById(R.id.skill_percept_text);
//            mCharacterSkillReligion = rootView.findViewById(R.id.skill_religion_text);
//            mCharacterSkillStealth = rootView.findViewById(R.id.skill_stealth_text);
//            mCharacterSkillStreetw = rootView.findViewById(R.id.skill_streetw_text);
//            mCharacterSkillThiev = rootView.findViewById(R.id.skill_thiev_text);
//            mCharacterSkillDungeon = rootView.findViewById(R.id.skill_dungeon_text);

//            mCharacterSkillHisDescription = rootView.findViewById(R.id.skill_his_description);
//            mCharacterSkillAcroDescription = rootView.findViewById(R.id.skill_acro_description);
//            mCharacterSkillArcanaDescription = rootView.findViewById(R.id.skill_arcana_description);
//            mCharacterSkillAthlDescription = rootView.findViewById(R.id.skill_athl_description);
//            mCharacterSkillBluffDescription = rootView.findViewById(R.id.skill_bluff_description);
//            mCharacterSkillDiploDescription = rootView.findViewById(R.id.skill_diplo_description);
//            mCharacterSkillEndurDescription = rootView.findViewById(R.id.skill_endur_description);
//            mCharacterSkillHealDescription = rootView.findViewById(R.id.skill_heal_description);
//            mCharacterSkillInsightDescription = rootView.findViewById(R.id.skill_insight_description);
//            mCharacterSkillIntimDescription = rootView.findViewById(R.id.skill_intim_description);
//            mCharacterSkillNatureDescription = rootView.findViewById(R.id.skill_nature_description);
//            mCharacterSkillPerceptDescription = rootView.findViewById(R.id.skill_percept_description);
//            mCharacterSkillReligionDescription = rootView.findViewById(R.id.skill_religion_description);
//            mCharacterSkillStealthDescription = rootView.findViewById(R.id.skill_stealth_description);
//            mCharacterSkillStreetwDescription = rootView.findViewById(R.id.skill_streetw_description);
//            mCharacterSkillThievDescription = rootView.findViewById(R.id.skill_thiev_description);
//            mCharacterSkillDungeonDescription = rootView.findViewById(R.id.skill_dungeon_description);
        }

        private void updateSkillView(SkillVersionQuery.GetVersionSkills item) {
            //Log.d("THING: ", item.fragments().skillData.stats().toString());
            if(item.fragments().skillData.stats() != null){
                List<SkillData.Stat> SkillStats = item.fragments().skillData.stats();
                //Log.d("SKILL_THING: ", SkillStats.toString());
                //SkillData.Modifier SkillMods = (SkillData.Modifier) item.fragments().skillData().stats();
                //CharacterData.AbilityPoints abilityPoints = item.fragments().characterData.abilityPoints();

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

                mCharacterSkillDiplo.setText(getResources().getString(R.string.pref_skill_diplomancy, SkillStats.get(5).name()));
                mCharacterSkillDiploDescription.setText(getResources().getString(R.string.pref_skill_diplomancy_description, SkillStats.get(5).description()));

                mCharacterSkillEndur.setText(getResources().getString(R.string.pref_skill_endurance, SkillStats.get(6).name()));
                mCharacterSkillEndurDescription.setText(getResources().getString(R.string.pref_skill_endurance_description, SkillStats.get(6).description()));

                mCharacterSkillHeal.setText(getResources().getString(R.string.pref_skill_heal, SkillStats.get(7).name()));
                mCharacterSkillHealDescription.setText(getResources().getString(R.string.pref_skill_heal_description, SkillStats.get(7).description()));

                mCharacterSkillInsight.setText(getResources().getString(R.string.pref_skill_insight, SkillStats.get(8).name()));
                mCharacterSkillInsightDescription.setText(getResources().getString(R.string.pref_skill_insight_description, SkillStats.get(8).description()));

                mCharacterSkillIntim.setText(getResources().getString(R.string.pref_skill_intimidate, SkillStats.get(9).name()));
                mCharacterSkillIntimDescription.setText(getResources().getString(R.string.pref_skill_intimidate_description, SkillStats.get(9).description()));

                mCharacterSkillNature.setText(getResources().getString(R.string.pref_skill_nature, SkillStats.get(10).name()));
                mCharacterSkillNatureDescription.setText(getResources().getString(R.string.pref_skill_nature_description, SkillStats.get(10).description()));

                mCharacterSkillPercept.setText(getResources().getString(R.string.pref_skill_perception, SkillStats.get(11).name()));
                mCharacterSkillPerceptDescription.setText(getResources().getString(R.string.pref_skill_perception_description, SkillStats.get(11).description()));

                mCharacterSkillReligion.setText(getResources().getString(R.string.pref_skill_religion, SkillStats.get(12).name()));
                mCharacterSkillReligionDescription.setText(getResources().getString(R.string.pref_skill_religion_description, SkillStats.get(12).description()));

                mCharacterSkillStealth.setText(getResources().getString(R.string.pref_skill_stealth, SkillStats.get(13).name()));
                mCharacterSkillStealthDescription.setText(getResources().getString(R.string.pref_skill_stealth_description, SkillStats.get(13).description()));

                mCharacterSkillStreetw.setText(getResources().getString(R.string.pref_skill_streetwise, SkillStats.get(14).name()));
                mCharacterSkillStreetwDescription.setText(getResources().getString(R.string.pref_skill_streetwise_description, SkillStats.get(14).description()));

                mCharacterSkillThiev.setText(getResources().getString(R.string.pref_skill_thievery, SkillStats.get(15).name()));
                mCharacterSkillThievDescription.setText(getResources().getString(R.string.pref_skill_thievery_description, SkillStats.get(15).description()));

                mCharacterSkillDungeon.setText(getResources().getString(R.string.pref_skill_dungeoneering, SkillStats.get(16).name()));
                mCharacterSkillDungeonDescription.setText(getResources().getString(R.string.pref_skill_dungeoneering_description, SkillStats.get(16).description()));


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
