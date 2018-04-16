package edu.ycp.cs482.iorc.Fragments.MasterFlows;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import edu.ycp.cs482.iorc.Activities.AlignmentReligionDetailActivity;
import edu.ycp.cs482.iorc.Activities.AlignmentReligionListActivity;
import edu.ycp.cs482.iorc.R;
import edu.ycp.cs482.iorc.fragment.VersionInfoData;

/**
 * A fragment representing a single AlignmentReligion detail screen.
 * This fragment is either contained in a {@link AlignmentReligionListActivity}
 * in two-pane mode (on tablets) or a {@link AlignmentReligionDetailActivity}
 * on handsets.
 */
public class AlignmentReligionDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM = "CURRENT_ITEM";
    public static final String ARG_SHOW_ITEM = "SHOW_RELIGION";

    /**
     * The dummy content this fragment is presenting.
     */
    private VersionInfoData.InfoList mItem;

    private boolean showReligion = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlignmentReligionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey(ARG_SHOW_ITEM)) {
            showReligion = getArguments().getBoolean(ARG_SHOW_ITEM);
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Bundle extras = getArguments();
            mItem = (new Gson()).fromJson(extras.getString(ARG_ITEM), VersionInfoData.InfoList.class);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null && mItem != null) {
                appBarLayout.setTitle(mItem.name());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alignmentreligion_detail, container, false);

        if(mItem != null){
            // Show the dummy content as text in a TextView.
            ((TextView) rootView.findViewById(R.id.alignmentreligion_detail)).setText(mItem.value());
        }


        return rootView;
    }
}
