package edu.ycp.cs482.iorc;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import edu.ycp.cs482.iorc.dummy.DummyContent;
import edu.ycp.cs482.iorc.fragment.ItemData;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM = "item";

    /**
     * The dummy content this fragment is presenting.
     */
    private ItemData mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem =  (new Gson()).fromJson( getArguments().getString(ARG_ITEM), ItemData.class);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name());
            }
        }
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        HashMap<String, Float> map = new HashMap<>();

        List<ItemData.Modifier> bonusList = mItem.modifiers();
        if (bonusList != null) {
            for (int i = 0; i < bonusList.size(); i++) {
                ItemData.Modifier listItem = bonusList.get(i);
                map.put(listItem.key(), (float) listItem.value());
            }
        }
//        map.put("ac", 2f);
//        map.put("wis", 2f);
//        map.put("con", -2f);
        Fragment childFragment = ModifierFragment.Companion.newInstance(2, map);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.item_modifier_frag_container, childFragment).commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.description());
            ((TextView) rootView.findViewById(R.id.item_price)).setText(mItem.price());
        }
        StringBuilder output = new StringBuilder();
        for(String tag : mItem.itemClasses()){
            output.append(tag).append(", ");
        }
        ((TextView) rootView.findViewById(R.id.item_tags)).setText(output);
        return rootView;
    }
}
