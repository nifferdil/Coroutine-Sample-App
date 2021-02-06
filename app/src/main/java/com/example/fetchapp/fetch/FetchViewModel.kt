package com.example.fetchapp.fetch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class FetchViewModel(private val apiService: Service) : ViewModel() {

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "Something went wrong, please try again later"
    }

    fun getData() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiService.getData()
                .filterNot { it.name.isNullOrBlank() }
                .sortedWith(compareBy({ it.listId }, { it.name }))
            ))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: DEFAULT_ERROR_MESSAGE))
        }
    }
}