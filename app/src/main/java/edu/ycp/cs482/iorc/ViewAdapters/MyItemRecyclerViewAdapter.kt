package edu.ycp.cs482.iorc.ViewAdapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import edu.ycp.cs482.iorc.Fragments.CharacterPanels.EquipmentFragment.OnListFragmentInteractionListener
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.dummy.DummyContent.DummyItem
import edu.ycp.cs482.iorc.R
import edu.ycp.cs482.iorc.fragment.ItemData
import org.w3c.dom.Text

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(private val mValues: List<ItemData>?, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(
                        R.layout.item_list_content,
                        parent,
                        false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(mValues != null) {
            holder.mItem = mValues[position]
            holder.mIdView.text = mValues[position].name()
            holder.mContentView.text = mValues[position].description()

            holder.mView.setOnClickListener {
                mListener?.onListFragmentInteraction(holder.mItem!!)
            }
        }
    }

    override fun getItemCount(): Int {
        if (mValues != null){
            return mValues.size
        }
        return 0

    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.findViewById(R.id.id_text)
        val mContentView: TextView = mView.findViewById(R.id.content)
        var mItem: ItemData? = null

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
