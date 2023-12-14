package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
class Home : AppCompatActivity() {

    private val images = listOf(
        R.drawable.air,
        R.drawable.barbie,
        R.drawable.dream,
        R.drawable.extraction2,
        R.drawable.insidiousmoreq,
        R.drawable.johnwick,
        R.drawable.joker,
        R.drawable.oppenheimer,
        R.drawable.sausageparty,
        R.drawable.thedarkknight,
        R.drawable.thedictator,
        R.drawable.thelastofus,
        R.drawable.thepurge,
        R.drawable.theupside


    )

    private lateinit var viewPager: ViewPager
    private lateinit var imageAdapter: ImageAdapter

    private val handler = Handler()
    private val delay: Long = 3000
    private var page = 0

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (page == images.size) {
                page = 0
            } else {
                page++
            }
            viewPager.setCurrentItem(page, true)
            handler.postDelayed(this, delay)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewPager = findViewById(R.id.viewPager)
        imageAdapter = ImageAdapter(this, images)
        viewPager.adapter = imageAdapter


        handler.postDelayed(timerRunnable, delay)
    }
    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacks(timerRunnable)
    }

    fun navigateToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent)
    }

    //Movie
    fun navigateToMv1(view: View) {
        Log.d("HomeActivity", "Navigating to Mv1")
        val intent = Intent(this, Mv1::class.java)
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
    fun navigateToMv10(view: View) {
        val intent = Intent(this, Mv10::class.java);
        startActivity(intent)
    }
    fun navigateToMv11(view: View) {
        val intent = Intent(this, Mv11::class.java);
        startActivity(intent)
    }
    fun navigateToMv12(view: View) {
        val intent = Intent(this, Mv12::class.java);
        startActivity(intent)
    }
    fun navigateToMv13(view: View) {
        val intent = Intent(this, Mv13::class.java);
        startActivity(intent)
    }
    fun navigateToMv14(view: View) {
        val intent = Intent(this, Mv14::class.java);
        startActivity(intent)
    }
    fun navigateToMv15(view: View) {
        val intent = Intent(this, Mv15::class.java);
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
}