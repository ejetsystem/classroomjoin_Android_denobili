package com.classroomjoin.app.teacherAttachmentPage

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.webkit.MimeTypeMap
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.FileChooser
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper.Utility
import com.classroomjoin.app.helper_utils.OfflineAttachmentModel
import com.classroomjoin.app.teacherMessageSendPage.TeacherCommunicationPage
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.saveAll
import kotlinx.android.synthetic.main.activity_image_chooser.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.error
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick
import rx.Observer
import java.io.*
import java.util.*


class ImageChooserActivity : LocalizationActivity(), AnkoLogger, AttachmentAdapterDelegate.AttachmentClickListener, AddAttachmentAdapterDelegate.AddAttachmentListener {


    override fun onClicked() {
        selectImage()
    }

    companion object Keys {
        val Attachment_Mapping_id_key = "com.classroom.mapping_id"
        val EVENT_TYPE = "com.classroom.event_type"

    }

    override fun onItemRemove(position: Int) {
        error { "position is " + position }
        adapter?.remove(position)
    }


    private var userChoosenTask: String = "Take Photo"
    private val REQUEST_CAMERA: Int = 1234
    private val SELECT_FILE: Int = 4321
    var finalFile: File? = null
    var viewmodel: AttachmentViewModel? = null
    private var adapter: AttachmentAdapter? = null

    private var progressdialog: ProgressDialog? = null
    private var attachment_id: Int = 0
    private var event_type: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_chooser)

        adapter = AttachmentAdapter(arrayListOf(AddAttachment()), this, this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.add_attachment))

        recycler_home.layoutManager = GridLayoutManager(this, 2)
        recycler_home.adapter = adapter
        recycler_home.isNestedScrollingEnabled = false
        progressdialog = DialogUtil.showProgressDialog(this@ImageChooserActivity)
        fab.imageResource = R.drawable.ic_done_white_24dp

        viewmodel = AttachmentViewModel(this@ImageChooserActivity)
        if (intent.hasExtra(Attachment_Mapping_id_key)) attachment_id = intent.getIntExtra(Attachment_Mapping_id_key, 0)
        fab.onClick { uploadFiles() }


        if (intent.hasExtra(EVENT_TYPE)) {
            event_type = intent.getIntExtra(EVENT_TYPE, -1)
        }

        println("DATA--->event_type11--->" + event_type)

    }

    private fun uploadFiles() {
        var images: ArrayList<MultipartBody.Part>? = ArrayList<MultipartBody.Part>()
        adapter!!.items.forEachWithIndex { i, displayItem ->
            if (displayItem is Attachment) {
                val attachment = displayItem as Attachment
                val filePart = MultipartBody.Part.createFormData("file" + "" + i, attachment.file!!.getName(),
                        RequestBody.create(MediaType.parse("image/*"), attachment.file))
                images!!.add(filePart)
            }
        }
        if (images!!.isNotEmpty()) {
            if ((NetworkHelper.isOnline(this@ImageChooserActivity))) {
                progressdialog!!.show()
                viewmodel!!.uploadAttachment(images, attachment_id, event_type).subscribe(Observer)
            } else DialogUtil.showerrorDialog(getString(R.string.noInternet), this@ImageChooserActivity::uploadFiles, this@ImageChooserActivity)
        } else Snackbar.make(recycler_home, getString(R.string.no_images), Snackbar.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(getString(R.string.take_photo), getString(R.string.choose_from_library), getString(R.string.btn_cancel))
        val builder = AlertDialog.Builder(this@ImageChooserActivity)
        builder.setTitle(getString(R.string.app_name))
        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
            val result = Utility.checkPermission(this@ImageChooserActivity)
            if (items[item] == getString(R.string.take_photo)) {
                userChoosenTask = getString(R.string.take_photo)
                if (result)
                    cameraIntent()
            } else if (items[item] == getString(R.string.choose_from_library)) {
                userChoosenTask = getString(R.string.choose_from_library)
                if (result)
                    galleryIntent()
            } else if (items[item] == getString(R.string.btn_cancel)) {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_FILE)
    }

    private fun addFile(file: File) {
        adapter?.additemat(adapter?.itemCount!! - 1, Attachment(file.name, file))
    }

    private fun cameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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
            if (requestCode == SELECT_FILE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    onSelectFromGalleryResult(data)
                }
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun onSelectFromGalleryResult(data: Intent?) {
        if (data != null) {
            try {
                val uri = data.data
                if (uri != null) {
                    var file = File(FileChooser.getPath(this@ImageChooserActivity, uri))
                    val file_size = Integer.parseInt((file.length() / 1024).toString())
                    var filename = file.name
                    try {
                        val returnCursor = contentResolver.query(uri, null, null, null, null)
                        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
                        returnCursor.moveToFirst()
                        filename = returnCursor.getString(nameIndex)
                        returnCursor.close()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (checkFileType(getMimeType(uri)) && file_size / 1024 < 25)
                        addFile(file = file)
                    else if (!checkFileType(getMimeType(uri))) Snackbar.make(recycler_home, getString(R.string.support_filetype_error), Snackbar.LENGTH_LONG).show()
                    else Snackbar.make(recycler_home, getString(R.string.file_size_large), Snackbar.LENGTH_LONG).show()
                }

            } catch (e: IOException) {
                e.printStackTrace()
                Snackbar.make(recycler_home, getString(R.string.file_issue), Snackbar.LENGTH_LONG).show()

            }

        }
    }

    private val Observer: Observer<FileAttachmentResponse>
        get() = object : Observer<FileAttachmentResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                progressdialog!!.dismiss()
                DialogUtil.showerrorDialog(getString(R.string.some_error), this@ImageChooserActivity::uploadFiles, this@ImageChooserActivity)
                e?.printStackTrace()
            }

            override fun onNext(t: FileAttachmentResponse) {
                progressdialog!!.dismiss()
                if (t.status == "Success") {
                    OfflineAttachmentModel().delete { realmQuery -> realmQuery.equalTo("att_map_id", attachment_id) }
                    t.data.saveAll()
                    val intent = Intent()
                    intent.putExtra(TeacherCommunicationPage.attachment_list_key, t.data[0].att_map_id!!)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    DialogUtil.showerrorDialog(getString(R.string.some_error), this@ImageChooserActivity::uploadFiles, this@ImageChooserActivity)
                }

            }
        }

    private fun onCaptureImageResult(data: Intent?) {
        error { "this is here" }
        val thumbnail = data?.extras?.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        finalFile = File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg")
        val fo: FileOutputStream
        addFile(finalFile!!)
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

    fun getMimeType(uri: Uri): String {
        var mimeType: String? = null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            val cr: ContentResolver = applicationContext.contentResolver
            mimeType = cr.getType(uri);
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }


    private fun checkFileType(value: String): Boolean =
            when (value) {
                ApprovedFileTypes.DOC.value,
                ApprovedFileTypes.PDF.value,
                ApprovedFileTypes.JPG.value,
                ApprovedFileTypes.PNG.value,
                ApprovedFileTypes.DOCX.value,
                ApprovedFileTypes.TXT.value,
                ApprovedFileTypes.XLS.value,
                ApprovedFileTypes.XLSX.value -> {
                    true
                }
                else -> false
            }


    enum class ApprovedFileTypes constructor(val value: String) {
        PDF("application/pdf"),
        JPG("image/jpeg"),
        PNG("image/png"),
        DOC("application/msword"),
        DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        TXT("text/plain"),
        XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        XLS("application/vnd.ms-excel");
    }


}
