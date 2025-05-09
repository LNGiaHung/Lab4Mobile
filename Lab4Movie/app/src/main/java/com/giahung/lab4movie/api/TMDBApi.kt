package com.giahung.lab4movie.api

import com.giahung.lab4movie.data.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieResponse
    
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponse
}

object ApiClient {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "419b6f6f6449395e754a303754a319cc" // Replace with your actual TMDB API key
} 