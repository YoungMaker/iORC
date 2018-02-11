package edu.ycp.cs482.iorc;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.ycp.cs482.iorc.dummy.DummyContent;

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
    public static final String ARG_SHOW_ITEM = "SHOW_RELIGION";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyAlignment mItem;
    private DummyContent.DummyReligion amItem;

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
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ALIGNMENT_MAP.get(getArguments().getString(ARG_ITEM_ID));
            amItem = DummyContent.RELIGION_MAP.get((getArguments().getString(ARG_ITEM_ID)));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                if(showReligion){
                    appBarLayout.setTitle(amItem.name);
                }else if(!showReligion){
                    appBarLayout.setTitle(mItem.name);
                }

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alignmentreligion_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (!showReligion && mItem != null) {
            ((TextView) rootView.findViewById(R.id.alignmentreligion_detail)).setText(mItem.content);
        } else if (showReligion && amItem != null){
            ((TextView) rootView.findViewById(R.id.alignmentreligion_detail)).setText(amItem.content);
        }
        return rootView;
    }
}
