package com.example.fetchapp.fetch.api

import retrofit2.http.GET

interface Service {

    @GET("hiring.json")
    suspend fun getData(): List<Item>
}