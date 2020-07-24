package com.denobili.app.teacherAttachmentPage

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import kotlinx.android.synthetic.main.activity_document_chooser.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.onClick
import java.io.File
import java.net.URISyntaxException


class DocumentChooserActivity : LocalizationActivity(), AnkoLogger {


    val DATA_TYPES: String = "application/msword," +
            ".pdf," +
            "application/vnd.ms-powerpoint," +
            "application/vnd.ms-excel," +
            "image/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_chooser)
        attach_document.onClick {
            showFileChooser()
        }

    }


    private val FILE_SELECT_CODE = 0

    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE)
        } catch (ex: android.content.ActivityNotFoundException) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            FILE_SELECT_CODE -> if (resultCode == Activity.RESULT_OK) {
                // Get the Uri of the selected file
                val uri = data.data
                if (uri != null) {
                    var file: File = File(getPath(this, uri))
                    var filename = file.name
                    try {
                        val returnCursor = contentResolver.query(uri, null, null, null, null)
                        /*
     * Get the column indexes of the data in the Cursor,
     * move to the first row in the Cursor, get the data,
     * and display it.
     */
                        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
                        returnCursor.moveToFirst()

                        filename = returnCursor.getString(nameIndex)
                        returnCursor.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    error { "file name " + filename }

                }
                error { "filetype is " + getPath(this@DocumentChooserActivity, uri) }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null

            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor!!.moveToFirst()) {
                    return cursor!!.getString(column_index)
                }
            } catch (e: Exception) {
                // Eat it
            }

        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }

        return null
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


}
