package com.example.test1.screens.detail

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.test1.databinding.ItemDataBinding
import com.example.test1.models.ItemPhoto
import com.example.test1.screens.adapter.AdapterType
import com.example.test1.screens.adapter.SimpleAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DetailVM @Inject constructor(): ViewModel() {
    var photosAdapter = SimpleAdapter(null, AdapterType.Detail)
}