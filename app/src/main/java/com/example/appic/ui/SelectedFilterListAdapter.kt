package com.example.appic.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appic.databinding.SelectedFilterItemBinding


class SelectedFilterListAdapter() : ListAdapter<String, SelectedFilterListAdapter.SelectedFilterViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedFilterViewHolder {
        val itemBinding = SelectedFilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedFilterViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SelectedFilterViewHolder, position: Int) {
        val listItem = getItem(position)
        if (listItem != null) {
            holder.bindTo(listItem)
        }
    }

    inner class SelectedFilterViewHolder(private val itemBinding: SelectedFilterItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(item: String) {
            itemBinding.root.text = item
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        }
    }
}