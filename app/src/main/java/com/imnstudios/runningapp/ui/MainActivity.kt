package com.imnstudios.runningapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imnstudios.runningapp.R
import com.imnstudios.runningapp.db.RunDao
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var runDao: RunDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}