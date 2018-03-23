package edu.ycp.cs482.iorc.Fragments.MasterFlows;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import edu.ycp.cs482.iorc.Activities.ClassRaceDetailActivity;
import edu.ycp.cs482.iorc.Activities.ClassRaceListActivity;
import edu.ycp.cs482.iorc.ClassVersionQuery;
import edu.ycp.cs482.iorc.Fragments.ModifierFragment;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.RaceVersionQuery;
import edu.ycp.cs482.iorc.fragment.ClassData;
import edu.ycp.cs482.iorc.fragment.RaceData;

/**
 * A fragment representing a single ClassRace detail screen.
 * This fragment is either contained in a {@link ClassRaceListActivity}
 * in two-pane mode (on tablets) or a {@link ClassRaceDetailActivity}
 * on handsets.
 */
public class ClassRaceDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_CLASS_MAP = "CLASS_MAP";
    public static final String ARG_CLASS_MAP_ID = "CLASS_MAP_ID";
    public static final String ARG_RACE_MAP = "RACE_MAP";
    public static final String ARG_RACE_MAP_ID = "RACE_MAP_ID";
    public static final String ARG_SHOW_ITEM = "SHOW_RACE";
    public static final String CREATION_DATA = "CREATION_DATA";

    /**
     * The dummy content this fragment is presenting.
     */
    private ClassVersionQuery.GetClassesByVersion mItem;
    private RaceVersionQuery.GetRacesByVersion amItem;

    private boolean showRace = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClassRaceDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey(ARG_SHOW_ITEM)) {
            showRace = getArguments().getBoolean(ARG_SHOW_ITEM);
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.CLASS_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Bundle bundle = getArguments();
            if(bundle != null && bundle.containsKey(ARG_RACE_MAP) && bundle.containsKey(ARG_RACE_MAP_ID)){

                String raceObj = "";
                HashMap<String, String> raceMap =(HashMap<String, String>)bundle.getSerializable(ARG_RACE_MAP);
                if (raceMap != null) {
                    raceObj = raceMap.get(bundle.getString(ARG_RACE_MAP_ID));
                }

                //Log.d("RACEOBJ :","Object contents: " + raceObj);
                amItem = (new Gson()).fromJson(raceObj, RaceVersionQuery.GetRacesByVersion.class);
                Log.d("RACEOBJ :","Object contents: " + amItem);
            } else if (bundle != null && bundle.containsKey(ARG_CLASS_MAP) && bundle.containsKey(ARG_CLASS_MAP_ID)){
                HashMap<String, String> classMap = (HashMap<String, String>)bundle.getSerializable(ARG_CLASS_MAP);
                String classObj = "";
                if(classMap != null){
                   classObj = classMap.get(bundle.getString(ARG_CLASS_MAP_ID));
                }
                Log.d("CLASSOBJ :","Object contents: " + classObj);
                mItem = (new Gson()).fromJson(classObj, ClassVersionQuery.GetClassesByVersion.class);
            }
            //amItem = DummyContent.RACE_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                if(showRace){
                    appBarLayout.setTitle(amItem.fragments().raceData().name());
                }
                else{
                    appBarLayout.setTitle(mItem.fragments().classData().name());
                }

            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        HashMap<String, Float> map = new HashMap<>();
        if(!showRace) {
            List<ClassData.Modifier> bonusList = mItem.fragments().classData().modifiers();
            if (bonusList != null) {
                for (int i = 0; i < bonusList.size(); i++) {
                    ClassData.Modifier listItem = bonusList.get(i);
                    map.put(listItem.key(), (float)listItem.value());
                }
            }
        }else if(showRace){
            List<RaceData.Modifier> bonusList = amItem.fragments().raceData().modifiers();
            if (bonusList != null) {
                for (int i = 0; i < bonusList.size(); i++) {
                    RaceData.Modifier listItem = bonusList.get(i);
                    map.put(listItem.key(), (float)listItem.value());
                }
            }
        }

        /*map.put("ac", 2f); //REPLACE THESE WITH MOD DATA FROM THE GRAPHQL CONTENT
        map.put("wis", 2f);
        map.put("con", -2f);*/
        Fragment childFragment = ModifierFragment.Companion.newInstance(2, map);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.modifier_frag_container, childFragment).commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.classrace_detail, container, false);
        //print descriptions of the selected race or class
        if (!showRace && mItem != null) {
            ((TextView) rootView.findViewById(R.id.classrace_detail)).setText(mItem.fragments().classData().description());

        }else if (showRace && amItem != null){
            ((TextView) rootView.findViewById(R.id.classrace_detail)).setText(amItem.fragments().raceData().description());
        }

        return rootView;
    }
}
