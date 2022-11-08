package com.example.appic.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appic.R
import com.example.appic.databinding.ActivitySelectBrandBinding
import com.example.appic.model.Brand
import com.example.appic.utils.Utils
import com.google.gson.Gson
import com.google.gson.JsonObject


class SelectBrandActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectBrandBinding
    private lateinit var brandListAdapter: BrandListAdapter
    private val selectedBrandList = mutableSetOf<Brand>()
    private val brandList = MutableLiveData<List<Brand>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBrandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val preSelectedBrands = intent.extras?.getParcelableArrayList<Brand>(SELECTED_BRAND)
        preSelectedBrands?.toList()?.let { selectedBrandList.addAll(it) }
        brandListAdapter = BrandListAdapter(isSelected = { selectedBrandList.contains(it) }, onItemClicked = { brand, position ->
            if (selectedBrandList.contains(brand)) {
                selectedBrandList.remove(brand)
            } else {
                selectedBrandList.add(brand)
            }
            brandListAdapter.notifyItemChanged(position)
        })
        binding.brandNameRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = brandListAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
        brandList.postValue(getBrandNames().toList())
        val decorator = DividerItemDecoration(this, RecyclerView.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.decorator)!!)
        binding.brandNameRecyclerView.addItemDecoration(decorator)
        brandList.observe(this) {
            brandListAdapter.submitList(it)
        }
        binding.applyFilterBtn.setOnClickListener {
            setResult()
        }
        binding.selectAll.setOnClickListener {
            brandList.value?.forEach {
                if (!it.isSelected) {
                    it.isSelected = true
                }
            }
            selectedBrandList.removeAll(selectedBrandList)
            brandList.value?.let { selectedBrandList.addAll(it) }
            brandListAdapter.notifyDataSetChanged()
        }
        binding.clear.setOnClickListener {
            brandList.value?.forEach {
                if (it.isSelected) {
                    it.isSelected = false
                }
            }
            selectedBrandList.removeAll(selectedBrandList)
            brandListAdapter.notifyDataSetChanged()
        }
        val list = getBrandNames().toList()
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.selectAll.visibility = View.GONE
                binding.clear.visibility = View.GONE
            } else {
                binding.selectAll.visibility = View.VISIBLE
                binding.clear.visibility = View.VISIBLE
            }
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(list, newText?.lowercase())
                return true
            }
        })
    }

    private fun filterList(list: List<Brand>, query: String?) {
        brandList.value = query?.let { list.filter { it.brandName.lowercase().contains(query) } } ?: list
    }

    private fun setResult() {
        val resultIntent = Intent()
        resultIntent.putParcelableArrayListExtra(SELECTED_BRAND, ArrayList(selectedBrandList))
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun getBrandNames(): MutableSet<Brand> {
        val jsonFileString = Utils.getAssetJsonData(this)
        val gson = Gson()
        val data = gson.fromJson(jsonFileString, JsonObject::class.java)
        val filterData = data.getAsJsonArray("filterData")
        val hierarchy = filterData.get(0).asJsonObject.getAsJsonArray("hierarchy")
        val listOfBrands = mutableSetOf<Brand>()
        hierarchy.forEach { hierarchyElement ->
            hierarchyElement.asJsonObject.getAsJsonArray("brandNameList").forEach { brandListElement ->
                listOfBrands.add(Brand(brandListElement.asJsonObject.get("brandName").asString))
            }
        }
        return listOfBrands
    }

    companion object {
        const val SELECTED_BRAND = "selectedBrand"

        fun getResultIntent(fragment: Fragment, brandsList: List<Brand>?): Intent {
            val intent = Intent(fragment.context, SelectBrandActivity::class.java)
            intent.putParcelableArrayListExtra(SELECTED_BRAND, brandsList?.let { ArrayList(it) })
            return intent
        }
    }
}