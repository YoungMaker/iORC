package edu.ycp.cs482.iorc.ViewAdapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.SkillsFragment

import edu.ycp.cs482.iorc.Fragments.CharacterPanels.SkillsFragment.OnListFragmentInteractionListener
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.dummy.DummyContent.DummyItem
import edu.ycp.cs482.iorc.R

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MySkillRecyclerViewAdapter(private val mValues: List<SkillsFragment.Stats>)
    : RecyclerView.Adapter<MySkillRecyclerViewAdapter.ViewHolder>() {
    //private val mListener: OnListFragmentInteractionListener?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_skill, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        Log.d("SKILL_OBJ", holder.mItem.toString())
        holder.mIdView.text = mValues[position].name

        //determine if stat should have + or -
        val statValue: String
        if(mValues[position].statVal >= 0){
            statValue = "+" + mValues[position].statVal.toString()
        }else{
            statValue = mValues[position].statVal.toString()
        }
        //set stat value as string
        holder.mSkillVal.text = statValue

        if(mValues[position].description.length <  110) {
            holder.mContentView.text = mValues[position].description
        }
        else {
            //Log.d("TEXT_OUTPUT", "\'" + mValues[position].description + "\'")
            val outputStr = mValues[position].description.subSequence(0, 110)
            holder.mContentView.text = "%s...".format(outputStr)
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.findViewById(R.id.skill_name)
        val mContentView: TextView = mView.findViewById(R.id.skill_description)
        val mSkillVal: TextView = mView.findViewById(R.id.skill_value)
        var mItem: SkillsFragment.Stats? = null

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
