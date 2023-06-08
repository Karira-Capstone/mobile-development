package com.capstone.karira.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
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

//fun rotateFile(file: File, isBackCamera: Boolean = false) {
//    val matrix = Matrix()
//    val bitmap = BitmapFactory.decodeFile(file.path)
//    val rotation = if (isBackCamera) 90f else -90f
//    matrix.postRotate(rotation)
//    if (!isBackCamera) {
//        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
//    }
//    val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//    result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
//}

fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height,
        matrix, true
    )
}

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

fun uriToFile(selectedImg: Uri, context: Context): File {
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