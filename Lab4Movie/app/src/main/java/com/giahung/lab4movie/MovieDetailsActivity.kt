package com.giahung.lab4movie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.giahung.lab4movie.data.Movie
import com.giahung.lab4movie.databinding.ActivityMovieDetailsBinding

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButton()
        
        val movie = intent.getSerializableExtra("movie") as? Movie
        movie?.let { displayMovieDetails(it) }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun displayMovieDetails(movie: Movie) {
        binding.apply {
            movieTitle.text = movie.title
            movieReleaseDate.text = "Release Date: ${movie.releaseDate}"
            movieRating.text = "Rating: ${movie.voteAverage}/10"
            movieOverview.text = movie.overview

            Glide.with(this@MovieDetailsActivity)
                .load(movie.posterUrl)
                .into(moviePoster)

            Glide.with(this@MovieDetailsActivity)
                .load(movie.backdropUrl)
                .into(movieBackdrop)
        }
    }
} 