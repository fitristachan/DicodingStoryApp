package com.dicoding.dicodingstoryapp.stories.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.databinding.FragmentStoriesBinding
import com.dicoding.dicodingstoryapp.stories.activity.MainActivity
import com.dicoding.dicodingstoryapp.stories.adapter.StoriesAdapter
import com.dicoding.dicodingstoryapp.viewmodel.StoriesViewModel
import com.dicoding.dicodingstoryapp.viewmodel.StoriesViewModelFactory

class StoriesFragment : Fragment() {

    private lateinit var binding: FragmentStoriesBinding
    private val storiesViewModel by viewModels<StoriesViewModel> {
        StoriesViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoriesBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvStoriesList.layoutManager = layoutManager

        val adapter = StoriesAdapter()
        binding.rvStoriesList.adapter = adapter


        storiesViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        storiesViewModel.getStories().observe(viewLifecycleOwner) { stories ->
                adapter.submitList(stories.listStory)
        }

        storiesViewModel.getErrorMessage().observe(viewLifecycleOwner) {
            val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage(it as CharSequence?)
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        }

        storiesViewModel.getSuccessMessage().observe(viewLifecycleOwner) {
            val toast = Toast.makeText(
                requireContext(),
                it.toString(),
                Toast.LENGTH_LONG,
            )
            toast.show()
        }

        return binding.root
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}
