package com.example.fetchapp.fetch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val apiService: Service) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(FetchViewModel::class.java)) {
            return FetchViewModel(apiService) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}