package com.example.user.presentation.booking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.user.R
import com.example.user.data.dto.Payment
import com.example.user.databinding.ItemPaymentBinding
import com.example.user.utils.PaymentMethod
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PaymentAdapter @Inject constructor(
    @ApplicationContext
    private val context: Context
): ListAdapter<Payment,PaymentAdapter.PaymentViewHolder>(callback) {

    private var list = listOf<Payment>()
    private var func: ((it: Payment, position:Int) -> Unit)? = null
    private var itemSelected = 0
    fun setOnClickListener(func: (it: Payment, position:Int) -> Unit) { this.func = func }
    fun getItem() = this.list[itemSelected]
    fun setList(list: List<Payment>){
        this.list = list
        submitList(list)
    }

    inner class PaymentViewHolder(val view: ItemPaymentBinding): RecyclerView.ViewHolder(view.root){
        fun bind(model: Payment,position: Int){
            view.root.setOnClickListener {
                notifyItemChanged(position)
                notifyItemChanged(itemSelected)
                itemSelected = position
                func?.let { it1 -> it1(model,position) }
            }
            if(position == list.size - 1) view.divider.visibility = View.INVISIBLE
            else view.divider.visibility = View.VISIBLE
            if (itemSelected == adapterPosition) {
                view.root.setBackgroundColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        R.color.light_green,
                        null
                    )
                )
            }else {
                view.root.setBackgroundColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        R.color.white,
                        null
                    )
                )
            }

            view.iv.setImageDrawable(model.drawable)
            view.tvNameMethod.text = model.method.name
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
                oldItem.method == newItem.method
            override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean =
                oldItem == newItem
        }
    }
}