package com.imnstudios.runningapp.other

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}