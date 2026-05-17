package com.example.medbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val guestBtn = findViewById<Button>(R.id.guestBtn)

        loginBtn.setOnClickListener {

            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            } else {

                Toast.makeText(
                    this,
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        guestBtn.setOnClickListener {

            Toast.makeText(this, "Guest Login", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}