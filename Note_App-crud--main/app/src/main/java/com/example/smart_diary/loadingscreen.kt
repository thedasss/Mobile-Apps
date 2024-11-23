package com.example.smart_diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class loadingscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadingscreen)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,Home::class.java)
            startActivity(intent)
            finish()
        }, 2000L)
    }
}