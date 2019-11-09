package com.classroomjoin.app.profileEditPage

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MenuItem
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.Utility
import com.classroomjoin.app.loginPage.Userdata
import com.classroomjoin.app.teacherAttachmentPage.AttachmentResponse
import com.classroomjoin.app.teacherAttachmentPage.AttachmentViewModel
import com.vicpin.krealmextensions.createOrUpdate
import com.vicpin.krealmextensions.query
import kotlinx.android.synthetic.main.activity_profile_image_chooser.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.onClick
import rx.Observer
import java.io.*


class ProfileImageChooserActivity : LocalizationActivity(), AnkoLogger {


    private var userChoosenTask: String = "Take Photo"
    private val REQUEST_CAMERA: Int = 1234
    private val SELECT_FILE: Int = 4321
    var finalFile: File? = null
    var viewmodel: AttachmentViewModel? = null
    var imageUri: Uri? = null

    private var progressdialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_image_chooser)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.select_image))

        progressdialog = DialogUtil.showProgressDialog(this@ProfileImageChooserActivity)

        viewmodel = AttachmentViewModel(this@ProfileImageChooserActivity)

        //cameraPermission.setOnClickListener {

        // }

        fab.onClick { uploadImage() }

        select_image.onClick {
            selectImage()
        }

        image_preview.onClick {
            selectImage()
        }

        selectImage()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }


    private fun uploadImage() {
        val filePart = MultipartBody.Part.createFormData("file", finalFile!!.getName(),
                RequestBody.create(MediaType.parse("image/*"), finalFile))
        if (finalFile != null) {
            progressdialog!!.show()
            viewmodel?.uploadImage(filePart)?.subscribe(Observer)
        } else Snackbar.make(image_preview, getString(R.string.no_images), Snackbar.LENGTH_SHORT).show()
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(getString(R.string.take_photo), getString(R.string.choose_from_library), getString(R.string.btn_cancel))
        val builder = AlertDialog.Builder(this@ProfileImageChooserActivity)
        builder.setTitle(getString(R.string.app_name))
        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
            // val result = Utility.checkPermission(this@ProfileImageChooserActivity) && Utility.checkWritePermission(this@ProfileImageChooserActivity)
            if (items[item] == getString(R.string.take_photo)) {
                userChoosenTask = getString(R.string.take_photo)

                //println("Data--->"+"type-->"+userChoosenTask+"---"+result)
                // if (result)
                cameraIntent()
            } else if (items[item] == getString(R.string.choose_from_library)) {
                userChoosenTask = getString(R.string.choose_from_library)
                //println("Data--->"+"type-->"+userChoosenTask+"---"+result)

                //if (result)
                galleryIntent()
            } else if (items[item] == getString(R.string.cancel)) {
                //println("Data--->"+"type-->"+"Cancel"+"---"+result)

                dialog.dismiss()
            }
        })
        builder.show()
    }


    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_FILE)
    }

    private val Observer: Observer<AttachmentResponse>
        get() = object : Observer<AttachmentResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                progressdialog!!.dismiss()
                DialogUtil.showerrorDialog(getString(R.string.some_error), this@ProfileImageChooserActivity::uploadImage, this@ProfileImageChooserActivity)
                e?.printStackTrace()
            }

            override fun onNext(t: AttachmentResponse) {
                progressdialog!!.dismiss()
                if (t.status == "Success") {
                    viewmodel!!.profile_url = t.data
                    val data = Userdata().query { realmQuery -> realmQuery.equalTo("user_id", viewmodel!!.userid!!.toInt()) }.first()
                    data.profile_url = t.data
                    data.createOrUpdate()
                    setResult(Activity.RESULT_OK, Intent().putExtra(ProfileEditPage.PROFILE_KEY, t.data))
                    finish()
                } else {
                    DialogUtil.showerrorDialog(getString(R.string.some_error), this@ProfileImageChooserActivity::uploadImage, this@ProfileImageChooserActivity)
                }

            }
        }


    private fun cameraIntent() {



        var values = ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (userChoosenTask == getString(R.string.take_photo))
                    cameraIntent()
                else if (userChoosenTask == getString(R.string.choose_from_library))
                    galleryIntent()
            } else {
                //code for deny
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data)
            else if (requestCode == REQUEST_CAMERA) {


                onCaptureImageResult(data)
            }
        } else {
            finish()
        }
    }

    fun getPath(context: Context, uri: Uri): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

    private fun onSelectFromGalleryResult(data: Intent?) {
        var bm: Bitmap? = null
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, data.data)
                bm = Bitmap.createScaledBitmap(bm,  800, 800,true)
                image_preview.setImageBitmap(bm)
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                val tempUri = getImageUri(applicationContext, bm)

                error { tempUri.toString() }
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                finalFile = File(getRealPathFromURI(tempUri))

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun onCaptureImageResult(data: Intent?) {

        var thumbnail = MediaStore.Images.Media.getBitmap(
                getContentResolver(), imageUri)

        //val selectedImageUri = data!!.getData()
        val picturePath = getPath(getApplicationContext(), imageUri!!)
        Log.d("Picture Path", picturePath)

        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(picturePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientation = exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED)

        println("onCaptureImageResult--->"+thumbnail.width+"*"+thumbnail.height)

        //   val thumbnail = data?.extras?.get("data") as Bitmap

        var bmRotated11 = rotateBitmap(thumbnail, orientation)

        thumbnail = Bitmap.createScaledBitmap(bmRotated11,  700, 700,true)

       // var mtx =  Matrix()
        //var angle = 90
       // mtx.preRotate(angle.toFloat());
       // thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, 700, 700, mtx,
                //false)
        image_preview.setImageBitmap(thumbnail)
        //thumbnail=getCroppedBitmap(thumbnail)
        println("onCaptureImageResult getCroppedBitmap--->"+thumbnail.width+"*"+thumbnail.height)

        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        finalFile = File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg")
        val fo: FileOutputStream
        try {
            finalFile!!.createNewFile()
            fo = FileOutputStream(finalFile)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    fun rotateBitmap(bitmap:Bitmap, orientation:Int):Bitmap {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        try
        {
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true)
            bitmap.recycle()
            return bmRotated
        }
        catch (e:OutOfMemoryError) {
            e.printStackTrace()
            return bitmap
        }
    }




    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }
}
