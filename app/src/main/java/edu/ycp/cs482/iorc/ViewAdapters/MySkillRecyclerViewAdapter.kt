package edu.ycp.cs482.iorc.ViewAdapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.SkillsFragment

import edu.ycp.cs482.iorc.Fragments.CharacterPanels.SkillsFragment.OnListFragmentInteractionListener
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.dummy.DummyContent.DummyItem
import edu.ycp.cs482.iorc.R
import edu.ycp.cs482.iorc.fragment.VersionSheetData

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
        holder.mIdView.text = mValues[position].name
        holder.mContentView.text = mValues[position].description

//        holder.mView.setOnClickListener {
//            mListener?.onListFragmentInteraction(holder.mItem!!)
//        }
    }

    override fun getItemCount(): Int {
        return mValues!!.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView
        val mContentView: TextView
        var mItem: SkillsFragment.Stats? = null

        init {
            mIdView = mView.findViewById(R.id.skill_name)
            mContentView = mView.findViewById(R.id.skill_description)
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
