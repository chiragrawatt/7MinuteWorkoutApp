package com.example.a7minuteworkoutapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.*
import com.example.a7minuteworkoutapp.databinding.FragmentTitleBinding

class Title : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding: FragmentTitleBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.fragment_title, container, false)

        binding.startBtn.setOnClickListener {
            val intent = Intent(activity , ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding.btnBmi.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_titleFragment_to_BMIFragment)
        }

        binding.btnHistory.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_titleFragment_to_historyFragment)
        }

        return binding.root
    }
}