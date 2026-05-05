package com.ropero.movielibrary.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ropero.movielibrary.db.AppDatabase
import com.ropero.movielibrary.databinding.FragmentMovieListBinding
import com.ropero.movielibrary.repository.MovieRepository
import com.ropero.movielibrary.viewmodel.MovieViewModel
import com.ropero.movielibrary.viewmodel.MovieViewModelFactory

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by activityViewModels {
        val db = AppDatabase.getInstance(requireContext())
        MovieViewModelFactory(MovieRepository(db.movieDao()))
    }

    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter { movie ->
            val action = MovieListFragmentDirections.actionListToDetail(movie.id)
            findNavController().navigate(action)
        }

        binding.rvMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMovies.adapter = adapter

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
            if (movies.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvMovies.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.rvMovies.visibility = View.VISIBLE
            }
        }

        binding.fabAdd.setOnClickListener {
            val action = MovieListFragmentDirections.actionListToEdit(-1)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}