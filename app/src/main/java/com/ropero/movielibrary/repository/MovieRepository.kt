package com.ropero.movielibrary.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.ropero.movielibrary.db.MovieDao
import com.ropero.movielibrary.db.toDomain
import com.ropero.movielibrary.db.toEntity
import com.ropero.movielibrary.model.Movie

class MovieRepository(private val dao: MovieDao) {

    fun getMovies(): LiveData<List<Movie>> {
        return dao.getAllMovies().map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getMovie(id: Int): LiveData<Movie?> {
        return dao.getMovieById(id).map { entity ->
            entity?.toDomain()
        }
    }

    suspend fun insert(movie: Movie) {
        dao.insert(movie.toEntity())
    }

    suspend fun update(movie: Movie) {
        dao.update(movie.toEntity())
    }

    suspend fun delete(movie: Movie) {
        dao.delete(movie.toEntity())
    }
}