package edu.ycp.cs482.iorc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentFragment extends Fragment {


    public EquipmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);

        String[] ownedItems = {"Torch", "Bedroll", "Rope"};

        ListView listView = view.findViewById(R.id.equipmentList);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, ownedItems);

        listView.setAdapter(listViewAdapter);

        // Inflate the layout for this fragment
        return view;
    }

}
