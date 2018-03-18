package edu.ycp.cs482.iorc


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.*
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton


/**
 * A simple [Fragment] subclass.
 * Use the [slots.newInstance] factory method to
 * create an instance of this fragment.
 */
 class SlotsFragment :Fragment(), View.OnClickListener {


    // TODO: Rename and change types of parameters
    private var mParam1:String? = null
    private var mParam2:String? = null

    private var hand_button_l: ImageButton? = null
    private var head_button: ImageButton? = null
    private var chest_button: ImageButton? = null
    private var wasit_button: ImageButton? = null
    private var hand_button_r: ImageButton? = null
    private var arm_button_l: ImageButton? = null
    private var arm_button_r: ImageButton? = null
    private var feet_button: ImageButton? = null

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
        {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?,
                                                savedInstanceState:Bundle?):View? {
     // Inflate the layout for this fragment
           val view =  inflater!!.inflate(R.layout.fragment_slots, container, false)

        hand_button_l = view.findViewById(R.id.hand_button_l)
        hand_button_l!!.setOnClickListener(this)
        hand_button_r = view.findViewById(R.id.hand_button_r)
        arm_button_l = view.findViewById(R.id.arm_button_l)
        arm_button_r = view.findViewById(R.id.arm_button_r)
        head_button = view.findViewById(R.id.head_button)
        wasit_button = view.findViewById(R.id.waist_button)
        chest_button = view.findViewById(R.id.chest_button)
        feet_button = view.findViewById(R.id.feet_button)




        return view
    }

    override fun onClick(view: View?) {
        if(view!! == hand_button_l){
            make(view, "ya clicked his head!", LENGTH_LONG)
                        .setAction("Action", null).show()
        }
    }

    companion object {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

    /**
    *  Use this factory method to create a new instance of
    * this fragment using the provided parameters.
     *
    * @param param1 Parameter 1.
    * @param param2 Parameter 2.
    * @return A new instance of fragment slots.
    */
    // TODO: Rename and change types and number of parameters
     fun newInstance(param1:String, param2:String): SlotsFragment {
        val fragment = SlotsFragment()
        val args = Bundle()
        args.putString(ARG_PARAM1, param1)
        args.putString(ARG_PARAM2, param2)
        fragment.arguments = args
        return fragment
    }
}

}// Required empty public constructor
