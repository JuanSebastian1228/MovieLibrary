package com.ropero.movielibrary.viewmodel

import androidx.lifecycle.*
import com.ropero.movielibrary.model.Movie
import com.ropero.movielibrary.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    val movies: LiveData<List<Movie>> = repository.getMovies()

    private val _selectedMovieId = MutableLiveData<Int>()
    val selectedMovie: LiveData<Movie?> = _selectedMovieId.switchMap { id ->
        repository.getMovie(id)
    }

    fun selectMovie(id: Int) {
        _selectedMovieId.value = id
    }

    fun insertMovie(movie: Movie) {
        viewModelScope.launch {
            repository.insert(movie)
        }
    }

    fun updateMovie(movie: Movie) {
        viewModelScope.launch {
            repository.update(movie)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            repository.delete(movie)
        }
    }

    fun toggleWatched(movie: Movie) {
        viewModelScope.launch {
            repository.update(movie.copy(watched = !movie.watched))
        }
    }
}