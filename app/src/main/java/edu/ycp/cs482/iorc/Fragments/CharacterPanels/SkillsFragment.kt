package edu.ycp.cs482.iorc.Fragments.CharacterPanels

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import edu.ycp.cs482.iorc.VersionSheetQuery

import edu.ycp.cs482.iorc.R
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.dummy.DummyContent
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.dummy.DummyContent.DummyItem
import edu.ycp.cs482.iorc.ViewAdapters.MySkillRecyclerViewAdapter
import edu.ycp.cs482.iorc.fragment.VersionSheetData
import java.util.HashMap

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class SkillsFragment : Fragment() {
    // TODO: Customize parameters
    private var mSkillList : ArrayList<VersionSheetData.Stat>? = null
    //private var mListener: OnListFragmentInteractionListener? = null
    private val V_DATA = "VERSION_DATA"
    private var mItem: VersionSheetQuery.GetVersionSheet? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        val skillMap = bundle.getSerializable(V_DATA) as HashMap<String, String>
        mItem = Gson().fromJson(skillMap.get(V_DATA), VersionSheetQuery.GetVersionSheet::class.java)


        Log.d("SERIALIZABLE", arguments.getSerializable(V_DATA).toString())
        if (arguments.getSerializable(V_DATA) != null) {
            mSkillList = mItem as ArrayList<VersionSheetData.Stat>?
            Log.d("mSKILL_LIST", mSkillList.toString())
        }
        else{
            Log.d("SKILLS_LIST", "NULL")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_skill_list, container, false)



        // Set the adapter
        if (view is RecyclerView) {
            Log.d("SKILL_LIST_CHECK", mSkillList.toString())
            if(mSkillList != null){
                view.adapter = MySkillRecyclerViewAdapter(mSkillList)
            }
        }
        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
//        if (context is OnListFragmentInteractionListener) {
//            mListener = context
//        } else {
//            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
        //mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: VersionSheetData.Stat)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val SKILLS_LIST = "skills_list"

        // TODO: Customize parameter initialization
        fun newInstance(skills : ArrayList<VersionSheetData.Stat>): SkillsFragment {
            val fragment = SkillsFragment()
            val args = Bundle()
            args.putSerializable(SKILLS_LIST, skills)
            fragment.arguments = args
            return fragment
        }
    }
}
