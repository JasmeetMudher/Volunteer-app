package com.volu.volunteerconnect.Ui.Events

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.volu.volunteerconnect.Model.Events.DataFetch
import com.volu.volunteerconnect.Model.Requests.RequestsData
import com.volu.volunteerconnect.R
import com.volu.volunteerconnect.Utils.DataStoreUtil
import com.volu.volunteerconnect.Utils.LoadingInterface
import com.volu.volunteerconnect.Utils.ResponseState
import com.volu.volunteerconnect.Utils.hasInternetConnection
import com.volu.volunteerconnect.databinding.FragmentQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionFragment : Fragment(R.layout.fragment_question) {

    private lateinit var binding: FragmentQuestionBinding
    private val args by navArgs<QuestionFragmentArgs>()
    private var listener: LoadingInterface? = null
    private val viewModel by viewModels<SingleEventViewModel>()
    private lateinit var prefs: DataStoreUtil
    private lateinit var eventData: DataFetch

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentQuestionBinding.bind(view)
        eventData = args.eventData!!
        prefs = DataStoreUtil(requireContext())
        viewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()


        binding.apply {
            tvQuestion1.text = "1. ${eventData.question?.get(0)} *"
            tvQuestion2.text = "2. ${eventData.question?.get(1)} *"
            tvQuestion3.text = "3. ${eventData.question?.get(2)} *"
            tvQuestion4.text = "4. ${eventData.question?.get(3)} *"
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.btnApply.setOnClickListener {
            if (validate()) {
                val data = RequestsData(
                    answers = listOf(getAnswers()),
                    recipient = eventData.user,
                    status = 1,
                    eventId = eventData.id,
                    eventName = eventData.name,
                )
                if (eventData.isPaid == true) {
                    findNavController().navigate(
                        QuestionFragmentDirections.actionQuestionFragmentToPaymentActivity(
                            eventData,
                            data
                        )
                    )
                } else {
                    viewModel.sendRequest(prefs.getToken().toString(), data)
                }
            }
        }

        handleRequestStatus()

    }

    private fun handleRequestStatus() {
        viewModel.sendRequestLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                }

                is ResponseState.Loading -> {
                    showLoading()
                }

                is ResponseState.Error -> {
                    hideLoading()
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun getAnswers(): Map<String, String> {
        return mapOf(
            eventData.question?.get(0).toString() to binding.tilAnswer1.editText?.text.toString(),
            eventData.question?.get(1).toString() to binding.tilAnswer2.editText?.text.toString(),
            eventData.question?.get(2).toString() to binding.tilAnswer3.editText?.text.toString(),
            eventData.question?.get(3).toString() to binding.tilAnswer4.editText?.text.toString()
        )
    }

    private fun validate(): Boolean {
        return binding.tilAnswer1.editText?.text.toString()
            .isNotEmpty() && binding.tilAnswer1.editText?.text.toString()
            .isNotEmpty() && binding.tilAnswer1.editText?.text.toString()
            .isNotEmpty() && binding.tilAnswer1.editText?.text.toString().isNotEmpty()
    }

    private fun showLoading() {
        binding.apply {
            listener?.showLoading()
            binding.tilAnswer1.editText?.isEnabled = false
            binding.tilAnswer2.editText?.isEnabled = false
            binding.tilAnswer3.editText?.isEnabled = false
            binding.tilAnswer4.editText?.isEnabled = false
            binding.btnApply.isEnabled = false
        }
    }

    private fun hideLoading() {
        listener?.hideLoading()
        binding.tilAnswer1.editText?.isEnabled = true
        binding.tilAnswer2.editText?.isEnabled = true
        binding.tilAnswer3.editText?.isEnabled = true
        binding.tilAnswer4.editText?.isEnabled = true
        binding.btnApply.isEnabled = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoadingInterface) {
            listener = context
        } else {
            throw ClassCastException("$context must implement LoadingInterface")
        }
    }

}