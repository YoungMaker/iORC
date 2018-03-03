package edu.ycp.cs482.iorc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SkillsFragment extends Fragment {


    public SkillsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skills, container, false);

        //String[] skillItems = {"Insight", "Religion", "Stealth"};
        ArrayList<String> skillItems = new ArrayList<>();
        skillItems.add("Insight");
        skillItems.add("Religion");
        skillItems.add("Stealth");

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, skillItems);

        ListView listView = view.findViewById(R.id.skillList);

        listView.setAdapter(listViewAdapter);

        // Inflate the layout for this fragment
        return view;
    }

}
