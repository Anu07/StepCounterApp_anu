package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.adapter.PickerAdapter
import com.sd.src.stepcounterapp.dialog.SelectCityDialog
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.BasicInfoRequestObject
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.SignInViewModel
import kotlinx.android.synthetic.main.activity_bmi_calc.*
import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager


class BmiCalculatorActivity : BaseActivity<SignInViewModel>(), View.OnClickListener {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.lbs_wt -> {
                isWtButtonClicked = false
                changeBttnBg(v)
                adapter.swapData(getWeightData(maxLbs))
                rv.smoothScrollToPosition(0)
            }
            R.id.kgs_wt -> {
                isWtButtonClicked = true
                changeBttnBg(v)
                adapter.swapData(getWeightData(maxKgs))
                rv.smoothScrollToPosition(0)

            }
            R.id.cms_ht -> {
                isHtButtonClicked = true
                changeHtBttnBg(v)
                adapter_ht.swapData(getHeightDataCms(maxLbs))
                rv_ht.smoothScrollToPosition(0)

            }
            R.id.fts_ht -> {
                isHtButtonClicked = false
                changeHtBttnBg(v)
                adapter_ht.swapData(getHeightData(maxHtFt))
                rv_ht.smoothScrollToPosition(0)

            }
            R.id.maleBttn -> {
                isGenderClicked = false
                selectGender(v)
            }
            R.id.femaleBttn -> {
                isGenderClicked = true
                selectGender(v)
            }

        }
    }

    private val maxCms: Int = 280
    private val maxKgs: Int = 210
    private val maxHtFt: Int = 8
    private val maxLbs: Int = 450
    private var weight: String = ""
    private var height: String = ""
    private var gender: String = ""
    var wts: Int = 0
    var hts: Int = 0
    var flooredheight: Float = 0.0f
    override val layoutId: Int
        get() = R.layout.activity_bmi_calc
    override val viewModel: SignInViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { SignInViewModel(application) }).get(SignInViewModel::class.java)

    override val context: Context
        get() = this@BmiCalculatorActivity

    var isWtButtonClicked: Boolean = false
    var isHtButtonClicked: Boolean = false
    var isGenderClicked: Boolean = false
    private lateinit var rv: RecyclerView
    private lateinit var rv_ht: RecyclerView
    lateinit var adapter: PickerAdapter
    lateinit var adapter_ht: PickerAdapter


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        rv = findViewById(R.id.rv)
        rv_ht = findViewById(R.id.rv_ht)

        val pickerLayoutManager = PickerLayoutManager(this, PickerLayoutManager.HORIZONTAL, false)
        pickerLayoutManager.isChangeAlpha = true
        pickerLayoutManager.scaleDownBy = 0.99f
        pickerLayoutManager.scaleDownDistance = 0.8f


        val pickerLayoutManager_ht = PickerLayoutManager(this, PickerLayoutManager.HORIZONTAL, false)
        pickerLayoutManager_ht.isChangeAlpha = true
        pickerLayoutManager_ht.scaleDownBy = 0.99f
        pickerLayoutManager_ht.scaleDownDistance = 0.8f


        adapter = PickerAdapter(this, getWeightData(maxKgs), rv)
        adapter_ht = PickerAdapter(this, getHeightDataCms(maxCms), rv_ht)

        kgs_wt.isSelected = true
        cms_ht.isSelected = true


        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rv as RecyclerView?)
        rv.layoutManager = pickerLayoutManager
        rv.adapter = adapter
        val snapHelper_ht = LinearSnapHelper()
        snapHelper_ht.attachToRecyclerView(rv_ht as RecyclerView?)
        rv_ht.layoutManager = pickerLayoutManager_ht
        rv_ht.adapter = adapter_ht
        pickerLayoutManager.setOnScrollStopListener { view ->
            val tv = view as TextView
            tv.setTextColor(resources.getColor(R.color.gradientColorEnd))
            weight = tv.text.toString()
        }
        pickerLayoutManager_ht.setOnScrollStopListener { view ->
            val tv = view as TextView
            tv.setTextColor(resources.getColor(R.color.gradientColorEnd))
            height = tv.text.toString()
        }

        kgs_wt.setOnClickListener(this)
        lbs_wt.setOnClickListener(this)
        fts_ht.setOnClickListener(this)
        cms_ht.setOnClickListener(this)
        maleBttn.setOnClickListener(this)
        femaleBttn.setOnClickListener(this)
        kgs_wt.performClick()
        cms_ht.performClick()

        viewModel.getBasicResponse()
            .observe(this, Observer { mUser ->
                if (mUser.status == 200) {
                    if (intent.hasExtra("inApp")) {
                        finish()
                    } else {
                        val intent = Intent(mContext, RewardsCategorySelectionActivity::class.java)
//                      val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
                        startActivity(intent)
                    }


                }
            })


        if (intent.hasExtra("inApp")) {
            skipBttn.visibility = View.GONE
            if (SharedPreferencesManager.getUpdatedUserObject(this@BmiCalculatorActivity).gender.equals("Male", true)) {
                isGenderClicked= false
                selectGender(maleBttn)
            } else {
                isGenderClicked= true
                selectGender(femaleBttn)
            }
        }

    }

    /**
     * gender selection
     */

    @RequiresApi(Build.VERSION_CODES.M)
    private fun selectGender(v: View) {
        if (v.id == R.id.maleBttn) {
            maleBttn.setBackgroundResource(if (!isGenderClicked) R.drawable.fill else R.drawable.unfill)
            femaleBttn.setBackgroundResource(R.drawable.unfill)
            maleBttn.setTextColor(resources.getColor(R.color.white))
            femaleBttn.setTextColor(resources.getColor(R.color.colorBlack))
            maleBttn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.male_w, 0, 0, 0)
            femaleBttn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.female_b, 0, 0, 0)
            gender = "male"
        } else if (v.id == R.id.femaleBttn) {
            femaleBttn.setBackgroundResource(if (isGenderClicked) R.drawable.fill else R.drawable.unfill)
            maleBttn.setBackgroundResource(R.drawable.unfill)
            maleBttn.setTextColor(resources.getColor(R.color.colorBlack))
            femaleBttn.setTextColor(resources.getColor(R.color.white))
            femaleBttn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.female, 0, 0, 0)
            maleBttn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.male, 0, 0, 0)
            gender = "female"
        }
    }

    /**
     * weight units selection
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun changeBttnBg(v: View) {
        if (v.id == R.id.kgs_wt) {
            lbs_wt.setBackgroundResource(if (isWtButtonClicked) R.drawable.unfill else R.drawable.fill)
            v.setBackgroundResource(if (isWtButtonClicked) R.drawable.fill else R.drawable.unfill)
            kgs_wt.setTextColor(resources.getColor(R.color.white))
            lbs_wt.setTextColor(resources.getColor(R.color.colorBlack))

        } else if (v.id == R.id.lbs_wt) {
            kgs_wt.setBackgroundResource(if (!isWtButtonClicked) R.drawable.unfill else R.drawable.fill)
            v.setBackgroundResource(if (!isWtButtonClicked) R.drawable.fill else R.drawable.unfill)
            kgs_wt.setTextColor(resources.getColor(R.color.colorBlack))
            lbs_wt.setTextColor(resources.getColor(R.color.white))
        }
    }

    /**
     * height units selection
     */

    @RequiresApi(Build.VERSION_CODES.M)
    private fun changeHtBttnBg(v: View) {
        if (v.id == R.id.cms_ht) {
            fts_ht.setBackgroundResource(if (isHtButtonClicked) R.drawable.unfill else R.drawable.fill)
            v.setBackgroundResource(if (isHtButtonClicked) R.drawable.fill else R.drawable.unfill)
            cms_ht.setTextColor(resources.getColor(R.color.white))
            fts_ht.setTextColor(resources.getColor(R.color.colorBlack))

        } else if (v.id == R.id.fts_ht) {
            cms_ht.setBackgroundResource(if (!isHtButtonClicked) R.drawable.unfill else R.drawable.fill)
            v.setBackgroundResource(if (!isHtButtonClicked) R.drawable.fill else R.drawable.unfill)
            fts_ht.setTextColor(resources.getColor(R.color.white))
            cms_ht.setTextColor(resources.getColor(R.color.colorBlack))

        }
    }

    override fun initListeners() {
        saveinfoBttn.setOnClickListener {

            if (validate()) {
                var bundle = Bundle()
                var w = if (isWtButtonClicked) "Kgs" else "Lbs"
                var h = if (isHtButtonClicked) "Cms" else "Feet"
                bundle.putString("Weigth", weight + w)
                bundle.putString("heigth", height + h)
                bundle.putFloat("bmi", calcBMI(weight.toInt(), height))

                val dialog =
                    SelectCityDialog(mContext,
                        R.style.pullBottomfromTop,
                        R.layout.dialog_bmi_result,
                        ArrayList(),
                        bundle,
                        "",
                        InterfacesCall.Callback {

                            if (intent.hasExtra("inApp")) {
                                mViewModel!!.addBasicInfo(
                                    BasicInfoRequestObject(
                                        SharedPreferencesManager.getUserId(this@BmiCalculatorActivity)!!,
                                        SharedPreferencesManager.getUpdatedUserObject(this@BmiCalculatorActivity).username,
                                        SharedPreferencesManager.getUpdatedUserObject(this@BmiCalculatorActivity).firstName,
                                        SharedPreferencesManager.getUpdatedUserObject(this@BmiCalculatorActivity).lastName,
                                        SharedPreferencesManager.getUpdatedUserObject(this@BmiCalculatorActivity).dob,
                                        SharedPreferencesManager.getUpdatedUserObject(this@BmiCalculatorActivity).gender,
                                        SharedPreferencesManager.getUpdatedUserObject(this@BmiCalculatorActivity).weight,
                                        SharedPreferencesManager.getUpdatedUserObject(this@BmiCalculatorActivity).weightType,
                                        SharedPreferencesManager.getUpdatedUserObject(this@BmiCalculatorActivity).height,
                                        SharedPreferencesManager.getUpdatedUserObject(this@BmiCalculatorActivity).heightType,          //TODO
                                        calcBMI(weight.toInt(), height).toDouble(),
                                        true
                                    )
                                )
                            } else {
                                mViewModel!!.addBasicInfo(
                                    BasicInfoRequestObject(
                                        SharedPreferencesManager.getUserId(this@BmiCalculatorActivity)!!,
                                        intent.getStringExtra("username"),
                                        intent.getStringExtra("firstname"),
                                        intent.getStringExtra("lastname"),
                                        intent.getStringExtra("dob"),
                                        gender,
                                        weight.toFloat(),
                                        w,
                                        flooredheight,
                                        "Cms",          //TODO
                                        calcBMI(weight.toInt(), height).toDouble(),
                                        true
                                    )
                                )
                            }


                        })
                dialog.show()
            }

        }
        skipBttn.setOnClickListener {
            mViewModel!!.addBasicInfo(
                BasicInfoRequestObject(
                    SharedPreferencesManager.getUserId(this@BmiCalculatorActivity)!!,
                    intent.getStringExtra("username"),
                    intent.getStringExtra("firstname"),
                    intent.getStringExtra("lastname"),
                    intent.getStringExtra("dob"),
                    "",
                    0.0f,
                    "",
                    0.0f,
                    "",
                    0.0,
                    true
                )
            )
            val intent = Intent(mContext, RewardsCategorySelectionActivity::class.java)
//                    val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
            startActivity(intent)
        }

    }

    private fun validate(): Boolean {
        if (gender.isEmpty()) {
            Toast.makeText(context, "Please select gender", Toast.LENGTH_LONG).show()
            return false
        } else if (weight.isEmpty()) {
            Toast.makeText(context, "Please select weight", Toast.LENGTH_LONG).show()
            return false
        } else if (height.isEmpty()) {
            Toast.makeText(context, "Please select height", Toast.LENGTH_LONG).show()
            return false
        }
        return true

    }

    fun getWeightData(count: Int): List<String> {
        val data = ArrayList<String>()
        for (i in 0 until count) {
            data.add(i.toString())
        }
        return data
    }


    /**
     * height in CMs
     */


    fun getHeightDataCms(count: Int): List<String> {
        val data = ArrayList<String>()
        for (i in 1 until count) {
            data.add(i.toString())
        }
        return data
    }

    /**
     * height in Fts
     */
    fun getHeightData(count: Int): List<String> {
        val data = ArrayList<String>()
        for (i in 1 until maxHtFt) {
            for (j in 1 until 13) {
                data.add("$i'$j\"")
            }
        }
        return data
    }


    /**
     * Kgs to Lbs
     */

    fun convertKgsToLbs(kgs: Int): Int {
        var result = kgs * 2.20
        return result.toInt()
    }


    /**
     * Cms to Feet
     */

    fun convertCmsToInch(cms: Int): Int {
        var result = cms * 0.39f
        return result.toInt()
    }


    /**
     *Feet to Inch
     */

    private fun convertFeetToInch(feet: String): Float {
        var heightSplit = feet.split("'")
        var resultft = heightSplit[0].toInt() * 12
        var resultin = heightSplit[1].replace("\"", "").toInt()
        var result = resultft + resultin
        Log.i("Height", "calculated" + result.toFloat())
        return result.toFloat()
    }

    /**
     * BMI
     *         (weight in pounds/(height in inches*height in inches))*703
     */
    fun calcBMI(wt: Int, ht: String): Float {

        if (isWtButtonClicked) {
            //lbs unselected
            wts = convertKgsToLbs(wt)
        } else {
            wts = wt
        }
        if (!isHtButtonClicked) {
            flooredheight = (String.format("%.2f", convertFeetToInch(ht))).toFloat()
        } else {
            hts = convertCmsToInch(ht.toInt())
            flooredheight = (String.format("%.2f", hts.toFloat())).toFloat()
        }

//        Formula: 703 x weight (lbs) / [height (in)]2
        Log.i("test", "calcl" + (703 * (wts / (flooredheight * flooredheight))))
        return (703 * (wts / (flooredheight * flooredheight)))
    }


    override fun onBackPressed() {
        if (intent.hasExtra("inApp")) {
            super.onBackPressed()
        }
    }
}