package com.example.medbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient:
            GoogleSignInClient

    private val googleSignInLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            val task =
                GoogleSignIn.getSignedInAccountFromIntent(
                    result.data
                )

            try {

                val account =
                    task.getResult(
                        ApiException::class.java
                    )

                firebaseAuthWithGoogle(
                    account.idToken!!
                )

            } catch (e: Exception) {

                Toast.makeText(
                    this,
                    "Google Sign-In Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val gso =
            GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )
                .requestIdToken(
                    getString(
                        R.string.default_web_client_id
                    )
                )
                .requestEmail()
                .build()

        googleSignInClient =
            GoogleSignIn.getClient(
                this,
                gso
            )

        val emailInput =
            findViewById<EditText>(R.id.emailInput)

        val passwordInput =
            findViewById<EditText>(R.id.passwordInput)

        val loginBtn =
            findViewById<Button>(R.id.loginBtn)

        val guestBtn =
            findViewById<Button>(R.id.guestBtn)

        val googleLoginBtn =
            findViewById<Button>(
                R.id.googleLoginBtn
            )

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

        // ANONYMOUS LOGIN
        guestBtn.setOnClickListener {

            auth.signInAnonymously()

                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Anonymous Login Successful",
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
                            "Anonymous Login Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // GOOGLE LOGIN
        googleLoginBtn.setOnClickListener {

            val signInIntent =
                googleSignInClient.signInIntent

            googleSignInLauncher.launch(
                signInIntent
            )
        }

        // OPEN REGISTER SCREEN
        goToRegisterBtn.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String
    ) {

        val credential =
            GoogleAuthProvider.getCredential(
                idToken,
                null
            )

        auth.signInWithCredential(
            credential
        )

            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Google Login Successful",
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
                        "Google Authentication Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}