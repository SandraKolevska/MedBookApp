package com.example.medbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailInput = findViewById<EditText>(R.id.registerEmailInput)
        val passwordInput = findViewById<EditText>(R.id.registerPasswordInput)

        val registerBtn = findViewById<Button>(R.id.registerBtn)

        registerBtn.setOnClickListener {

            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {

                            Toast.makeText(
                                this,
                                getString(R.string.registration_successful),
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(
                                Intent(this, LoginActivity::class.java)
                            )

                            finish()

                        } else {

                            Toast.makeText(
                                this,
                                getString(R.string.registration_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            } else {

                Toast.makeText(
                    this,
                    getString(R.string.enter_email_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}