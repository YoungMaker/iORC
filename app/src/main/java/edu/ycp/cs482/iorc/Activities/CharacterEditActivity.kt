package edu.ycp.cs482.iorc.Activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.PendingIntent.getActivity
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import edu.ycp.cs482.iorc.R

import kotlinx.android.synthetic.main.activity_character_edit.*
import android.text.Spanned
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import edu.ycp.cs482.iorc.Fragments.MasterFlows.CharacterDetailFragment
import edu.ycp.cs482.iorc.fragment.CharacterData


class CharacterEditActivity : AppCompatActivity() {

    private lateinit var mCharName: EditText
    private lateinit var mStrEdit: EditText
    private lateinit var mConEdit: EditText
    private lateinit var mDexEdit: EditText
    private lateinit var mIntEdit: EditText
    private lateinit var mWisEdit: EditText
    private lateinit var mChaEdit: EditText
    private lateinit var mProgressView: ProgressBar
    private lateinit var mUpdateFormView: LinearLayout

    private lateinit var mCharacterData: CharacterData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_edit)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        mCharName = findViewById(R.id.char_name_edit)

        mStrEdit = findViewById(R.id.character_abil_str_edit)
        mConEdit = findViewById(R.id.character_abil_con_edit)
        mDexEdit = findViewById(R.id.character_abil_dex_edit)
        mIntEdit = findViewById(R.id.character_abil_int_edit)
        mWisEdit = findViewById(R.id.character_abil_wis_edit)
        mChaEdit = findViewById(R.id.character_abil_cha_edit)

        mProgressView = findViewById(R.id.update_char_progress)
        mUpdateFormView = findViewById(R.id.char_update_form)

        mStrEdit.filters = arrayOf(InputFilterMinMax("1", "500"))
        mConEdit.filters = arrayOf(InputFilterMinMax("1", "500"))
        mDexEdit.filters = arrayOf(InputFilterMinMax("1", "500"))
        mIntEdit.filters = arrayOf(InputFilterMinMax("1", "500"))
        mWisEdit.filters = arrayOf(InputFilterMinMax("1", "500"))
        mChaEdit.filters = arrayOf(InputFilterMinMax("1", "500"))

        val extra = intent
        if (extra != null) {
            //get characterdata from list
            if (extra.getSerializableExtra(CharacterDetailFragment.ARG_ITEM_ID) != null) {
                //get character data to be used in fragments
                val serializedCharData = extra
                        .getSerializableExtra(CharacterDetailFragment.ARG_ITEM_ID) as String
                mCharacterData = deserializeCharData(serializedCharData)
                populateCharData()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun populateCharData(){
        mCharName.setText(mCharacterData.name(), TextView.BufferType.EDITABLE)

        mStrEdit.setText(mCharacterData.abilityPoints().str().toString(), TextView.BufferType.EDITABLE)
        mConEdit.setText(mCharacterData.abilityPoints().con().toString(), TextView.BufferType.EDITABLE)
        mDexEdit.setText(mCharacterData.abilityPoints().dex().toString(), TextView.BufferType.EDITABLE)
        mIntEdit.setText(mCharacterData.abilityPoints().int_().toString(), TextView.BufferType.EDITABLE)
        mWisEdit.setText(mCharacterData.abilityPoints().wis().toString(), TextView.BufferType.EDITABLE)
        mChaEdit.setText(mCharacterData.abilityPoints().cha().toString(), TextView.BufferType.EDITABLE)
    }

    //deserialize character data
    private fun deserializeCharData(serialData: String): CharacterData {
        return Gson().fromJson(serialData, CharacterData::class.java)
    }

    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

            mUpdateFormView.visibility = if (show) View.GONE else View.VISIBLE
            mUpdateFormView.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mUpdateFormView.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

            mProgressView.visibility = if (show) View.VISIBLE else View.GONE
            mProgressView.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mProgressView.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.visibility = if (show) View.VISIBLE else View.GONE
            mUpdateFormView.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}


class InputFilterMinMax : InputFilter {
    private var min: Int = 0
    private var max: Int = 0

    constructor(min: Int, max: Int) {
        this.min = min
        this.max = max
    }

    constructor(min: String, max: String) {
        this.min = Integer.parseInt(min)
        this.max = Integer.parseInt(max)
    }

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (isInRange(min, max, input))
                return null
        } catch (nfe: NumberFormatException) {
        }

        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c >= a && c <= b else c >= b && c <= a
    }
}