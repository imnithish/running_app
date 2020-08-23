package com.imnstudios.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.imnstudios.runningapp.R
import com.imnstudios.runningapp.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.imnstudios.runningapp.other.Constants.KEY_NAME
import com.imnstudios.runningapp.other.Constants.KEY_WEIGHT
import com.imnstudios.runningapp.ui.MainActivity.Companion.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var sharedPref: SharedPreferences


    private lateinit var userName: String

//    @set:Inject
//    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if(!isFirstAppOpen) {
//            val navOptions = NavOptions.Builder()
//                .setPopUpTo(R.id.setupFragment, true)
//                .build()
//            findNavController().navigate(
//                R.id.action_setupFragment_to_runFragment,
//                savedInstanceState,
//                navOptions
//            )
//        }

        userName = auth.currentUser?.displayName.toString()

        val personalizedText = "Welcome!\n$userName"
        tvWelcome.text = personalizedText

        tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if (success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {

        val weight = etWeight.text.toString()
        if (weight.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, userName)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()
//        val toolbarText = "Let's go, $name!"
//        requireActivity().tvToolbarTitle.text = toolbarText
        return true
    }
}