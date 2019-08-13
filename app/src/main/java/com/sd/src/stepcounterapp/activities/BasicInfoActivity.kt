package com.sd.src.stepcounterapp.activities

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sd.src.stepcounterapp.AppApplication
import com.sd.src.stepcounterapp.R
import com.sd.src.stepcounterapp.utils.SharedPreferencesManager
import com.sd.src.stepcounterapp.viewModels.BaseViewModelFactory
import com.sd.src.stepcounterapp.viewModels.SignInViewModel
import com.squareup.picasso.Picasso
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import kotlinx.android.synthetic.main.activity_basic_info.*
import okhttp3.MediaType
import okhttp3.MultipartBody.Part
import okhttp3.MultipartBody.Part.createFormData
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BasicInfoActivity : BaseActivity<SignInViewModel>(), DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, month)
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateLabel()
    }

    private fun updateLabel() {
        var myFormat = "dd/MM/yyyy" //In which you need put here
        var sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        dobTxt.setText(sdf.format(mCalendar.time))
    }

    private var mCalendar = Calendar.getInstance()
    var selectedImage: Uri? = null
    private lateinit var fileName: String
    private var imageFileBody: Part? = null
    private var uri: Uri? = null

    override val layoutId: Int
        get() = R.layout.activity_basic_info

    override val viewModel: SignInViewModel
        get() = ViewModelProviders.of(
            this,
            BaseViewModelFactory { SignInViewModel(application) })
            .get(SignInViewModel::class.java)

    override val context: Context
        get() = this@BasicInfoActivity
    private val REQUEST_GALLERY_IMAGE: Int = 111
    private val REQUEST_IMAGE_CAPTURE: Int = 112
    private val REQUEST_CROP_IMAGE: Int = 113

    override fun onCreate() {
        viewModel.getBasicResponse()
            .observe(this, androidx.lifecycle.Observer { mUser ->
                if (mUser.status == 200) {

                    showPopupProgressSpinner(false)
//                    Toast.makeText(this@BasicInfoActivity, "Details saved successfully", Toast.LENGTH_LONG).show()

                    if (AppApplication.hasNetwork()) {
                        // add another part within the multipart request
                        if (selectedImage != null) {
                            showPopupProgressSpinner(true)
                            var requestUserID: RequestBody =
                                RequestBody.create(
                                    MediaType.parse("multipart/form-data"),
                                    SharedPreferencesManager.getUserId(this@BasicInfoActivity)!!
                                )
                            mViewModel!!.uploadImage(requestUserID, getFiletoServer())
                        } else {
                            val intent = Intent(mContext, BmiCalculatorActivity::class.java)
                            intent.putExtra("firstname",mobileTxt.text.toString())
                            intent.putExtra("lastname",pwdTxt.text.toString())
                            intent.putExtra("dob",dobTxt.text.toString())
                            intent.putExtra("username",emailTxt.text.toString())


//                                        val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
                            startActivity(intent)
                            finish()
                        }

                    } else {
                        Toast.makeText(this@BasicInfoActivity, "Internet connection unavailable", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    showPopupProgressSpinner(false)
                    Toast.makeText(this@BasicInfoActivity, "Username Exists", Toast.LENGTH_LONG).show()
                }
            })

        viewModel.getImageResponse()
            .observe(this, androidx.lifecycle.Observer { mImg ->
                if (mImg.status == 200) {

                    showPopupProgressSpinner(false)
//                    Toast.makeText(this@BasicInfoActivity, "Details saved successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(mContext, BmiCalculatorActivity::class.java)
                    intent.putExtra("email",emailTxt.text.toString())
                    intent.putExtra("firstname",mobileTxt.text.toString())
                    intent.putExtra("lastname",pwdTxt.text.toString())
                    intent.putExtra("dob",dobTxt.text.toString())
//                                        val options = ActivityOptions.makeSceneTransitionAnimation(this@SignInActivity)
                    startActivity(intent)
                    finish()
                } else {
                }
            })
    }

    override fun initListeners() {
        if(SharedPreferencesManager.hasKey(this@BasicInfoActivity,"User")){
            var user = SharedPreferencesManager.getUserObject(this@BasicInfoActivity)
            emailTxt.setText(user.data.username)
            mobileTxt.setText(user.data.firstName)
            pwdTxt.setText(user.data.lastName)
            dobTxt.setText(user.data.dob)
        }

        profileImg.setOnClickListener {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions(this@BasicInfoActivity)
                        } else {
                            Toast.makeText(
                                this@BasicInfoActivity,
                                "Please allow these permissions from settings to use this feature",
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

        saveBttn.setOnClickListener {
            if (validate()) {
                if (AppApplication.hasNetwork()) {
                    showPopupProgressSpinner(true)
                    mViewModel!!.checkAvailability(
                        emailTxt.text.toString()
                    )

                } else {
                    Toast.makeText(this@BasicInfoActivity, "Internet connection unavailable", Toast.LENGTH_LONG).show()
                }


            }

        }
        dobTxt.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val DRAWABLE_RIGHT = 2

                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= dobTxt.right - dobTxt.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        DatePickerDialog(
                            this@BasicInfoActivity, this@BasicInfoActivity, mCalendar
                                .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                            mCalendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                        return true
                    }
                }
                return false
            }
        })
        emailTxt.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val DRAWABLE_RIGHT = 2

                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= dobTxt.right - dobTxt.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        Toast.makeText(this@BasicInfoActivity, "Please select a unique username", Toast.LENGTH_LONG)
                            .show()
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun validate(): Boolean {
        if (!emailTxt.nonEmpty()) {
            Toast.makeText(
                this@BasicInfoActivity,
                resources.getString(com.sd.src.stepcounterapp.R.string.usn_error),
                Toast.LENGTH_LONG
            ).show()
            return false
        }/* else if (!mobileTxt.nonEmpty()) {
            Toast.makeText(this@BasicInfoActivity, resources.getString(R.string.first_name_error), Toast.LENGTH_LONG).show()
            return false
        }else if (!pwdTxt.nonEmpty()) {
            Toast.makeText(this@BasicInfoActivity, resources.getString(R.string.lst_name_error), Toast.LENGTH_LONG).show()
            return false
        }else if (!dobTxt.nonEmpty()) {
            Toast.makeText(this@BasicInfoActivity, resources.getString(R.string.dob_error), Toast.LENGTH_LONG).show()
            return false
        }*/
        return true
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
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        fileName = System.currentTimeMillis().toString() + ".jpg"
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName))
                        if (takePictureIntent.resolveActivity(packageManager) != null) {
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
        Dexter.withActivity(this)
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

                 var bundle:Bundle = data.getExtras()

                 var bitmap:Bitmap = bundle.getParcelable("data")

                   profileImg.setImageBitmap(bitmap)

            }
            } else {
                setResultCancelled()
            }
        }
    }

    private fun showImage(cacheImagePath: Uri) {
        Picasso.get().load(getRealPathFromURI(cacheImagePath)).into(profileImg)
    }


    private fun setResultCancelled() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }


    fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }


    fun getFiletoServer(): Part {

        // add another part within the multipart request
        if (selectedImage != null) {
            var file = File(getRealPathFromURI(selectedImage!!))
            var requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            imageFileBody = createFormData("image", file.name, requestBody)
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
}

