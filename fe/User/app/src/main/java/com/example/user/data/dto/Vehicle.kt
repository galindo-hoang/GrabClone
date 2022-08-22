package com.example.user.data.dto

import android.graphics.drawable.Drawable
import com.example.user.utils.TypeCar
import kotlin.math.roundToInt

data class Vehicle(
    val typeCar: TypeCar,
    val drawable: Drawable?,
    val distance: Int
) {
    fun getPrice(): Int {
        return when(typeCar){
            TypeCar.MOTORCYCLE -> {
                if(distance <= 2) 12500
                else 12500 + (distance - 2) * 4300
            }
            TypeCar.CAR -> {
                if(distance <= 2) 29000
                else 29000 + (distance - 2) *10000
            }
        }
    }
}