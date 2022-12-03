package com.simple.calorie.app.presentation.register


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.simple.calorie.app.databinding.FragmentRegisterBinding
import com.simple.calorie.app.navigator.AppNavigator
import com.simple.calorie.app.navigator.Screens
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val viewModel by viewModels<RegisterViewModel>()

    @Inject
    lateinit var navigator: AppNavigator
    private lateinit var binding: FragmentRegisterBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.btRegister.setOnClickListener {
            viewModel.register(binding.etUserId.text.toString())
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        watchLiveData()
    }

    private fun watchLiveData() {
        viewModel.showRegisterButton.observe(viewLifecycleOwner) {
            if (it) binding.btRegister.visibility = View.VISIBLE
            else binding.btRegister.visibility = View.GONE
        }

        viewModel.showProgressBar.observe(viewLifecycleOwner) {
            if (it) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }

        viewModel.registrationFailedMessage.observe(viewLifecycleOwner) {
            binding.tvError.text = it
        }

        viewModel.isRegistrationSucceed.observe(viewLifecycleOwner) {
            if (it) navigator.navigateTo(Screens.DASHBOARD)
        }
    }

}