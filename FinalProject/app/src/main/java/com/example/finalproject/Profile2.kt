package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Profile2 : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth;
    lateinit var LogOut : Button;
    lateinit var Email : TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile2)

        auth = FirebaseAuth.getInstance()
        LogOut = findViewById(R.id.LogOutbutton)
        Email = findViewById(R.id.EmailDisplay)
        val currentUser = auth.currentUser
        if( currentUser == null){
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
            finish()

        }else{
            Email.setText(currentUser.email)
        }
        LogOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
            finish()
        }

    }
}