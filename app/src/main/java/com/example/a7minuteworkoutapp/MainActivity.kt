package com.example.a7minuteworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.a7minuteworkoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbarMain)
        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val navController = this.findNavController(R.id.myNavHostFragment)

        binding.toolbarMain.setNavigationOnClickListener {
            navController.navigateUp()
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.titleFragment) {
                binding.toolbarMain.visibility = View.GONE
                binding.toolbarMain.title = "7 Minute Workout"
            } else if (destination.id == R.id.BMIFragment) {
                binding.toolbarMain.visibility = View.VISIBLE
                binding.toolbarMain.title = "BMI Calculator"
            } else if (destination.id == R.id.historyFragment) {
                binding.toolbarMain.visibility = View.VISIBLE
                binding.toolbarMain.title = "History"
            } else {
                binding.toolbarMain.visibility = View.VISIBLE
                binding.toolbarMain.title = "7 Minute Workout"
            }
        }
    }
}