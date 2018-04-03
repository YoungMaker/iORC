package edu.ycp.cs482.iorc.Fragments.CharacterPanels

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import edu.ycp.cs482.iorc.CharacterVersionQuery

import edu.ycp.cs482.iorc.R
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.dummy.DummyContent
import edu.ycp.cs482.iorc.Fragments.CharacterPanels.dummy.DummyContent.DummyItem
import edu.ycp.cs482.iorc.ViewAdapters.MyItemRecyclerViewAdapter
import edu.ycp.cs482.iorc.fragment.ItemData

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
class EquipmentFragment : Fragment() {
    private var mListener: OnListFragmentInteractionListener? = null
    private var mItemsList: List<ItemData>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            val charMap = arguments.getSerializable(ARG_INV_DATA) as java.util.HashMap<String, String>
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            //TODO: put data into view adapter
            view.adapter = MyItemRecyclerViewAdapter(mItemsList, mListener)
        }
        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
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
        fun onListFragmentInteraction(item: ItemData)
    }

    companion object {

        private val ARG_INV_DATA = "ITEM_INV_DATA"

        fun newInstance(invMap: HashMap<String, String>): EquipmentFragment {
            val fragment = EquipmentFragment()
            val args = Bundle()
            args.putSerializable(ARG_INV_DATA, invMap)
            fragment.arguments = args
            return fragment
        }
    }
}
