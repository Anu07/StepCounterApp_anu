package com.sd.src.stepcounterapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.model.profile.Data
import com.sd.src.stepcounterapp.model.profile.UpdateProfileRequest
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.ProfileViewModel
import com.sd.src.stepcounterapp.viewModels.SignInViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_basic_info.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.dobTxt
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : BaseFragment(), View.OnClickListener {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.maleBttn -> {
                isGenderClicked = false
                selectGender(v)
            }
            R.id.femaleBttn -> {
                isGenderClicked = true
                selectGender(v)
            }
            R.id.profileImg -> {
                chooseImage()
            }
            R.id.saveinfoBttn -> {
                if(validateInputs()){
                    saveProfile()
                }
            }
        }
    }


    /**
     * va;idate inputs
     */

    private fun validateInputs(): Boolean {
        if(firstNameEd.text.isEmpty()){
            Toast.makeText(activity, "Firstname field can't be empty.",Toast.LENGTH_LONG).show()
            return false
        }else if(lastNameEd.text.isEmpty()){
            Toast.makeText(activity, "Lastname field can't be empty.",Toast.LENGTH_LONG).show()
            return false
        }else if(heightTxt.text.isEmpty()){
            Toast.makeText(activity, "Height field can't be empty.",Toast.LENGTH_LONG).show()
            return false
        }else if(weightTxt.text.isEmpty()){
            Toast.makeText(activity, "Weight field can't be empty.",Toast.LENGTH_LONG).show()
            return false
        }else if(mobileEd.text.isEmpty()){
            Toast.makeText(activity, "Mobile field can't be empty.",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    /**
     * Save profile details
     */

    private fun saveProfile() {
        showPopupProgressSpinner(true)
        mProfileViewModel.updateProfileData(UpdateProfileRequest(firstNameEd.text.toString(),lastNameEd.text.toString()
        ,"","Kgs",gender,"Cms",dobTxt.text.toString(),mobileEd.text.toString().toDouble(),Integer.getInteger(weightTxt.text.toString()),SharedPreferencesManager.getUserId(
                mContext),
            Integer.getInteger(heightTxt.text.toString()),SharedPreferencesManager.getUpdatedUserObject(mContext).bmi))


    }

    private fun chooseImage() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showImagePickerOptions(mContext)
                    } else {
                        Toast.makeText(
                            mContext,
                            "Please allow these permissions from settings to use mContext feature",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {

                }
            }).check()
    }

    private lateinit var mProfileViewModel: ProfileViewModel
    private var mCalendar = Calendar.getInstance()
    var selectedImage: Uri? = null
    private lateinit var fileName: String
    private var imageFileBody: MultipartBody.Part? = null
    private var uri: Uri? = null
    private var isGenderClicked: Boolean = false
    private var userData: Data? = null
    private var gender: String = ""
    private val REQUEST_GALLERY_IMAGE: Int = 111
    private val REQUEST_IMAGE_CAPTURE: Int = 112
    private val REQUEST_CROP_IMAGE: Int = 113
    private lateinit var mViewModel: SignInViewModel

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: ProfileFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): ProfileFragment {
            instance = ProfileFragment()
            mContext = context
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(SignInViewModel::class.java)
        mProfileViewModel = ViewModelProviders.of(activity!!).get(ProfileViewModel::class.java)
        userData = SharedPreferencesManager.getUpdatedUserObject(mContext)
        Picasso.get().load(userData?.image).placeholder(R.drawable.nouser).into(img_nav_header)
        firstNameEd.setText(userData!!.firstName)
        lastNameEd.setText(userData!!.firstName)
        if (SharedPreferencesManager.getUpdatedUserObject(mContext).gender.equals("Male", true)) {
            isGenderClicked = false
            selectGender(maleBttn)
        } else {
            isGenderClicked = true
            selectGender(femaleBttn)
        }
        dobTxt.setText(userData!!.dob)
        heightTxt.setText(userData!!.height)
        weightTxt.setText(userData!!.weight)
        maleBttn.setOnClickListener(this)
        femaleBttn.setOnClickListener(this)

        mobileEd.setText(userData!!.mobile.toString())
        profileImg.setOnClickListener(this)
        dobTxt.setOnTouchListener(object : View.OnTouchListener, DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                mCalendar.set(Calendar.YEAR, year)
                mCalendar.set(Calendar.MONTH, month)
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val DRAWABLE_RIGHT = 2

                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= dobTxt.right - dobTxt.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        DatePickerDialog(
                            activity, this, mCalendar
                                .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                            mCalendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                        return true
                    }
                }
                return false
            }
        })
        mViewModel.getImageResponse()
            .observe(this, androidx.lifecycle.Observer { mImg ->
                    showPopupProgressSpinner(false)
            })

        mProfileViewModel.getProfileResponse().observe(this,androidx.lifecycle.Observer {mData->
            showPopupProgressSpinner(false)
            if(mData.status==200){
                Toast.makeText(activity, "Profile updated successfully",Toast.LENGTH_LONG).show()
            }
        })

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

    fun showImagePickerOptions(context: Context) {
        // setup the alert builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.lbl_set_profile_photo))

        // add a list
        val animals = arrayOf(
            context.getString(R.string.lbl_take_camera_picture),
            context.getString(R.string.lbl_choose_from_gallery)
        )
        builder.setItems(animals) { dialog, which ->
            when (which) {
                0 -> takeCameraImage()
                1 -> chooseImageFromGallery()
            }
        }

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun takeCameraImage() {
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        fileName = System.currentTimeMillis().toString() + ".jpg"
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName))
                        if (takePictureIntent.resolveActivity(mContext.packageManager) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun chooseImageFromGallery() {
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val pickPhoto = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> if (resultCode == Activity.RESULT_OK) {
                selectedImage = data!!.data
                profileImg.setImageURI(selectedImage)
                ImageCropFunction()

            } else {
                setResultCancelled()
            }
            REQUEST_GALLERY_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                selectedImage = data!!.data
//                profileImg.setImageURI(selectedImage)
                ImageCropFunction()
            } else {
                setResultCancelled()
            }
            REQUEST_CROP_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    var bundle: Bundle = data.getExtras()

                    var bitmap: Bitmap = bundle.getParcelable("data")

                    profileImg.setImageBitmap(bitmap)
                    uploadImageToServer()
                }
            } else {
                setResultCancelled()
            }
        }
    }

    private fun uploadImageToServer() {
        var requestUserID: RequestBody =
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                SharedPreferencesManager.getUserId(mContext)!!
            )
        showPopupProgressSpinner(true)
        mViewModel!!.uploadImage(requestUserID, getFiletoServer())
    }


    private fun setResultCancelled() {
        val intent = Intent()
        activity?.setResult(Activity.RESULT_CANCELED, intent)
        activity?.finish()
    }


    fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = activity?.managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(column_index!!)!!
    }

    /**
     * request body for image
     */


    fun getFiletoServer(): MultipartBody.Part {

        // add another part within the multipart request
        if (selectedImage != null) {
            var file = File(getRealPathFromURI(selectedImage!!))
            var requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            imageFileBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
        }
        return imageFileBody!!
    }


    fun ImageCropFunction() {

        // Image Crop Code
        try {
            var cropIntent = Intent("com.android.camera.action.CROP")

            cropIntent.setDataAndType(selectedImage, "image/*")

            cropIntent.putExtra("crop", "true")
            cropIntent.putExtra("outputX", 180)
            cropIntent.putExtra("outputY", 180)
            cropIntent.putExtra("aspectX", 3)
            cropIntent.putExtra("aspectY", 4)
            cropIntent.putExtra("scaleUpIfNeeded", true)
            cropIntent.putExtra("return-data", true)

            startActivityForResult(cropIntent, REQUEST_CROP_IMAGE)

        } catch (e: ActivityNotFoundException) {
            Log.i("Cropping error", "" + e.localizedMessage)
        }
    }


    private fun updateLabel() {
        var myFormat = "dd/MM/yyyy" //In which you need put here
        var sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        dobTxt.setText(sdf.format(mCalendar.time))
    }
}
