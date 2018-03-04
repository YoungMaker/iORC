package edu.ycp.cs482.iorc

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import edu.ycp.cs482.iorc.dummy.DummyContent
import edu.ycp.cs482.iorc.dummy.DummyContent.DummyItem
import edu.ycp.cs482.iorc.fragment.ClassData

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
class ModifierFragment : Fragment() {
    // TODO: Customize parameters
    private var mColumnCount = 2
    private var mModMap: HashMap<String, Float> = hashMapOf()
    //private var mListener: OnListFragmentInteractionListener? = null

    @SuppressWarnings("unchecked")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)

            mModMap = arguments.getSerializable(ARG_HASH_MAP) as HashMap<String, Float>

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_modifier_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            view.adapter = MyModifierRecyclerViewAdapter(convertBackToModifiers(mModMap), ContextCompat.getColor(context, R.color.OkGreen))
        }
        return view
    }


    fun convertBackToModifiers(mods: HashMap<String, Float>): List<Modifier>{
        val outputList = mutableListOf<Modifier>()
        for((key, value) in mods) {
            outputList.add(Modifier(key, value))
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
        fun onListFragmentInteraction(item: Modifier)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"
        private val ARG_HASH_MAP = "mod-map"


        fun newInstance(columnCount: Int, modMap: HashMap<String, Float>): ModifierFragment {
            val fragment = ModifierFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            args.putSerializable(ARG_HASH_MAP, modMap)
            fragment.arguments = args
            return fragment
        }
    }

    data class Modifier(
            val key: String,
            val value: Float
            )
}
