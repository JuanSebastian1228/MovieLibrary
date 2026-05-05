package com.ropero.movielibrary.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ropero.movielibrary.db.AppDatabase
import com.ropero.movielibrary.databinding.FragmentMovieDetailBinding
import com.ropero.movielibrary.repository.MovieRepository
import com.ropero.movielibrary.viewmodel.MovieViewModel
import com.ropero.movielibrary.viewmodel.MovieViewModelFactory

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()

    private val viewModel: MovieViewModel by activityViewModels {
        val db = AppDatabase.getInstance(requireContext())
        MovieViewModelFactory(MovieRepository(db.movieDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId
        viewModel.selectMovie(movieId)

        viewModel.selectedMovie.observe(viewLifecycleOwner) { movie ->
            movie ?: return@observe
            binding.tvDetailTitle.text = movie.title
            binding.tvDetailYear.text = "Año: ${movie.year}"
            binding.tvDetailGenre.text = "Género: ${movie.genre}"
            binding.tvDetailRating.text = "Rating: ${movie.rating} / 10"
            binding.tvDetailWatched.text =
                if (movie.watched) "Estado:Vista" else "Estado: 🎬 Pendiente"
            binding.btnToggleWatched.text =
                if (movie.watched) "Marcar como NO vista" else "Marcar como vista"

            binding.btnToggleWatched.setOnClickListener {
                viewModel.toggleWatched(movie)
            }

            binding.btnEdit.setOnClickListener {
                val action = MovieDetailFragmentDirections.actionDetailToEdit(movie.id)
                findNavController().navigate(action)
            }

            binding.btnDelete.setOnClickListener {
                viewModel.deleteMovie(movie)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}