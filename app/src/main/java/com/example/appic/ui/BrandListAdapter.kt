package com.example.appic.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appic.databinding.FilterOptionBinding
import com.example.appic.model.Brand


class BrandListAdapter(
        private val isSelected: (brand: Brand) -> Boolean,
        private val onItemClicked: (brand: Brand, position: Int) -> Unit,
) : ListAdapter<Brand, BrandListAdapter.BrandViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val itemBinding = FilterOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrandViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val listItem = getItem(position)
        if (listItem != null) {
            holder.bindTo(listItem)
        }
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int, payloads: MutableList<Any>) {
        val listItem = getItem(position)
        if (listItem != null) {
            holder.bindTo(listItem, payloads)
        }
    }

    inner class BrandViewHolder(private val itemBinding: FilterOptionBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(brand: Brand) {
            updateUi(brand)
        }

        fun bindTo(brand: Brand, payloads: MutableList<Any>) {
            updateUi(brand)
        }

        private fun onCheckChangeListener(brand: Brand) {
            itemBinding.root.setOnClickListener {
                onItemClicked.invoke(brand, bindingAdapterPosition)
            }
        }

        private fun updateUi(brand: Brand) {
            itemBinding.checkbox.text = brand.brandName
            itemBinding.checkbox.isChecked = isSelected.invoke(brand)
            onCheckChangeListener(brand)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Brand>() {
            override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean = oldItem == newItem
        }
    }
}