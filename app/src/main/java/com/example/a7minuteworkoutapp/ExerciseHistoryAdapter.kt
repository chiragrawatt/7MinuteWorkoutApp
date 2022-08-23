package com.example.a7minuteworkoutapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minuteworkoutapp.databinding.ItemExerciseHistoryDateBinding

class ExerciseHistoryAdapter(val context: Context, val items: ArrayList<String>) : RecyclerView.Adapter<ExerciseHistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemExerciseHistoryDateBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvItem = binding.ivItem
        val tvPosition = binding.ivPosition
        val llView = binding.llHistoryItemMain
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemExerciseHistoryDateBinding = ItemExerciseHistoryDateBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItem.text = items[position]
        holder.tvPosition.text = (position+1).toString()

        if(position%2==0) {
            holder.llView.setBackgroundColor(ContextCompat.getColor(context, R.color.seaGreen))
        } else {
            holder.llView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}