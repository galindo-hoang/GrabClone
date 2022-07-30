package com.example.user.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.user.databinding.ItemCardPromptBinding

class PromptAdapter(val list: List<String>): RecyclerView.Adapter<PromptAdapter.PromptViewHolder>() {
    class PromptViewHolder(view: ItemCardPromptBinding): RecyclerView.ViewHolder(view.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromptViewHolder {
        return PromptViewHolder(ItemCardPromptBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PromptViewHolder, position: Int) {}

    override fun getItemCount(): Int = 3
}