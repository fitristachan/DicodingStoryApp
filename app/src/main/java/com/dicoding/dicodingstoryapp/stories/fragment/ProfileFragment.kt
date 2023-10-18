package com.dicoding.dicodingstoryapp.stories.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.auth.RegisterActivity
import com.dicoding.dicodingstoryapp.databinding.FragmentProfileBinding
import com.dicoding.dicodingstoryapp.datastore.UserPreference
import com.dicoding.dicodingstoryapp.datastore.dataStore
import com.dicoding.dicodingstoryapp.viewmodel.UserPreferenceViewModel
import com.dicoding.dicodingstoryapp.viewmodel.UserPreferenceViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val pref = UserPreference.getInstance(requireContext().dataStore)
        val userPreferenceViewModel: UserPreferenceViewModel by viewModels {
            UserPreferenceViewModelFactory(pref)
        }

        val hi = resources.getString(R.string.hi_user)
        userPreferenceViewModel.getName().observe(viewLifecycleOwner) { name: String? ->
            binding.txtHi.text = "$hi $name"
        }

        binding.actionLogout.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())

            alertDialogBuilder.setMessage(getString(R.string.logout_conf))
            alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                userPreferenceViewModel.deleteAll()
                val intent = Intent(requireContext(), RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                dialog.dismiss()
            }

            alertDialogBuilder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }

        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        return binding.root
    }

}