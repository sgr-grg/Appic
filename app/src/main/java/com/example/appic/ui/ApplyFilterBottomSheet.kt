package com.example.appic.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appic.R
import com.example.appic.databinding.FilterBottomSheetBinding
import com.example.appic.model.Brand
import com.example.appic.ui.SelectBrandActivity.Companion.SELECTED_BRAND
import com.example.appic.viewmodel.FilterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ApplyFilterBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: FilterViewModel
    private lateinit var binding: FilterBottomSheetBinding
    private lateinit var selectedFilterListAdapter: SelectedFilterListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FilterBottomSheetBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(modelClass = FilterViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.accountNumberCount.observe(viewLifecycleOwner) {
            binding.accountCount.root.text = getString(R.string.account_number, it)
            binding.accItemCount.text = it.toString()
        }
        viewModel.brandCount.observe(viewLifecycleOwner) {
            binding.brandCount.root.text = getString(R.string.brand_count, it)
            binding.brandItemCount.text = it.toString()
        }
        viewModel.locationCount.observe(viewLifecycleOwner) {
            binding.locationCount.root.text = getString(R.string.location_count, it)
            binding.locationItemCount.text = it.toString()
        }
        selectedFilterListAdapter = SelectedFilterListAdapter()
        binding.selectedFilterRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = selectedFilterListAdapter
        }
        viewModel.selectedFiltersList.observe(viewLifecycleOwner) { selectedFilterItem ->
            if (selectedFilterItem.isEmpty()) {
                binding.selectedFilterRecyclerView.visibility = View.GONE
            } else {
                binding.selectedFilterRecyclerView.visibility = View.VISIBLE
                selectedFilterListAdapter.submitList(selectedFilterItem.map { it.brandName })
            }
        }
        binding.selectBrand.setOnClickListener {
            selectBrandResultLauncher.launch(SelectBrandActivity.getResultIntent(this, viewModel.selectedFiltersList.value?.map { Brand(it.brandName) }))
        }

        binding.applyFilterBtn.setOnClickListener {
            dialog?.cancel()
        }

        binding.closeBtn.setOnClickListener {
            dialog?.cancel()
        }
    }

    private val selectBrandResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val selectedBrand = result.data!!.getParcelableArrayListExtra<Brand>(SELECTED_BRAND)
            viewModel.brandCount.value = selectedBrand?.size
            viewModel.selectedFiltersList.postValue(selectedBrand)
        }
    }
}