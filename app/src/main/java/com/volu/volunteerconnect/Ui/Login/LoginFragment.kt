package com.volu.volunteerconnect.Ui.Login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.volu.volunteerconnect.Model.Auth.LoginData
import com.volu.volunteerconnect.R
import com.volu.volunteerconnect.Utils.Constants.TAG
import com.volu.volunteerconnect.Utils.DataStoreUtil
import com.volu.volunteerconnect.Utils.LoadingInterface
import com.volu.volunteerconnect.Utils.ResponseState
import com.volu.volunteerconnect.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var loginData: LoginData
    private var listener: LoadingInterface? = null
    private lateinit var dataStore: DataStoreUtil

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        dataStore = DataStoreUtil(requireContext())
        loginStatus()

        binding.tvNoAcc.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvForgotPass.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }


        binding.btnLogin.setOnClickListener {
            if (binding.tilEmail.editText?.text.isNullOrEmpty()) {
                binding.tilEmail.error = "Field cannot be empty"
            } else if (binding.tilPass.editText?.text.isNullOrEmpty()) {
                binding.tilPass.error = "Field Cannot be Empty"
            } else {
                binding.tilEmail.isErrorEnabled = false
                binding.tilPass.isErrorEnabled = false
                if (Patterns.EMAIL_ADDRESS.matcher(binding.tilEmail.editText?.text.toString())
                        .matches()
                ) {
                    loginData = LoginData(
                        "",
                        binding.tilEmail.editText?.text.toString(),
                        binding.tilPass.editText?.text.toString()
                    )

                } else {
                    loginData = LoginData(
                        binding.tilEmail.editText?.text.toString(),
                        "",
                        binding.tilPass.editText?.text.toString()
                    )
                }
                loginViewModel.loginUser(loginData)
                showLoading()
            }
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loginStatus() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.signInDataLiveData.observe(requireActivity()) {
                    when (it) {
                        is ResponseState.Success -> {
                            hideLoading()
                            Log.d(TAG, "loginStatus: ${it.data?.token}")
                            addToken(it.data?.token, it.data?.role.toString())
                            if(it.data?.role.equals("organization")) {
                                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeOrgFragment())
                            } else {
                                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                            }
                        }

                        is ResponseState.Error -> {
                            hideLoading()
                            Toast.makeText(
                                requireContext(),
                                it.message.toString(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        is ResponseState.Loading -> {

                        }

                        else -> {

                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoadingInterface) {
            listener = context
        } else {
            throw ClassCastException("$context must implement LoadingInterface")
        }
    }


    private fun showLoading() {
        listener?.showLoading()
        binding.tilEmail.isEnabled = false
        binding.tilPass.isEnabled = false
    }

    private fun hideLoading() {
        listener?.hideLoading()
        binding.tilEmail.isEnabled = true
        binding.tilPass.isEnabled = true
    }


    private fun addToken(token: String?, role: String) {
        dataStore.setLoggedIn(true)
        dataStore.setToken(token)
        dataStore.setRole(role)
    }

}