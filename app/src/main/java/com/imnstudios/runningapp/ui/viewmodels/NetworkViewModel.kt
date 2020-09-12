package com.imnstudios.runningapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imnstudios.runningapp.db.Run
import com.imnstudios.runningapp.repositories.MainRepository
import com.imnstudios.runningapp.ui.fragments.RunFragment.Companion.collectionReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NetworkViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {


    fun syncDataToRoomFromFirestore() = viewModelScope.launch {

        val list: MutableList<Run> =
            collectionReference.get().await().toObjects(Run::class.java)

        for (i in list)
            mainRepository.insertRun(i)
    }

}