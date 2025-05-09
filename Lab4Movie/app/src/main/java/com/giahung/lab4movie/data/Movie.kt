package com.giahung.lab4movie.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Movie(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("overview")
    val overview: String,
    
    @SerializedName("poster_path")
    val posterPath: String?,
    
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    
    @SerializedName("release_date")
    val releaseDate: String,
    
    @SerializedName("vote_average")
    val voteAverage: Double
) : Serializable {
    val posterUrl: String
        get() = "https://image.tmdb.org/t/p/w500$posterPath"
        
    val backdropUrl: String
        get() = "https://image.tmdb.org/t/p/original$backdropPath"
}

data class MovieResponse(
    @SerializedName("results")
    val results: List<Movie>,
    
    @SerializedName("total_pages")
    val totalPages: Int,
    
    @SerializedName("total_results")
    val totalResults: Int
) 