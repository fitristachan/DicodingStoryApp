package com.dicoding.dicodingstoryapp.stories.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.databinding.FragmentStoriesBinding
import com.dicoding.dicodingstoryapp.stories.adapter.LoadingStateAdapter
import com.dicoding.dicodingstoryapp.stories.adapter.StoriesPagingAdapter
import com.dicoding.dicodingstoryapp.viewmodel.StoriesPagingViewModel
import com.dicoding.dicodingstoryapp.viewmodel.StoriesPagingViewModelFactory

class StoriesPagingFragment : Fragment() {

    private lateinit var binding: FragmentStoriesBinding
    private val storiesPagingViewModel: StoriesPagingViewModel by viewModels {
        StoriesPagingViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoriesBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvStoriesList.layoutManager = layoutManager

        val adapter = StoriesPagingAdapter()
        binding.rvStoriesList.adapter = adapter.withLoadStateFooter(footer = LoadingStateAdapter {
            adapter.retry()
        })

        adapter.addLoadStateListener { loadState ->
            bind(loadState)
        }

        storiesPagingViewModel.getStories.observe(viewLifecycleOwner) { pagingData ->
            if (pagingData != null) {
                adapter.submitData(lifecycle, pagingData)
            } else {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setMessage(getString(R.string.error_title))
                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }

        return binding.root
    }


    private fun bind(combinedLoadStates: CombinedLoadStates) {
        val loadState = combinedLoadStates.source.append
        if (loadState is LoadState.Error) {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage(loadState.error.localizedMessage)
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading


    }
}
