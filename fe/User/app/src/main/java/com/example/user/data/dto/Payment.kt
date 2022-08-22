package com.example.user.data.dto

import android.graphics.drawable.Drawable
import com.example.user.utils.PaymentMethod

data class Payment(
    val method: PaymentMethod,
    val drawable: Drawable?,
)
