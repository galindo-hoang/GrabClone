package com.example.user.presentation.booking.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.user.R
import com.example.user.data.dto.Vehicle
import com.example.user.databinding.ItemVehicleBinding
import com.example.user.utils.TypeCar
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.DecimalFormat
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class VehicleAdapter @Inject constructor(
    @ApplicationContext
    private val context: Context
): ListAdapter<Vehicle,VehicleAdapter.VehicleViewHolder>(callback) {

    private var list = listOf<Vehicle>()
    private var func: ((it: Vehicle, position:Int) -> Unit)? = null
    private var itemSelected = 0
    fun setOnClickListener(func: (it: Vehicle, position:Int) -> Unit) { this.func = func }
    fun getItem() = this.list[itemSelected]
    fun setList(list: List<Vehicle>){
        this.list = list
        submitList(list)
    }

    inner class VehicleViewHolder(val view: ItemVehicleBinding): RecyclerView.ViewHolder(view.root){
        fun bind(model: Vehicle, position: Int){
            view.root.setOnClickListener {
                notifyItemChanged(position)
                notifyItemChanged(itemSelected)
                itemSelected = position
                func?.let { it1 -> it1(model,position) }
            }
            if (itemSelected == adapterPosition){
                view.root.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.light_green, null))
            }else{
                view.root.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.white, null))
            }
            if(adapterPosition == list.size - 1) { view.divider.visibility = View.GONE }

            if(model.typeCar == TypeCar.CAR) view.tvSeatAvailable.text = "3 seater"
            else view.tvSeatAvailable.text = "1 seater"
            view.tvName.text = model.typeCar.name
            view.ivVehicle.setImageDrawable(model.drawable)
            view.tvPrice.text = "${DecimalFormat("#.###").format(model.getPrice())}đ"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        return VehicleViewHolder(ItemVehicleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int = list.size


    companion object {
        val callback = object : DiffUtil.ItemCallback<Vehicle>() {
            override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean =
                oldItem.typeCar == newItem.typeCar

            override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean =
                oldItem == newItem
        }
    }
}