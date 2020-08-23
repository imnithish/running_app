package com.imnstudios.runningapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.imnstudios.runningapp.R
import com.imnstudios.runningapp.db.RunDao
import com.imnstudios.runningapp.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var auth: FirebaseAuth
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)

        navigateToTrackingFragmentIfNeeded(intent)

        setSupportActionBar(toolbar)
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.settingsFragment, R.id.runFragment, R.id.statisticsFragment -> {
                        bottomNavigationView.visibility = View.VISIBLE
                        appBarLayout.visibility = View.VISIBLE
                    }
                    else -> {
                        bottomNavigationView.visibility = View.GONE
                        appBarLayout.visibility = View.GONE
                    }
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
        print("hello world")
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }
}