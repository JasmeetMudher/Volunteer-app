package com.volu.volunteerconnect.Ui.Events

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.volu.volunteerconnect.Model.Requests.RequestsData
import com.volu.volunteerconnect.R
import com.volu.volunteerconnect.Utils.DataStoreUtil
import com.volu.volunteerconnect.Utils.ResponseState
import com.volu.volunteerconnect.Utils.hasInternetConnection
import com.volu.volunteerconnect.databinding.FragmentSingleEventBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class SingleEventFragment : Fragment(R.layout.fragment_single_event) {

    private lateinit var binding: FragmentSingleEventBinding
    private val singleViewModel by viewModels<SingleEventViewModel>()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var isLiked = false
    private lateinit var prefs: DataStoreUtil
    private val args by navArgs<SingleEventFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSingleEventBinding.bind(view)

        prefs = DataStoreUtil(requireContext())
        val data = args.eventData
        singleViewModel.isNetworkConnectedLiveData.value = requireContext().hasInternetConnection()
        singleViewModel.getEventData(data?.id.toString())
        viewPagerAdapter = ViewPagerAdapter()


        binding.viewPager.apply {
            adapter = viewPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }



        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.indicator.onPageSelected(position)

            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }



        handleEventData()
        handleRequestStatus()

        binding.cardRetry.setOnClickListener {
            singleViewModel.getEventData(data?.id.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleEventData() {
        singleViewModel.eventLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()

                    if (it.data?.volunteers?.contains(prefs.getID().toString()) == true) {
                        binding.btnApply.text = "Request Accepted"
                        binding.btnApply.isClickable = false
                        binding.btnApply.isEnabled = false
                    } else {
                        binding.btnApply.text = "Apply"
                        binding.btnApply.isClickable = true
                        binding.btnApply.isEnabled = true
                    }

                    binding.tvEventDate.text =
                        dateFormatEnglish(it.data?.eventStartDataAndTime.toString())
                    binding.llError.visibility = View.GONE
                    binding.tvEventName.text = it.data?.name
                    binding.tvEventDesc.text = it.data?.desc
                    binding.tvEventFee.text =
                        if (it.data?.isPaid == true) "₹${it.data.price}" else "Free"
                    binding.tvEventSeats.text = "${
                        it.data?.volunteers?.size?.let { it1 ->
                            it.data.volunteerCount?.minus(
                                it1
                            )
                        }
                    } seats available"
                    binding.tvEventLocation.text = it.data?.address
                    binding.tvEventEmail.text = it.data?.email
                    binding.tvEventPhone.text = it.data?.phone
                    val imageList = arrayListOf<String>()
                    imageList.add(0, it.data?.eventPicture.toString())
                    it.data?.gallery?.forEach { url ->
                        if (url.isNotEmpty()) imageList.add(url)
                    }
                    viewPagerAdapter.differ.submitList(imageList)
                    binding.tvEventSkills.text = it.data?.skills?.joinToString()
                    binding.tvEventCategory.text = it.data?.category?.joinToString()
                    binding.tvEventVisibility.text = "This Event is ${
                        if (it.data?.isPaid == true && it.data.visibility.equals(
                                "private", ignoreCase = true
                            )
                        ) "Private Paid Event" else it.data?.visibility?.replaceFirstChar { i ->
                            if (i.isLowerCase()) i.titlecase(
                                Locale.ROOT
                            ) else i.toString()
                        }
                    }"

                    binding.tvEventPayment.text =
                        if (it.data?.isPaying == true) "The Volunteers will be provided ₹${it.data.payment} after Completion" else "The Volunteers will be thanked for there Service."

                    binding.tvEventGoodies.text =
                        if (it.data?.isGoodiesProvided == true) "${it.data.goodies} will be provided to Volunteers after Completion" else "There are No Goodies after Completion"

                    binding.tvEventPoints.text =
                        "After Completion ${it.data?.eventPoint} Points will be Credited to the Volunteer"

                    binding.tvEventStartEndTime.text =
                        "Event Will Start From ${dateFormat(it.data?.eventStartDataAndTime.toString())} and will End At ${
                            dateFormat(
                                it.data?.eventEndingDateAndTime.toString()
                            )
                        }"
                    binding.indicator.setPageSize(binding.viewPager.adapter!!.itemCount)

                    binding.tvEventAddress.setOnClickListener { v ->
                        val strUri =
                            "http://maps.google.com/maps?q=loc:${it.data?.coordinates?.get(0)},${
                                it.data?.coordinates?.get(
                                    1
                                )
                            }(${it.data?.name})"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
                        intent.setClassName(
                            "com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity"
                        )
                        startActivity(intent)
                    }

                    binding.btnApply.setOnClickListener { v ->
                        if (!it.data?.question.isNullOrEmpty()) {
                            findNavController().navigate(
                                SingleEventFragmentDirections.actionSingleEventFragmentToQuestionFragment(
                                    it.data
                                )
                            )
                        } else {
                            val data = RequestsData(
                                answers = listOf(),
                                recipient = it.data?.user,
                                status = 1,
                                eventId = it.data?.id,
                                eventName = it.data?.name,
                            )
                            singleViewModel.sendRequest(
                                prefs.getToken().toString(),
                                data
                            )
                        }
                    }

                    binding.ivHeart.setOnClickListener { v ->
                        if (isLiked) {
                            isLiked = false
                            binding.ivHeart.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(), R.drawable.heart_layer_list
                                )
                            )
                            it.data?.let { it1 -> singleViewModel.deleteEvent(it1) }
                        } else {
                            isLiked = true
                            binding.ivHeart.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(), R.drawable.heart_layer_list_liked
                                )
                            )
                            it.data?.let { it1 -> singleViewModel.saveEvent(it1) }
                        }
                    }
                }

                is ResponseState.Loading -> {
                    showLoading()
                    binding.llError.visibility = View.GONE
                }

                is ResponseState.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    hideLoading()
                    binding.llError.visibility = View.VISIBLE
                    binding.tvErrorMsg.text = it.message.toString()
                }
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressLoading.visibility = View.VISIBLE
            coLayout.visibility = View.GONE
            llApply.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        binding.apply {
            progressLoading.visibility = View.GONE
            coLayout.visibility = View.VISIBLE
            llApply.visibility = View.VISIBLE
        }
    }

    private fun handleRequestStatus() {
        singleViewModel.sendRequestLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    hideLoading()
                    Toast.makeText(requireContext(), "Request Success", Toast.LENGTH_SHORT).show()
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

    private fun dateFormat(timestamp: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.ENGLISH)

        val date = inputFormat.parse(timestamp)
        return outputFormat.format(date as Date)
    }

    private fun dateFormatEnglish(timestamp: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

        val date = inputFormat.parse(timestamp)
        return outputFormat.format(date as Date)
    }
}