package edu.ycp.cs482.iorc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MagicFragment extends Fragment {


    public MagicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_magic, container, false);

        //String[] spellItems = {"Magic Missile", "Lance of Faith", "Sacred Flame"};

        //ListView listView = view.findViewById(R.id.spellList);

        //ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(getActivity(),
                //android.R.layout.simple_list_item_1, spellItems);

        //listView.setAdapter(listViewAdapter);

        ListView listView = view.findViewById(R.id.spellList);

        HashMap<String, String> nameDescription = new HashMap<>();

        nameDescription.put("Magic Missile", "2 + Intelligence modifier force damage. " +
                "Add the enhancement bonus, if any, on the implement used for magic missile to" +
                " magic missile's damage.");

        nameDescription.put("Sacred Flame", "1d6 + Wisdom modifier radiant damage, and one ally you" +
               " can see chooses either to make a saving throw or to gain temporary hit points " +
               "equal to your Charisma modifier + one-half your level.");

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems, R.layout.list_spells,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.spell_name, R.id.spell_description});

        Iterator it = nameDescription.entrySet().iterator();
        while (it.hasNext()){
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultMap);
        }
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }
}
