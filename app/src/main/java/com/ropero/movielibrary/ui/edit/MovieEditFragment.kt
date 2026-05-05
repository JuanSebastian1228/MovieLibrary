package com.ropero.movielibrary.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ropero.movielibrary.db.AppDatabase
import com.ropero.movielibrary.databinding.FragmentMovieEditBinding
import com.ropero.movielibrary.model.Movie
import com.ropero.movielibrary.repository.MovieRepository
import com.ropero.movielibrary.viewmodel.MovieViewModel
import com.ropero.movielibrary.viewmodel.MovieViewModelFactory

class MovieEditFragment : Fragment() {

    private var _binding: FragmentMovieEditBinding? = null
    private val binding get() = _binding!!

    private val args: MovieEditFragmentArgs by navArgs()

    private val viewModel: MovieViewModel by activityViewModels {
        val db = AppDatabase.getInstance(requireContext())
        MovieViewModelFactory(MovieRepository(db.movieDao()))
    }

    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId
        isEditMode = movieId != -1

        if (isEditMode) {
            viewModel.selectMovie(movieId)
            viewModel.selectedMovie.observe(viewLifecycleOwner) { movie ->
                movie ?: return@observe
                binding.etTitle.setText(movie.title)
                binding.etYear.setText(movie.year.toString())
                binding.etGenre.setText(movie.genre)
                binding.etRating.setText(movie.rating.toString())
                binding.switchWatched.isChecked = movie.watched
            }
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val yearStr = binding.etYear.text.toString().trim()
            val genre = binding.etGenre.text.toString().trim()
            val ratingStr = binding.etRating.text.toString().trim()
            val watched = binding.switchWatched.isChecked

            if (title.isEmpty() || yearStr.isEmpty() || genre.isEmpty() || ratingStr.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val year = yearStr.toIntOrNull() ?: run {
                Toast.makeText(requireContext(), "Año inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rating = ratingStr.toFloatOrNull() ?: run {
                Toast.makeText(requireContext(), "Rating inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) {
                viewModel.updateMovie(
                    Movie(
                        id = movieId,
                        title = title,
                        year = year,
                        genre = genre,
                        rating = rating,
                        watched = watched
                    )
                )
                Toast.makeText(requireContext(), "Película actualizada", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertMovie(
                    Movie(
                        title = title,
                        year = year,
                        genre = genre,
                        rating = rating,
                        watched = watched
                    )
                )
                Toast.makeText(requireContext(), "Película agregada", Toast.LENGTH_SHORT).show()
            }

            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}