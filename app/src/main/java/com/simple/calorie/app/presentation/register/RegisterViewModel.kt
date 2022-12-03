package com.simple.calorie.app.presentation.register


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.calorie.app.repositories.ResponseResource
import com.simple.calorie.app.repositories.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(private val remoteRepository: RemoteRepository) :
    ViewModel() {


    val showRegisterButton: MutableLiveData<Boolean> = MutableLiveData(true)
    val showProgressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRegistrationSucceed: MutableLiveData<Boolean> = MutableLiveData(false)
    val registrationFailedMessage: MutableLiveData<String> = MutableLiveData("")

    fun register(userId: String) {
        var willCollect = true
        registrationFailedMessage.value = ""
        viewModelScope.launch {
            remoteRepository.register(userId).takeWhile { willCollect }.collect { response ->
                when (response) {
                    is ResponseResource.Failed -> {
                        registrationFailedMessage.value = response.message
                        showProgressBar.value = false
                        showRegisterButton.value = true
                        willCollect = false
                    }
                    is ResponseResource.Pending -> {
                        registrationFailedMessage.value = ""
                        showProgressBar.value = true
                        showRegisterButton.value = false
                    }
                    is ResponseResource.Succeed -> {
                        isRegistrationSucceed.value = true
                        willCollect = false
                    }
                }
            }
        }

    }
}