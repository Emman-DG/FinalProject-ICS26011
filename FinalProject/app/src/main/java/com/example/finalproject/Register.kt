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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlin.system.exitProcess

class Register : AppCompatActivity() {

    lateinit var editEmail : EditText;
    lateinit var editPassword : EditText;
    lateinit var editUsername : EditText;
    lateinit var regBut : Button;
    lateinit var ProfIcon : Button;
    private lateinit var auth: FirebaseAuth;
    lateinit var progressBar : ProgressBar;
    lateinit var BackBut : ImageButton;

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, Home::class.java);
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        editEmail = findViewById(R.id.edtEmailR)
        editUsername = findViewById(R.id.editUsernameR)
        editPassword = findViewById(R.id.edtPasswordR)
        regBut = findViewById(R.id.btnRegisterR)
        ProfIcon = findViewById(R.id.btnBrowse)
        progressBar = findViewById(R.id.progressBar)
        BackBut = findViewById(R.id.BackButton)

        BackBut.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



        regBut.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            if (email.isEmpty()) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Register, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Register, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(
                            baseContext,
                            "Account Created",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }

    }
}