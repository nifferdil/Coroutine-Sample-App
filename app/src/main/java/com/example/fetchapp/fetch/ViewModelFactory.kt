package com.example.fetchapp.fetch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fetchapp.fetch.api.Service

class ViewModelFactory(private val apiService: Service) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(apiService) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}