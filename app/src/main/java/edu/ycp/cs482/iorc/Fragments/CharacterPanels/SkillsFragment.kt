package edu.ycp.cs482.iorc.Fragments.CharacterPanels

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import edu.ycp.cs482.iorc.R
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
    private var mSkillMap : HashMap<String, Double>? = null
    private var mSkillList: MutableList<VersionSheetData.Stat>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_skill_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            Log.d("SKILL_LIST_CHECK", mSkillList.toString())
            if(mSkillList != null && mSkillMap != null){
                view.adapter = MySkillRecyclerViewAdapter(convertBackToSkills())
            }
                val itemDecor = DividerItemDecoration(view.context,
                        DividerItemDecoration.VERTICAL) //this should probably get the layoutManager's preference.
                view.addItemDecoration(itemDecor)

        }
        return view
    }

    fun convertBackToSkills(): List<Stats>{
        val outputList = mutableListOf<Stats>()
        //create skills list
        for(skill in mSkillList!!) {
            //add value to the list
            val skillName = skill.name()
            val skillDesc = skill.description()
            var skillVal = 0
            //check for key inside map first
            if(mSkillMap!!.contains(skillName.toLowerCase())){
                skillVal = mSkillMap!!.get(skill.name().toLowerCase())!!.toInt()
            }
            outputList.add(Stats(skillName, skillDesc, skillVal))
        }
        return outputList
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
            val description: String,
            val statVal: Int
    )

    fun assignSkillList(skillList: MutableList<VersionSheetData.Stat>){
        mSkillList = skillList
    }

    fun assignSkillMap(skillMap: HashMap<String, Double>){
        mSkillMap = hashMapOf()
        for((key, value) in skillMap){
            //Log.d("KEY", key)
            //Log.d("VALUE", value.toString())
            mSkillMap!!.put(key, value)
        }
    }
}
