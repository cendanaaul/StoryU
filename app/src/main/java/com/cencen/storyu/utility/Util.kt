package com.cencen.storyu.utility

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val dateTimeFormat: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun makeCustomFilePhoto(context: Context): File {
    val fileStorage: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(dateTimeFormat, ".jpg", fileStorage)
}

fun convertUriToFiles(image: Uri, context: Context): File {
    val dataContentProvider: ContentResolver = context.contentResolver
    val photoFiles = makeCustomFilePhoto(context)

    val add = dataContentProvider.openInputStream(image) as InputStream
    val result: OutputStream = FileOutputStream(photoFiles)
    val limit = ByteArray(1024)
    var len: Int
    while (add.read(limit).also { len = it } > 0) result.write(limit, 0, len)
    result.close()
    add.close()

    return photoFiles
}

fun minimizeImageFile(file: File, maxFileSize: Long = 1000000): File? {
    val targetFileSize = maxFileSize
    val targetQuality = 80
    val options = BitmapFactory.Options()
    options.inSampleSize = 1
    val originalBitmap = BitmapFactory.decodeFile(file.path, options)
    var quality = targetQuality
    var fileSize = file.length()

    while (fileSize > targetFileSize && quality > 0) {
        val stream = ByteArrayOutputStream()
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val bitmapData = stream.toByteArray()
        val tempFile = File.createTempFile("temp_", ".jpg", file.parentFile)
        val fileOutputStream = FileOutputStream(tempFile)
        fileOutputStream.write(bitmapData)
        fileOutputStream.close()
        quality -= 5
        fileSize = tempFile.length()
        file.delete()
        tempFile.renameTo(file)
    }

    return if (fileSize <= targetFileSize) {
        file
    } else {
        null
    }
}