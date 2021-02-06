package com.example.fetchapp.fetch

import retrofit2.http.GET

interface Service {

    @GET("hiring.json")
    suspend fun getData(): List<Item>
}