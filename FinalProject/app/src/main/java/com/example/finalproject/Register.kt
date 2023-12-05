package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.MotionEffect.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.*

class Register : AppCompatActivity() {

    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var editUsername: EditText
    lateinit var regBut: Button
    lateinit var ProfIcon: Button
    private lateinit var auth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    lateinit var BackBut: ImageButton
    private var justRegistered = false
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null && !justRegistered) {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
        justRegistered = false // Reset the flag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        usersRef = database.reference.child("users")

        editEmail = findViewById(R.id.edtEmailR)
        editUsername = findViewById(R.id.editUsernameR)
        editPassword = findViewById(R.id.edtPasswordR)
        regBut = findViewById(R.id.btnRegisterR)
        progressBar = findViewById(R.id.progressBar)
        BackBut = findViewById(R.id.BackButton)

        BackBut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        regBut.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val username = editUsername.text.toString()

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Register, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Register, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username.isEmpty()) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Register, "Enter username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if the username is already taken
            checkUsernameAvailability(username, email, password)
        }
    }

    private fun checkUsernameAvailability(username: String, email: String, password: String) {
        usersRef.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Username is already taken
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@Register, "Username is already taken", Toast.LENGTH_SHORT).show()
                    } else {
                        // Username is available, proceed with registration
                        registerUser(email, password, username)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@Register, "Error checking username availability", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun registerUser(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext,
                        "Account Created.",
                        Toast.LENGTH_SHORT,
                    ).show()

                    // Save the username in the database
                    saveUsernameInDatabase(username)

                    justRegistered = true // Set the flag to true after successful registration
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    handleRegistrationFailure(task.exception)
                }
            }
    }

    private fun saveUsernameInDatabase(username: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = usersRef.child(userId)
            userRef.child("username").setValue(username)
        }
    }

    private fun handleRegistrationFailure(exception: Exception?) {
        when (exception) {
            is FirebaseAuthUserCollisionException -> {
                Toast.makeText(
                    baseContext,
                    "Email is already in use. Please choose a different email.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is FirebaseAuthInvalidCredentialsException -> {
                val errorCode = (exception as FirebaseAuthInvalidCredentialsException).errorCode
                if (errorCode == "ERROR_WEAK_PASSWORD") {
                    Toast.makeText(
                        baseContext,
                        "Weak password. Please choose a stronger password.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Invalid email format. Please use a valid email address.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> {
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
