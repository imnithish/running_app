package com.imnstudios.runningapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.imnstudios.runningapp.R
import com.imnstudios.runningapp.other.Constants.RC_SIGN_IN
import com.imnstudios.runningapp.other.hide
import com.imnstudios.runningapp.other.show
import com.imnstudios.runningapp.other.snackbar
import com.imnstudios.runningapp.ui.MainActivity.Companion.auth
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber

class LogInFragment : Fragment(R.layout.fragment_login) {

    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        login_button.setOnClickListener {
            login()
        }

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null)
            findNavController().navigate(R.id.action_logInFragment_to_runFragment)
        else
            login_button.show()

    }

    private fun login() {
        progress_bar.show()
        login_button.hide()
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                base_layout.snackbar("Something is Wrong $e")
                progress_bar.hide()
                login_button.show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Timber.d("firebaseAuthWithGoogle: + ${acct.id!!}")
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(
                requireActivity()
            ) { task ->
                if (task.isSuccessful) {
                    Timber.d("signInWithCredential:success")

                    findNavController().navigate(R.id.action_logInFragment_to_setupFragment)

                } else {
                    Timber.d("signInWithCredential:failure ${task.exception}")
                    base_layout.snackbar("Authentication failed ${task.exception.toString()}")
                    progress_bar.hide()
                    login_button.show()
                }
            }
    }

}