package com.example.focusminder

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class OnboardScreen3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard_screen3)

        val navigateToLoginButton = findViewById<ImageView>(R.id.imageView11)
        navigateToLoginButton.setOnClickListener { // Navigate to login page
            val intent = Intent(this@OnboardScreen3, Login::class.java)
            startActivity(intent)
        }
    }
}
