package edu.ycp.cs482.iorc;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentFragment extends Fragment {


    private static final String CREATION_DATA = "CREATION_DATA";
    private HashMap<String, String> creationData;

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


        //creationData = (HashMap<String, String>) extra.getSerializable(CREATION_DATA);
        FloatingActionButton fab = view.findViewById(R.id.item_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemIntent = new Intent(getActivity(), ItemListActivity.class);
                itemIntent.putExtra(CREATION_DATA, creationData);
                startActivity(itemIntent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
