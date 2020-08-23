package com.imnstudios.runningapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            overridePendingTransition(
                0, 0
            )
            finish()
        }
    }
}