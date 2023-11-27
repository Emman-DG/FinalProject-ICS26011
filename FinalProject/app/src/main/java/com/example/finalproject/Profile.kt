package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    //Main
    fun navigateToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent)
    }
    //Home
    fun navigateToHome(view: View) {
        val intent = Intent(this, Home::class.java);
        startActivity(intent)
    }
    //Search
    fun navigateToSearch(view: View) {
        val intent = Intent(this, Search::class.java);
        startActivity(intent)
    }
    //Profile
    fun navigateToProfile(view: View) {
        val intent = Intent(this, Profile::class.java);
        startActivity(intent)
    }

    //Movie
    fun navigateToMv1(view: View) {
        val intent = Intent(this, Mv1::class.java);
        startActivity(intent)
    }
    fun navigateToMv2(view: View) {
        val intent = Intent(this, Mv2::class.java);
        startActivity(intent)
    }
    fun navigateToMv3(view: View) {
        val intent = Intent(this, Mv3::class.java);
        startActivity(intent)
    }
    fun navigateToMv4(view: View) {
        val intent = Intent(this, Mv4::class.java);
        startActivity(intent)
    }
    fun navigateToMv5(view: View) {
        val intent = Intent(this, Mv5::class.java);
        startActivity(intent)
    }
    fun navigateToMv6(view: View) {
        val intent = Intent(this, Mv6::class.java);
        startActivity(intent)
    }
    fun navigateToMv7(view: View) {
        val intent = Intent(this, Mv7::class.java);
        startActivity(intent)
    }
    fun navigateToMv8(view: View) {
        val intent = Intent(this, Mv8::class.java);
        startActivity(intent)
    }
    fun navigateToMv9(view: View) {
        val intent = Intent(this, Mv9::class.java);
        startActivity(intent)
    }
}