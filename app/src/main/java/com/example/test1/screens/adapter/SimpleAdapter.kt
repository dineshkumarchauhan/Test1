package com.example.test1.screens.adapter

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.R
import com.example.test1.databinding.GridItemBinding
import com.example.test1.databinding.ListItemBinding
import com.example.test1.models.ItemPhoto
import com.example.test1.utils.dateConvert
import com.squareup.picasso.Picasso
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class SimpleAdapter(private val layoutManager: GridLayoutManager? = null, home: AdapterType) :
    ListAdapter<ItemPhoto, RecyclerView.ViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    var type = home
    inner class GridViewHolder(private val binding: GridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(dataClass: ItemPhoto) {
            binding.apply {
                binding.txtCount.text = "${(dataClass.photoResult.size-1)}+"
                binding.txtTitle.text = dataClass.title
                Picasso.get().load(dataClass.uri).into(binding!!.ivIcon)
                binding.txtDate.text = dataClass.date.dateConvert()
                if(type == AdapterType.Home){
                    if (dataClass.photoResult.size-1 == 0)  binding.txtCount.visibility = View.GONE else binding.txtCount.visibility = View.VISIBLE
                    binding.root.setOnClickListener {
                        binding.root.findNavController().navigate(R.id.home_to_detail, Bundle().apply {
                            putParcelable("data", dataClass)
                        })
                    }
                }else{
                    if (dataClass.photoResult.size == 0)  binding.txtCount.visibility = View.GONE else binding.txtCount.visibility = View.VISIBLE
                }
            }
        }
    }

    inner class ListViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(dataClass: ItemPhoto) {
            binding.apply {
                binding.txtCount.text = "${(dataClass.photoResult.size-1)}+"
                binding.txtTitle.text = dataClass.title
                Picasso.get().load(dataClass.uri).into(binding!!.ivIcon)
                binding.txtDate.text = dataClass.date.dateConvert()
                if(type == AdapterType.Home){
                    if (dataClass.photoResult.size-1 == 0)  binding.txtCount.visibility = View.GONE else binding.txtCount.visibility = View.VISIBLE
                    binding.root.setOnClickListener {
                        binding.root.findNavController().navigate(R.id.home_to_detail, Bundle().apply {
                            putParcelable("data", dataClass)
                        })
                    }
                }else{
                    if (dataClass.photoResult.size == 0)  binding.txtCount.visibility = View.GONE else binding.txtCount.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (layoutManager?.spanCount == 1) ViewType.LIST.ordinal
        else ViewType.GRID.ordinal
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ViewType.GRID.ordinal -> {
                val binding =
                    GridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return GridViewHolder(binding)
            }
            ViewType.LIST.ordinal -> {
                val binding =
                    ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ListViewHolder(binding)
            }
            else -> {
                val binding =
                    ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ListViewHolder(binding)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.getItemViewType() == ViewType.GRID.ordinal) {
            val currentItem = getItem(position)
            (holder as GridViewHolder).bind(currentItem)
        }
        if (holder.getItemViewType() == ViewType.LIST.ordinal) {
            val currentItem = getItem(position)
            (holder as ListViewHolder).bind(currentItem)
        }
    }


    enum class ViewType {
        GRID,
        LIST
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ItemPhoto>() {
            override fun areItemsTheSame(
                oldItem: ItemPhoto,
                newItem: ItemPhoto
            ): Boolean {
                return true
            }

            override fun areContentsTheSame(
                oldItem: ItemPhoto,
                newItem: ItemPhoto
            ): Boolean {
                return true
            }
        }
    }
}