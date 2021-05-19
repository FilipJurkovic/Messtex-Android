package com.messtex.ui.fragments.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.messtex.data.models.localdb.User
import com.messtex.data.repositories.mainRepository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StepUserInfoViewModel(private val repository: MainRepository) : ViewModel() {

    val userData = MutableLiveData<User>()

     fun onSubmitted(user: User) {
         viewModelScope.launch(Dispatchers.IO)
         {
             if (true) {
                 repository.appendUser(user)
             } else {

                 repository.updateUser(user)
             }
         }

    }

}