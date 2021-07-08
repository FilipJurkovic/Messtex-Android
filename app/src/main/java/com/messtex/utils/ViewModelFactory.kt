package com.messtex.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.messtex.data.repositories.mainRepository.MainRepository
import com.messtex.ui.main.viewmodel.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}