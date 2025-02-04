package com.volu.volunteerconnect.Ui.Register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volu.volunteerconnect.R
import com.volu.volunteerconnect.Utils.Constants.ORGANIZATION
import com.volu.volunteerconnect.Utils.Constants.VOLUNTEER
import com.volu.volunteerconnect.databinding.FragmentRegisterAsBinding

class RegisterAsFragment : Fragment(R.layout.fragment_register_as) {

    private lateinit var binding: FragmentRegisterAsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterAsBinding.bind(view)

        binding.tvNoAcc.setOnClickListener {
            findNavController().navigate(RegisterAsFragmentDirections.actionRegisterAsFragmentToLoginFragment())
        }

        binding.btnRegisterUser.setOnClickListener {
            findNavController().navigate(
                RegisterAsFragmentDirections.actionRegisterAsFragmentToRegisterFragment(
                    VOLUNTEER
                )
            )
            findNavController().clearBackStack(R.id.registerAsFragment)
        }

        binding.btnRegisterOrganization.setOnClickListener {
            findNavController().navigate(
                RegisterAsFragmentDirections.actionRegisterAsFragmentToRegisterFragment(
                    ORGANIZATION
                )
            )
            findNavController().clearBackStack(R.id.registerAsFragment)
        }
    }
}