package com.ropero.movielibrary.db

import com.ropero.movielibrary.model.Movie

fun MovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    year = year,
    genre = genre,
    rating = rating,
    watched = watched
)

fun Movie.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    year = year,
    genre = genre,
    rating = rating,
    watched = watched
)