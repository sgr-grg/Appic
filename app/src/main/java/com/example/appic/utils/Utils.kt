package com.example.appic.utils

import android.content.Context
import java.io.IOException

object Utils {

    fun getAssetJsonData(context: Context): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open("data.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}