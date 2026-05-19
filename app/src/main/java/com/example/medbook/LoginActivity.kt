package com.example.medbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailInput =
            findViewById<EditText>(R.id.emailInput)

        val passwordInput =
            findViewById<EditText>(R.id.passwordInput)

        val loginBtn =
            findViewById<Button>(R.id.loginBtn)

        val guestBtn =
            findViewById<Button>(R.id.guestBtn)

        val goToRegisterBtn =
            findViewById<Button>(R.id.goToRegisterBtn)

        // EMAIL LOGIN
        loginBtn.setOnClickListener {

            val email =
                emailInput.text.toString().trim()

            val password =
                passwordInput.text.toString().trim()

            if (
                email.isNotEmpty() &&
                password.isNotEmpty()
            ) {

                auth.signInWithEmailAndPassword(
                    email,
                    password
                )

                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {

                            Toast.makeText(
                                this,
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(
                                Intent(
                                    this,
                                    MainActivity::class.java
                                )
                            )

                            finish()

                        } else {

                            Toast.makeText(
                                this,
                                "Authentication failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            } else {

                Toast.makeText(
                    this,
                    "Enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // GUEST MODE
        guestBtn.setOnClickListener {

            auth.signOut()

            Toast.makeText(
                this,
                "Guest Mode",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(
                Intent(this, MainActivity::class.java)
            )

            finish()
        }

        // OPEN REGISTER SCREEN
        goToRegisterBtn.setOnClickListener {

            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
    }
}