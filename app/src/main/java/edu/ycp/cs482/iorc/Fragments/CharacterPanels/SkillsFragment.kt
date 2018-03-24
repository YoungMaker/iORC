package edu.ycp.cs482.iorc.Fragments.CharacterPanels

import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import edu.ycp.cs482.iorc.VersionSheetQuery

import edu.ycp.cs482.iorc.R
import edu.ycp.cs482.iorc.ViewAdapters.MySkillRecyclerViewAdapter
import edu.ycp.cs482.iorc.fragment.VersionSheetData
import java.util.HashMap
//import javax.swing.UIManager.put



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
    private var mSkillList : HashMap<String, String>? = hashMapOf()
    //private var mListener: OnListFragmentInteractionListener? = null
    private val V_DATA = "VERSION_DATA"
    //private var mItem: VersionSheetQuery.GetVersionSheet? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nameDescription = HashMap<String, String>()
        val bundle = arguments
        val skillMap = bundle.getSerializable(V_DATA) as HashMap<String, String>
        //mItem = Gson().fromJson(skillMap.get(V_DATA), VersionSheetQuery.GetVersionSheet::class.java)

//        val size = mItem!!.fragments().versionSheetData().stats()!!.size
//
//        for (i in 0 until size) {
//            nameDescription.put(mItem!!.fragments().versionSheetData().stats()!!.get(i).name(),
//                    mItem!!.fragments().versionSheetData().stats()!!.get(i).description())
//        }
//
//        //val listItems = ArrayList<HashMap<String, String>>()
//
//        val it = nameDescription.iterator()
//        while (it.hasNext()) {
//            val resultMap = HashMap<String, String>()
//            val pair = it.next()
//            resultMap.put("Name", pair.key)
//            resultMap.put("Description", pair.value)
//            mSkillList!!.add(resultMap)
//        }



        Log.d("SERIALIZABLE", arguments.getSerializable(V_DATA).toString())
        if (arguments.getSerializable(V_DATA) != null) {
            mSkillList = arguments.getSerializable((V_DATA)) as HashMap<String, String>
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
                view.adapter = MySkillRecyclerViewAdapter(convertBackToSkills(mSkillList!!))
            }
        }
        return view
    }

    fun convertBackToSkills(skill_list: HashMap<String, String>): List<Stats>{
        val outputList = mutableListOf<Stats>()
        for((name, description) in skill_list) {
            outputList.add(Stats(name, description))
        }
        return outputList
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
        fun newInstance(skillsMap : HashMap<String, String>): SkillsFragment {
            val fragment = SkillsFragment()
            val args = Bundle()
            args.putSerializable(SKILLS_LIST, skillsMap)
            fragment.arguments = args
            return fragment
        }
    }

    data class Stats(
            val name: String,
            val description: String
    )
}
