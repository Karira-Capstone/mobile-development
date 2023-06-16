package com.capstone.karira.utils

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale


private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private const val MAXIMAL_SIZE = 1000000

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun reduceFileImage(file: File): File {
    val ei = ExifInterface(file)
    val orientation: Int = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )

    var bitmap = BitmapFactory.decodeFile(file.path)
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> bitmap = rotateImage(bitmap, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> bitmap = rotateImage(bitmap, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> bitmap = rotateImage(bitmap, 270)
        ExifInterface.ORIENTATION_NORMAL -> bitmap = bitmap
        else -> bitmap = bitmap
    }

    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)

    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun createCustomTempImage(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToImg(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempImage(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}


@SuppressLint("Range")
fun getFileNameFromUri(uri: Uri, context: Context): String? {
    val fileName: String?
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.moveToFirst()
    fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    cursor?.close()
    return fileName
}

fun createCustomTempFile(suffix: String, context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    return File.createTempFile(timeStamp, suffix, storageDir)
}

fun uriToFile(selectedFile: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(".${getFileNameFromUri(selectedFile, context).toString().split(".").last()}", context)

    val inputStream = contentResolver.openInputStream(selectedFile) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun downloadFile(baseActivity:Context, url: String?, title: String?): Long {
    val direct = File(Environment.getExternalStorageDirectory().toString() + "/your_folder")

    if (!direct.exists()) {
        direct.mkdirs()
    }
    val downloadReference: Long
    var  dm: DownloadManager
    dm= baseActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val uri = Uri.parse(url)
    val request = DownloadManager.Request(uri)
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setTitle(title)
    Toast.makeText(baseActivity, "Download $title", Toast.LENGTH_SHORT).show()

    downloadReference = dm?.enqueue(request) ?: 0

    return downloadReference

}