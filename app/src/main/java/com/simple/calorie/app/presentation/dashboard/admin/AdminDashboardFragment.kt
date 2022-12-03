package com.simple.calorie.app.presentation.dashboard.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.simple.calorie.app.data.room.entity.FoodEntry
import com.simple.calorie.app.databinding.FragmentAdminDashboardBinding
import com.simple.calorie.app.navigator.AppNavigator
import com.simple.calorie.app.navigator.Screens
import com.simple.calorie.app.presentation.dashboard.FoodEntryAdapter
import com.simple.calorie.app.presentation.dashboard.FoodEntryContextMenuListener
import com.simple.calorie.app.presentation.dashboard.UserAverageListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AdminDashboardFragment : Fragment(), FoodEntryContextMenuListener {

    companion object {
//        private val dashboardFragment = AdminDashboardFragment()
        fun newInstance() = AdminDashboardFragment()
    }

    private val viewModel by viewModels<AdminDashboardViewModel>()

    @Inject
    lateinit var navigator: AppNavigator
    private lateinit var binding: FragmentAdminDashboardBinding

    var foodEntryAdapter = FoodEntryAdapter(this)
    val userAverageListAdapter = UserAverageListAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        binding.rvList.adapter = foodEntryAdapter
        binding.rvUserAverageList.adapter = userAverageListAdapter
        initializeInput()
        return binding.root

    }

    private fun initializeInput() {
        binding.ivEditStartDate.setOnClickListener {
            datePicker(viewModel.startCalender) { year: Int, monthOfYear: Int, dayOfMonth: Int ->
                viewModel.updateStartDate(year, monthOfYear, dayOfMonth)
            }
        }

        binding.ivEditEndDate.setOnClickListener {
            datePicker(viewModel.endCalender) { year: Int, monthOfYear: Int, dayOfMonth: Int ->
                viewModel.updateEndDate(year, monthOfYear, dayOfMonth)
            }
        }

        binding.btSync.setOnClickListener {
            viewModel.requestSync()
        }

    }

    override fun onStart() {
        super.onStart()

        binding.btAddEntry.setOnClickListener {
            navigator.navigateTo(Screens.ADD_FOOD_ENTRY)
        }
        viewModel.foodEntryListLiveData.observe(viewLifecycleOwner) {
            foodEntryAdapter.submitList(it)
        }
        viewModel.startTimeString.observe(viewLifecycleOwner) {
            binding.tvStartDate.text = it
        }

        viewModel.endTimeString.observe(viewLifecycleOwner) {
            binding.tvEndDate.text = it
        }

        viewModel.entryCntLastSevenDay.observe(viewLifecycleOwner) {
            binding.tvCurrentDays.text = it
        }

        viewModel.entryCntLastPrevSevenDay.observe(viewLifecycleOwner) {
            binding.tvCurrentPrevDays.text = it
        }

        viewModel.userAverageItemList.observe(viewLifecycleOwner) {
            userAverageListAdapter.submitList(it)
        }



        viewModel.requestSync()
    }

    override fun onResume() {
        super.onResume()
        viewModel.observerFoodEntryDb()
        viewModel.observerLastSevenDaysData()
    }

    private fun datePicker(
        calendar: Calendar,
        callback: (year: Int, monthOfYear: Int, dayOfMonth: Int) -> Unit
    ) {
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                callback(year, monthOfYear, dayOfMonth)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    override fun deleteItem(foodEntry: FoodEntry) {
        viewModel.deleteFoodEntry(foodEntry)
    }

    override fun editItem(foodEntry: FoodEntry) {
        navigator.navigateTo(Screens.UPDATE_FOOD_ENTRY, foodEntry.entryId)
    }


}
