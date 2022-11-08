package com.example.appic.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Brand(@SerializedName("brandName") var brandName: String, var isSelected: Boolean = false) : Parcelable
