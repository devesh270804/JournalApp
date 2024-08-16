package com.example.journalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.databinding.JouranlRowBinding

class JournalRecyclerAdapter(private val context: Context, private var journalList: List<Journal>)
    : RecyclerView.Adapter<JournalRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: JouranlRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(journal: Journal) {
            binding.journal = journal
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = JouranlRowBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = journalList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(journalList[position])
    }
}
