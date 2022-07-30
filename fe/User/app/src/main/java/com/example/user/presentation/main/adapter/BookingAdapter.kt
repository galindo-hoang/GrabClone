package com.example.user.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.user.databinding.ItemCardBookingBinding

class BookingAdapter(val list: List<String>): RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(view: ItemCardBookingBinding): RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder(ItemCardBookingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int){}

    override fun getItemCount(): Int = 3
}