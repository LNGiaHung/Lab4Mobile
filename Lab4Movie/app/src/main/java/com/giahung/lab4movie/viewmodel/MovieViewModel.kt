package com.giahung.lab4movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giahung.lab4movie.data.Movie
import com.giahung.lab4movie.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()
    
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.searchMovies(query)
                _movies.value = response.results
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun loadPopularMovies() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.getPopularMovies()
                _movies.value = response.results
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _loading.value = false
            }
        }
    }
} 