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
 * A fragment representing a single Character detail screen.
 * This fragment is either contained in a {@link CharacterListActivity}
 * in two-pane mode (on tablets) or a {@link CharacterDetailActivity}
 * on handsets.
 */
public class CharacterDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyCharacter mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharacterDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.CHARACTER_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.character_detail, container, false);

        CharacterView mCharView = new CharacterView(rootView);
        mCharView.updateCharView(mItem);
        return rootView;
    }


    private class CharacterView {

//        private TextView mCharacterDetailInitiative;
//        private TextView mCharacterDetailHp;
        private TextView mCharacterDetailFort;
        private TextView mCharacterDetailRef;
       // private TextView mCharacterDetailSpd;
        private TextView mCharacterAbilStr;
        private TextView mCharacterAbilCon;
        private TextView mCharacterAbilDex;
        private TextView mCharacterAbilInt;
        private TextView mCharacterAbilWis;
        private TextView mCharacterAbilCha;


        private CharacterView(View rootView){
//            mCharacterDetailInitiative = (TextView)rootView.findViewById(R.id.character_detail_initiative);
           // mCharacterDetailHp = (TextView)rootView.findViewById(R.id.character_abil_hp);
            mCharacterDetailFort = (TextView)rootView.findViewById(R.id.character_abil_fort);
            mCharacterDetailRef = (TextView)rootView.findViewById(R.id.character_abil_ref);
           // mCharacterDetailSpd = (TextView)rootView.findViewById(R.id.character_detail_spd);
            mCharacterAbilStr = (TextView)rootView.findViewById(R.id.character_abil_str);
            mCharacterAbilCon = (TextView)rootView.findViewById(R.id.character_abil_con);
            mCharacterAbilDex = (TextView)rootView.findViewById(R.id.character_abil_dex);
            mCharacterAbilInt = (TextView)rootView.findViewById(R.id.character_abil_int);
            mCharacterAbilWis = (TextView)rootView.findViewById(R.id.character_abil_wis);
            mCharacterAbilCha = (TextView)rootView.findViewById(R.id.character_abil_cha);
        }

        private void updateCharView(DummyContent.DummyCharacter item) {
            mCharacterAbilStr.setText(getResources().getString(R.string.pref_str, item.str));
            mCharacterAbilCon.setText(getResources().getString(R.string.pref_con, item.con));
            mCharacterAbilDex.setText(getResources().getString(R.string.pref_dex, item.dex));
            mCharacterAbilInt.setText(getResources().getString(R.string.pref_int, item.intel));
            mCharacterAbilWis.setText(getResources().getString(R.string.pref_wis, item.wis));
            mCharacterAbilCha.setText(getResources().getString(R.string.pref_cha, item.cha));
            mCharacterDetailRef.setText(getResources().getString(R.string.pref_ref, item.ref));
            mCharacterDetailFort.setText(getResources().getString(R.string.pref_fort, item.fort));
           // mCharacterDetailSpd.setText(getResources().getString(R.string.pref_cha, item.sp));

        }
    }

}


