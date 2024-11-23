package com.example.focusminder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    private val PREF_NAME = "UserPrefs"
    private val PREF_EMAIL = "email"
    private val PREF_PASSWORD = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val navigateToRegisterButton = findViewById<TextView>(R.id.registerText)
        navigateToRegisterButton.setOnClickListener {
            // Navigate to register page
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }

        val navigateToLoginButtonT = findViewById<TextView>(R.id.LoginBtn)
        navigateToLoginButtonT.setOnClickListener {
            val emailEditText = findViewById<EditText>(R.id.email)
            val passwordEditText = findViewById<EditText>(R.id.passowrd)

            val enteredEmail = emailEditText.text.toString()
            val enteredPassword = passwordEditText.text.toString()

            // Retrieve user data from shared preferences
            val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val storedEmail = sharedPreferences.getString(PREF_EMAIL, null)
            val storedPassword = sharedPreferences.getString(PREF_PASSWORD, null)

            // Check if user has registered
            if (storedEmail != null && storedPassword != null) {
                // Check if entered credentials match stored values
                if (enteredEmail == storedEmail && enteredPassword == storedPassword) {
                    // Navigate to main activity
                    val intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Display error message for invalid credentials
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Display error message for unregistered user
                Toast.makeText(this, "You need to register first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}