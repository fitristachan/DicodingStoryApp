package com.dicoding.dicodingstoryapp.stories.fragment

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.databinding.FragmentAddStoryBinding
import com.dicoding.dicodingstoryapp.stories.activity.MainActivity
import com.dicoding.dicodingstoryapp.stories.utils.getImageUri
import com.dicoding.dicodingstoryapp.stories.utils.reduceFileImage
import com.dicoding.dicodingstoryapp.stories.utils.uriToFile
import com.dicoding.dicodingstoryapp.viewmodel.StoriesViewModel
import com.dicoding.dicodingstoryapp.viewmodel.StoriesViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryFragment : Fragment() {

    private lateinit var binding: FragmentAddStoryBinding
    private lateinit var requestBody: RequestBody
    private lateinit var multipartBody: MultipartBody.Part
    private var currentImageUri: Uri? = null
    private val storiesViewModel by viewModels<StoriesViewModel> {
        StoriesViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStoryBinding.inflate(inflater, container, false)

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        storiesViewModel.isLoading.observe(requireActivity()) { isLoading ->
            showLoading(isLoading)
        }

        binding.buttonAdd.setOnClickListener {
            val description = binding.edAddDescription.text.toString()

            if (currentImageUri == null && description.isEmpty()) {
                showPositiveAlert(getString(R.string.add_desc_photo_warning), "Ok")
            } else if (description.isEmpty()) {
                showPositiveAlert(getString(R.string.add_desc_warning), "Ok")
            } else if (currentImageUri == null) {
                showPositiveAlert(getString(R.string.empty_image_warning), "Ok")
            } else {
                currentImageUri?.let {
                    val imageFile = uriToFile(it, requireContext()).reduceFileImage()

                    requestBody = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    multipartBody = MultipartBody.Part.createFormData(
                        "photo", imageFile.name, requestImageFile
                    )
                }

                storiesViewModel.addStory(requestBody, multipartBody).observe(requireActivity()) {}

                storiesViewModel.getSuccessMessage().observe(requireActivity()) { successMessage ->
                    if (successMessage.isNotEmpty()) {
                        val alertDialogBuilder =
                            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        alertDialogBuilder.setMessage(successMessage as CharSequence?)
                        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        val alertDialog: androidx.appcompat.app.AlertDialog =
                            alertDialogBuilder.create()
                        alertDialog.show()
                    }
                }

                storiesViewModel.getErrorMessage().observe(requireActivity()) { errorMessage ->
                    if (errorMessage.isNotEmpty()) {
                        showPositiveAlert(errorMessage.toString(), "OK")
                    }
                }
            }
        }

        return binding.root
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            val toast = Toast.makeText(
                requireContext(),
                getString(R.string.no_media),
                Toast.LENGTH_LONG,
            )
            toast.show()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.imgPhoto.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showPositiveAlert(message: String, positiveButton: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(positiveButton) { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}