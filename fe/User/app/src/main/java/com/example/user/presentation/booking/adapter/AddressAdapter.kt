package com.example.user.presentation.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.user.data.dto.Payment
import com.example.user.data.model.place.Address
import com.example.user.databinding.ItemAddressBinding

class AddressAdapter: RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    private var list = listOf<Address>()
    private var func: ((Address) -> Unit)? = null
    fun setOnClickListener(func: (Address) -> Unit) {
        this.func = func
    }

    fun setList(list: List<Address>) {
        this.list = list

    }

    inner class AddressViewHolder(val view: ItemAddressBinding): RecyclerView.ViewHolder(view.root){
        fun setContent(address: Address, position: Int) {
            view.tvAddress.text = address.name
            if(position == list.size - 1) view.divider.visibility = View.INVISIBLE
            else view.divider.visibility = View.VISIBLE
            view.root.setOnClickListener {
                func?.let { it1 -> it1(address) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.setContent(list[position],position)
    }

    override fun getItemCount(): Int = list.size
}