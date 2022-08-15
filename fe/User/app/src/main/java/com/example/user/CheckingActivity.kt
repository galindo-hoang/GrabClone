package com.example.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class CheckingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checking)
        findViewById<Button>(R.id.btnChecking).setOnClickListener {
            try {
                a()
            } catch (e:Exception){
                Log.e("------",e.message.toString())
            }
        }
    }

    fun a(){
        throw Exception("hello")
    }
}