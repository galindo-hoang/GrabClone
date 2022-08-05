package com.example.user.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.user.databinding.ItemCardBookingBinding
import com.example.user.databinding.ItemCardVoucherBinding

class VoucherAdapter(val list: List<String>): RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>() {

    class VoucherViewHolder(view: ItemCardVoucherBinding): RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VoucherViewHolder {
        return VoucherViewHolder(ItemCardVoucherBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {}

    override fun getItemCount(): Int = 3
}