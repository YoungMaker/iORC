package edu.ycp.cs482.iorc

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import edu.ycp.cs482.iorc.ModifierFragment.OnListFragmentInteractionListener
import edu.ycp.cs482.iorc.dummy.DummyContent.DummyItem
import edu.ycp.cs482.iorc.fragment.ClassData

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Is this gonna work for other Modifiers?
 */
class MyModifierRecyclerViewAdapter(private val mValues: List<ModifierFragment.Modifier>, private val mGreen: Int)
                                        : RecyclerView.Adapter<MyModifierRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_modifier, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]

        holder.mContentView.text = mValues[position].key

        val cItemValue = mValues[position].value
        if(cItemValue > 0 && mValues[position].key != "*") {
            holder.mIdView.setTextColor(mGreen)
            holder.mIdView.text = ("+ %.0f".format(cItemValue)) //todo: make this str object? its just a plus
        }
        else if(cItemValue < 0 && mValues[position].key != "*"){
            holder.mIdView.setTextColor(Color.RED)
            holder.mIdView.text = ("  %.0f".format(cItemValue))
        }
        else{
            holder.mIdView.text = cItemValue.toString()
        }

        holder.mView.setOnClickListener {
            //mListener?.onListFragmentInteraction(holder.mItem!!)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.findViewById<View>(R.id.value) as TextView
        val mContentView: TextView = mView.findViewById<View>(R.id.name) as TextView
        var mItem: ModifierFragment.Modifier? = null

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + " " + mIdView.text + "'"
        }
    }
}
