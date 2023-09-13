package com.example.test1.photoDB

import android.annotation.SuppressLint
import android.content.ContentUris
import android.provider.MediaStore
import android.util.Log
import com.example.test1.MainActivity
import com.example.test1.models.ItemPhoto
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class QueryClass @Inject constructor(): QueryInterface { // retuns gallery data
    var galleryImageUrls: ArrayList<ItemPhoto> = ArrayList()
    @SuppressLint("SuspiciousIndentation")
    override suspend fun getQuery(page: String): List<ItemPhoto> {
        galleryImageUrls.clear()
        val c: Calendar = Calendar.getInstance()
        c.add(Calendar.DATE, -7);
        val date1: Date = c.getTime()
        val cc: Calendar = Calendar.getInstance()
        val date2: Date = cc.getTime()
        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.TITLE
        )
        val searchQuery = MediaStore.Images.Media.TITLE+" like '%$page%'"
        val imagecursor = MainActivity.context?.get()?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                .buildUpon()
                .appendQueryParameter("LIMIT",
                    "1")
                .appendQueryParameter("offset",
                    "1")
                .build(),
            columns,
            MediaStore.Images.Media.DATE_TAKEN + ">? and "
                    + MediaStore.Images.Media.DATE_TAKEN + "<? and "+searchQuery,
            arrayOf<String>("" +date1.time, "" + date2.time),
            MediaStore.Images.Media.DATE_TAKEN+" DESC "
        )
        val image_column_index = imagecursor?.getColumnIndex(MediaStore.Images.Media._ID)
        var gallery: ArrayList<ItemPhoto> = ArrayList()
        for (i in 0 until imagecursor?.count!!) {
            imagecursor!!.moveToPosition(i)
            try {
                val id = imagecursor.getInt(image_column_index!!)
                val imageUri_t =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())
                val indexDisplayName: Int =
                    imagecursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)
                val fileName: String = imagecursor.getString(indexDisplayName)
                val indexDate: Int =
                    imagecursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_TAKEN)
                val fileDate: String = imagecursor.getString(indexDate)
                    gallery.add(ItemPhoto(imageUri_t, fileName, fileDate))
            } catch (e: Exception){}
        }
        galleryImageUrls.add(ItemPhoto(gallery.get(0).uri, gallery.get(0).title, gallery.get(0).date, gallery))
        imagecursor!!.close()
        return galleryImageUrls
    }



}