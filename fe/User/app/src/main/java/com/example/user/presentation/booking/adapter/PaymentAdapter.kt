package com.example.user.presentation.booking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.user.R
import com.example.user.data.dto.Payment
import com.example.user.databinding.ItemPaymentBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PaymentAdapter @Inject constructor(
    @ApplicationContext
    private val context: Context
): ListAdapter<Payment,PaymentAdapter.PaymentViewHolder>(callback) {

    private var list = listOf<Payment>()
    private var func: ((it: Payment, position:Int) -> Unit)? = null
    private var itemSelected = 2
    fun setOnClickListener(func: (it: Payment, position:Int) -> Unit){
        this.func = func
    }

    fun setList(list: List<Payment>){
        this.list = list
        submitList(list)
    }

    inner class PaymentViewHolder(view: ItemPaymentBinding): RecyclerView.ViewHolder(view.root){
        private val binding = view
        fun bind(model: Payment,position: Int){
            binding.root.setOnClickListener {
                notifyItemChanged(position)
                notifyItemChanged(itemSelected)
                itemSelected = position
                func?.let { it1 -> it1(model,position) }
            }
            if (itemSelected == adapterPosition){
                binding.root.setBackgroundColor(ResourcesCompat.getColor(context.resources,R.color.light_green,null))
            }else{
                binding.root.setBackgroundColor(ResourcesCompat.getColor(context.resources,R.color.white,null))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        return PaymentViewHolder(ItemPaymentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(getItem(position),position)
    }

    override fun getItemCount(): Int = list.size


    companion object {
        val callback = object : DiffUtil.ItemCallback<Payment>() {
            override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean =
                oldItem == newItem
        }
    }
}