package com.example.a7minuteworkoutapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minuteworkoutapp.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)

        setUpHistoryRecyclerView()

        return binding.root
    }

    private fun setUpHistoryRecyclerView() {
        val historyList = getALlCompleteDates()
        if(historyList.size>0) {
            binding.rvExerciseHistory.visibility = View.VISIBLE
            binding.tvBlankHistory.visibility = View.GONE
            binding.rvExerciseHistory.layoutManager = LinearLayoutManager(context)
            val historyAdapter = ExerciseHistoryAdapter(requireContext(), historyList)
            binding.rvExerciseHistory.adapter = historyAdapter
        } else {
            binding.rvExerciseHistory.visibility = View.GONE
            binding.tvBlankHistory.visibility = View.VISIBLE
        }
    }

    private fun getALlCompleteDates() : ArrayList<String> {
        val dbHandler = SqliteOpenHelper(requireContext(), null)
        return dbHandler.getAllCompletedDatesList()
    }
}