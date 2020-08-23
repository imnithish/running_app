package com.imnstudios.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.imnstudios.runningapp.R
import com.imnstudios.runningapp.other.Constants
import com.imnstudios.runningapp.other.Constants.KEY_NAME
import com.imnstudios.runningapp.other.Constants.KEY_WEIGHT
import com.imnstudios.runningapp.ui.MainActivity
import com.imnstudios.runningapp.ui.MainActivity.Companion.auth
import com.imnstudios.runningapp.ui.MainActivity.Companion.firestoreDb
import com.imnstudios.runningapp.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbarText = "Settings"
        requireActivity().tvToolbarTitle.text = toolbarText

        loadFieldsFromSharedPref()
        btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()
            if (success) {
                Snackbar.make(view, "Saved changes", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(view, "Please fill out all the fields", Snackbar.LENGTH_LONG).show()
            }
        }

        btnLogOut.setOnClickListener {
            logOut()

        }

        btnSync.setOnClickListener {
            syncToFirebase()
        }
    }

    private fun syncToFirebase() {
        if (auth.currentUser != null) {

            viewModel.runsSortedByDate.observe(viewLifecycleOwner, Observer {
                it?.let {

                    for (i in it) {
                        firestoreDb.collection("Users")
                            .document(auth.currentUser?.uid.toString())
                            .collection("Runs")
                            .document(i.id.toString())
                            .set(i)

                    }

                }
            })


        } else {
            Snackbar.make(base_layout, "Something's wrong", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun logOut() {
        auth.signOut()
        sharedPreferences.edit()
            .putBoolean(Constants.KEY_FIRST_TIME_TOGGLE, true)
            .apply()
        findNavController().navigate(R.id.action_settingsFragment_to_logInFragment)
    }


    private fun loadFieldsFromSharedPref() {
//        val name = sharedPreferences.getString(KEY_NAME, "")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT, 80f)
//        etName.setText(name)
        etWeight.setText(weight.toInt().toString())
    }

    private fun applyChangesToSharedPref(): Boolean {
//    val nameText = etName.text.toString()
        val weightText = etWeight.text.toString()
        if (weightText.isEmpty()) {
            return false
        }
        sharedPreferences.edit()
//        .putString(KEY_NAME, nameText)
            .putFloat(KEY_WEIGHT, weightText.toFloat())
            .apply()
//    val toolbarText = "Let's go $nameText"
//    requireActivity().tvToolbarTitle.text = toolbarText
        return true
    }
}