package com.example.user.presentation.booking.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.user.R
import com.example.user.data.dto.Vehicle
import com.example.user.databinding.ItemVehicleBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VehicleAdapter @Inject constructor(
    @ApplicationContext
    private val context: Context
): RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    private var list = listOf<Vehicle>()
    private var func: ((it: Vehicle, position:Int) -> Unit)? = null
    private var itemSelected = 2
    fun setOnClickListener(func: (it: Vehicle, position:Int) -> Unit){
        this.func = func
    }

    fun setList(list: List<Vehicle>){
        this.list = list
        notifyDataSetChanged()
    }

    inner class VehicleViewHolder(view: ItemVehicleBinding): RecyclerView.ViewHolder(view.root){
        private val binding = view
        fun bind(model: Vehicle,position: Int){
            binding.root.setOnClickListener {
                Log.e("a","+++++++ $adapterPosition ++++++++++ $position +++++++++ ${list.size}")
                notifyItemChanged(itemSelected)
                itemSelected = position
                notifyItemChanged(position)
                func?.let { it1 -> it1(model,position) }

            }
            if (itemSelected == adapterPosition){
                binding.root.setBackgroundColor(ResourcesCompat.getColor(context.resources,R.color.light_green,null))
                binding.tvTitleVehicle.setTextColor(ResourcesCompat.getColor(context.resources,R.color.black,null))
            }else{
                binding.root.setBackgroundColor(ResourcesCompat.getColor(context.resources,R.color.white,null))
                binding.tvTitleVehicle.setTextColor(ResourcesCompat.getColor(context.resources,R.color.gray,null))
            }
            Log.e("a","''''''''' $adapterPosition")
//            if(adapterPosition == list.size - 1){
//                binding.divider.visibility = View.GONE
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        return VehicleViewHolder(ItemVehicleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int = list.size
}