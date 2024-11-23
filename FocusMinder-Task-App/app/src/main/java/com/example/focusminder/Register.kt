package com.example.focusminder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

class Register : AppCompatActivity() {

    private val PREF_NAME = "UserPrefs"
    private val PREF_EMAIL = "email"
    private val PREF_PASSWORD = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val navigateToLoginButton = findViewById<TextView>(R.id.LoginText)
        navigateToLoginButton.setOnClickListener {
            // Navigate to login page
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }

        val navigateToregButton = findViewById<TextView>(R.id.RegisterBtn)
        navigateToregButton.setOnClickListener {
            val emailEditText = findViewById<EditText>(R.id.email)
            val passwordEditText = findViewById<EditText>(R.id.password)
            val nameEditText = findViewById<EditText>(R.id.name)

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()

            // Validate email
            if (isValidEmail(email)) {
                // Store user data in shared preferences
                val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString(PREF_EMAIL, email)
                editor.putString(PREF_PASSWORD, password)
                editor.putString("name", name)
                editor.apply()

                // Navigate to login page
                val intent = Intent(this@Register, Login::class.java)
                startActivity(intent)
            } else {
                // Display error message for invalid email
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailPattern.matcher(email).matches()
    }
}