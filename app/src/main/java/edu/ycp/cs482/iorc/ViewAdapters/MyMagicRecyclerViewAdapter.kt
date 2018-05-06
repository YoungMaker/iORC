package edu.ycp.cs482.iorc.ViewAdapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import edu.ycp.cs482.iorc.Fragments.CharacterPanels.MagicFragment.OnListFragmentInteractionListener
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.dummy.DummyContent.DummyItem
import edu.ycp.cs482.iorc.R
import edu.ycp.cs482.iorc.fragment.ItemData
import org.w3c.dom.Text

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMagicRecyclerViewAdapter(private val mValues: List<ItemData>?, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<MyMagicRecyclerViewAdapter.ViewHolder>() {

    private val MIN_CMP_FLOAT = 0.001

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(
                        R.layout.equipment_list_content,
                        parent,
                        false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(mValues != null) {
            holder.mItem = mValues[position]
            holder.mIdView.text = mValues[position].name()
            //TODO: Show modifiers
            var output = ""
            for( modifier in mValues[position].modifiers()!!) {
                output += if(modifier.value() % 1 > 0 && modifier.key() != "*"){
                    "%d d %d".format(modifier.value().toInt(), getDiceValue(modifier.value().toFloat())) + " " + modifier.key() + " "
                }else if (modifier.value() < 0) {
                    "%.0f".format(modifier.value()) + " " + modifier.key() + " "
                }else {
                    "+ %.0f".format(modifier.value()) + " " + modifier.key() + " "
                }
            }
            holder.mContentView.text = output

            holder.mView.setOnClickListener {
                mListener?.onListFragmentInteraction(holder.mItem!!)
            }
        }
    }

    private fun getDiceValue(modVal: Float): Int{
        val diceValue = (modVal %1)
        val outDice : Int
        outDice = if((diceValue-0.2f) < MIN_CMP_FLOAT ) {
            Math.round(diceValue * 100)
        } else {
            Math.round(diceValue * 10)
        }
        return outDice
    }

    override fun getItemCount(): Int {
        if (mValues != null){
            return mValues.size
        }
        return 0

    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.findViewById(R.id.id_text)
        val mContentView: TextView = mView.findViewById(R.id.item_mods)
        var mItem: ItemData? = null

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
