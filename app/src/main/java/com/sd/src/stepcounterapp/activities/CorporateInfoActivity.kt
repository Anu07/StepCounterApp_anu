package com.sd.src.stepcounterapp.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.dialog.BMIResultDialog
import com.sd.src.stepcounterapp.interfaces.InterfacesCall
import com.sd.src.stepcounterapp.model.OptionsModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModel
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import kotlinx.android.synthetic.main.activity_corporate_info.*
import java.util.*

class CorporateInfoActivity : BaseActivity<BaseViewModel>(), View.OnClickListener {

    override val layoutId: Int
        get() = R.layout.activity_corporate_info

    override val viewModel: BaseViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { BaseViewModel(application) }).get(BaseViewModel::class.java)

    override val context: Context
        get() = this@CorporateInfoActivity

    var mDepartmentArray = ArrayList<OptionsModel>()

    override fun onCreate() {
        mDepartmentArray.add(OptionsModel(0, "Department_1", false))
        mDepartmentArray.add(OptionsModel(1, "Department_2", false))
        mDepartmentArray.add(OptionsModel(2, "Department_3", false))
        mDepartmentArray.add(OptionsModel(3, "Department_4", false))
    }

    override fun initListeners() {
        llDepartment.setOnClickListener(this@CorporateInfoActivity)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llDepartment -> {
                openDepartment()
            }
            R.id.saveBttn -> {

            }
        }
    }

    private fun openDepartment() {
        val dialog =
            BMIResultDialog(mContext, R.style.pullBottomfromTop,R.layout.dialog_options,mDepartmentArray, Bundle(),  "SELECT DEPARTMENT",
                InterfacesCall.Callback { pos -> txtDepartment.text = mDepartmentArray[pos].name })
        dialog.show()
    }

}
