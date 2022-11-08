package com.example.appic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appic.model.Brand

class FilterViewModel : ViewModel() {

    val accountNumberCount = MutableLiveData(0)
    val brandCount = MutableLiveData(0)
    val locationCount = MutableLiveData(0)
    var selectedFiltersList: MutableLiveData<List<Brand>> = MutableLiveData()
}