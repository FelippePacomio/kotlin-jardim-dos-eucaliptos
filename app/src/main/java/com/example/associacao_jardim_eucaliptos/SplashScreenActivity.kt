package com.example.associacao_jardim_eucaliptos

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        // Hide the action bar
        supportActionBar?.hide()

        // Hide the navigation bar
        hideBottomNavigationBar()

        // Delay for 3 seconds and then start the MainActivity
        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun hideBottomNavigationBar() {
        // Check if the device is running Android 11 or later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = window?.insetsController
            windowInsetsController?.hide(WindowInsets.Type.navigationBars())
        } else {
            // For devices running earlier versions of Android
            window.decorView.apply {
                systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
            }
        }
    }
}
