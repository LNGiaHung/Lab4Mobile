package com.giahung.lab4movie.repository

import com.giahung.lab4movie.api.ApiClient
import com.giahung.lab4movie.api.TMDBApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepository {
    private val api: TMDBApi by lazy {
        Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBApi::class.java)
    }
    
    suspend fun searchMovies(query: String, page: Int = 1) = 
        api.searchMovies(ApiClient.API_KEY, query, page)
    
    suspend fun getPopularMovies(page: Int = 1) = 
        api.getPopularMovies(ApiClient.API_KEY, page)
} 