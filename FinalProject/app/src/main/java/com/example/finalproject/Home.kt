package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun navigateToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent)
    }

//    fun navigateToSearch(view: View) {
//        val intent = Intent(this, Search::class.java);
//        startActivity(intent)
//    }
}