package com.simple.calorie.app.presentation.input

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.simple.calorie.app.data.preference.SharedPreferenceDataStore
import com.simple.calorie.app.databinding.FragmentFoodEntryInputBinding
import com.simple.calorie.app.navigator.AppNavigator
import com.simple.calorie.app.navigator.Screens
import com.simple.calorie.app.presentation.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FoodEntryInputFragment : Fragment() {

    companion object {
        fun newInstance() = FoodEntryInputFragment()
        private const val FOOD_ENTRY_ID = "food.entry.id"
        fun newInstanceForUpdate(entryId: Long): FoodEntryInputFragment {
            return FoodEntryInputFragment().apply {
                arguments = Bundle().apply {
                    putLong(FOOD_ENTRY_ID, entryId)
                }
            }
        }
    }

    private val viewModel by viewModels<FoodEntryInputViewModel>()

    @Inject
    lateinit var navigator: AppNavigator


    private lateinit var binding: FragmentFoodEntryInputBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFoodEntryInputBinding.inflate(inflater, container, false)
        arguments?.let {
            val entryId = it.getLong(FOOD_ENTRY_ID, -1)
            if (entryId != -1L) {
                viewModel.isEditMode = true
                CoroutineScope(Dispatchers.IO).launch {
                    val foodEntry = viewModel.getFoodEntry(entryId)
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.etFoodName.setText(foodEntry?.foodName)
                        binding.etCalorieAmount.setText(foodEntry?.calorie?.toString())
                        binding.etUserId.setText(foodEntry?.userId)
                        binding.etUserIdLayout.isEnabled = false
                        binding.btAddEntry.text = "Edit Food Entry"
                    }
                }
            }
        }

        if (viewModel.store.isUserAdmin()) {
            binding.etUserIdLayout.visibility = View.VISIBLE
        } else {
            binding.etUserIdLayout.visibility = View.GONE
        }


        initializeInput()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observeViewModelData()

    }

    private fun observeViewModelData() {
        viewModel.getInputTimeText().observe(viewLifecycleOwner) {
            binding.etTimeOfEntry.setText(it)
        }

        viewModel.showProgressBar.observe(viewLifecycleOwner) {
            if (it) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }

        viewModel.failedMessage.observe(viewLifecycleOwner) {
            binding.tvError.text = it
        }

        viewModel.isAddedSucceed.observe(viewLifecycleOwner) {
            if (it) navigator.navigateTo(Screens.DASHBOARD)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initializeInput() {
        binding.etTimeOfEntry.setOnTouchListener { _: View, motionEvent: MotionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN)
                datePicker()
            return@setOnTouchListener true
        }
        binding.etTimeOfEntry.showSoftInputOnFocus = false

        binding.btCancel.setOnClickListener {
            navigator.navigateTo(Screens.DASHBOARD)
        }

        binding.btAddEntry.setOnClickListener {
            viewModel.addFoodEntry(
                binding.etFoodName.text.toString(),
                binding.etCalorieAmount.text.toString(),
                binding.etUserId.text.toString()
            )
        }
    }

    private fun datePicker() {
        val calendar = viewModel.calendar
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                viewModel.updateInputDate(year, monthOfYear, dayOfMonth)
                timePicker()
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    private fun timePicker() {
        val calendar = viewModel.calendar
        val mHour = calendar[Calendar.HOUR_OF_DAY]
        val mMinute = calendar[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                viewModel.updateInputTime(hourOfDay, minute)

            }, mHour, mMinute, false
        )
        timePickerDialog.show()
    }

}