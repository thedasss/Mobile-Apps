package com.example.focusminder

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity

class OnboardScreen2 : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard_screen2) // Corrected layout

        // Initialize gesture detector
        gestureDetector = GestureDetector(this, MyGestureListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            return gestureDetector.onTouchEvent(it) || super.onTouchEvent(it)
        }
        return super.onTouchEvent(event)
    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val sensitivity = 50
            if (e1 != null && e2 != null) {
                if (e1.x - e2.x > sensitivity) {
                    // Swipe left (move to the next screen)
                    // Start the OnboardScreen3 activity
                    val intent = Intent(this@OnboardScreen2, OnboardScreen3::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    return true
                }
            }
            return false
        }
    }
}
