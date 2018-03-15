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

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_MAP_ID = "map_id";

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

                HashMap<String, String> skillMap =(HashMap<String, String>)bundle.getSerializable(ARG_MAP_ID);
                String skillObj = "";
                if(skillMap != null){
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
        View view = inflater.inflate(R.layout.fragment_skills, container, false);

        SkillView mSkillView = new SkillView(view);
        if(mItem != null){
            mSkillView.updateSkillView(mItem);
        }
        return view;
    }

    private class SkillView {

        private TextView mCharacterSkill;
        private TextView mCharacterSkillContent;


        private SkillView(View rootView){

            mCharacterSkill = rootView.findViewById(R.id.skill_text);
            mCharacterSkillContent = rootView.findViewById(R.id.skill_content);
        }

        private void updateSkillView(SkillVersionQuery.GetVersionSkills item) {
            //convert the long values (of each ability score) to strings that can be shown as the character ability score values

            List<SkillData.Stat> SkillStats = mItem.fragments().skillData().stats();

            mCharacterSkill.setText(getResources().getString(R.string.pref_skill, SkillStats));
            mCharacterSkillContent.setText(getResources().getString(R.string.pref_skill, SkillStats));


            //mCharacterAbilStr.setText(getResources().getString(R.string.pref_str, longToString(abilityPoints.str())));

        }

        private String longToString(long longValue){
            return String.valueOf(longValue);
        }
    }


}
