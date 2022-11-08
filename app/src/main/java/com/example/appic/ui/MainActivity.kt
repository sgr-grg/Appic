package com.example.appic.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openFilterBottomSheet.setOnClickListener {
            val sheet = ApplyFilterBottomSheet()
            sheet.show(supportFragmentManager, "Filters Bottom Sheet")
        }
    }
}