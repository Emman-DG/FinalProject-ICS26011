package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.helper.widget.MotionEffect.TAG
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var editEmail : EditText;
    lateinit var editPassword : EditText;
    private lateinit var auth: FirebaseAuth;
    lateinit var progressBar : ProgressBar;
    lateinit var regBut : Button;
    lateinit var LoginBut : Button;

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, Home::class.java);
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        auth = Firebase.auth
        editEmail = findViewById(R.id.edtEmail)
        editPassword = findViewById(R.id.edtPassword)
        regBut = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.progressBar2)
        LoginBut = findViewById(R.id.btnLogin)

        regBut.setOnClickListener{
            val intent = Intent(this, Register::class.java);
            startActivity(intent)
            finish()
        }
        LoginBut.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            if (email.isEmpty()) {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "Login Succesful",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val userId = auth.currentUser?.uid
                        val database = FirebaseDatabase.getInstance()
                        val userRef = database.reference.child("users").child(userId ?: "")
                        userRef.child("email").setValue(email)

                        val intent = Intent(this, Profile::class.java);
                        startActivity(intent)
                        finish()


                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        Log.e(TAG, "whyCantLogin")

                    }
                }
        }
    }


    /**fun navigateToHome(view: View) {
        val intent = Intent(this, Home::class.java);
        startActivity(intent)
    }**/
}