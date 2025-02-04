package com.volu.volunteerconnect.Ui.Register

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.volu.volunteerconnect.Model.Auth.RegisterData
import com.volu.volunteerconnect.R
import com.volu.volunteerconnect.Utils.Constants.ORGANIZATION
import com.volu.volunteerconnect.Utils.LoadingInterface
import com.volu.volunteerconnect.Utils.ResponseState
import com.volu.volunteerconnect.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.ClassCastException
import java.util.Locale

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()
    private val args by navArgs<RegisterFragmentArgs>()
    private var listener: LoadingInterface ?= null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        if(args.role == ORGANIZATION) {
            binding.tvWelcome.text = context?.getString(R.string.sign_up_organization)
        } else {
            binding.tvWelcome.text = context?.getString(R.string.sign_up)
        }

        registerStatus()

        binding.btnRegister.setOnClickListener {
            if (validateUsername() && validateName() && validateEmail() && validatePassword() && validateMobile()) {
                val registerData = RegisterData(
                    binding.tilUsername.editText?.text.toString(),
                    binding.tilName.editText?.text.toString(),
                    binding.tilEmail.editText?.text.toString(),
                    binding.tilPass.editText?.text.toString(),
                    if (args.role == ORGANIZATION) "organization" else "volunteer",
                    binding.tilMobile.editText?.text.toString(),
                )

                registerViewModel.register(registerData)
                showLoading()
            }
        }

        binding.tvNoAcc.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }

    private fun validateUsername(): Boolean {
        return if (binding.tilUsername.editText?.text.isNullOrEmpty()) {
            binding.tilUsername.error = "Field cannot be Empty"
            false
        } else {
            binding.tilUsername.isErrorEnabled = false
            true
        }
    }

    private fun validateName(): Boolean {
        return if (binding.tilName.editText?.text.isNullOrEmpty()) {
            binding.tilName.error = "Fields cannot be Empty"
            false
        } else {
            binding.tilName.isErrorEnabled = false
            true
        }
    }

    private fun validateEmail(): Boolean {
        val isValid: Boolean
        if (binding.tilEmail.editText?.text.isNullOrEmpty()) {
            binding.tilEmail.error = "Field cannot be Empty"
            isValid = false
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.tilEmail.editText?.text.toString()).matches()) {
                binding.tilEmail.error = "Enter a Valid Email"
                isValid = false
            } else {
                binding.tilEmail.isErrorEnabled = false
                isValid = true
            }
        }
        return isValid
    }

    private fun validatePassword(): Boolean {
        return if (binding.tilPass.editText?.text.isNullOrEmpty()) {
            binding.tilPass.error = "Field cannot be Empty"
            false
        } else {
            if(binding.tilPass.editText?.text.toString().length < 6) {
                binding.tilPass.error = context?.getString(R.string.password_criteria)
                false
            } else {
                binding.tilPass.isErrorEnabled = false
                true
            }
        }
    }

    private fun validateMobile(): Boolean {
        val isValid: Boolean
        if (binding.tilMobile.editText?.text.isNullOrEmpty()) {
            binding.tilMobile.error = "Field cannot be Empty"
            isValid = false
        } else {
            if (!Patterns.PHONE.matcher(binding.tilMobile.editText?.text.toString()).matches() || binding.tilMobile.editText?.text?.length != 10) {
                binding.tilMobile.error = "Enter a Valid Mobile Number"
                isValid = false
            } else {
                binding.tilMobile.isErrorEnabled = false
                isValid = true
            }
        }
        return isValid
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun registerStatus() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.registerLiveData.observe(requireActivity()) {
                    when (it) {
                        is ResponseState.Success -> {
                            hideLoading()
                            Toast.makeText(
                                requireContext(),
                                "Registered as Volunteer Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ResponseState.Error -> {
                            hideLoading()
                            Toast.makeText(requireContext(),
                                it.message.toString()
                                    .replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase(Locale.ROOT) else c.toString() },
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ResponseState.Loading -> {
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is LoadingInterface) {
            listener = context
        } else {
            throw ClassCastException("$context must implement LoadingInterface")
        }
    }


    private fun showLoading() {
        binding.apply {
            listener?.showLoading()
            etUsernameLogin.isEnabled = false
            etNameLogin.isEnabled = false
            etEmailLogin.isEnabled = false
            etPasswordLogin.isEnabled = false
            etMobileNo.isEnabled = false
        }
    }

    private fun hideLoading() {
        binding.apply {
            listener?.hideLoading()
            etUsernameLogin.isEnabled = true
            etNameLogin.isEnabled = true
            etEmailLogin.isEnabled = true
            etPasswordLogin.isEnabled = true
            etMobileNo.isEnabled = true
        }
    }
}