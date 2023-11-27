package com.example.finalproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView

class Mv5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mv5)

        //Trailer
        val videoView = findViewById<VideoView>(R.id.Mv5)
        val packageName = "android.resource://" + getPackageName() + "/" + R.raw.thedarkknight
        val uri = Uri.parse(packageName)
        videoView.setVideoURI(uri)

        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
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
}