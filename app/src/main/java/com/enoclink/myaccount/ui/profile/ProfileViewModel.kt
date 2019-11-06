package com.enoclink.myaccount.ui.profile

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoclink.myaccount.R
import com.enoclink.myaccount.data.UserRepository
import kotlinx.coroutines.launch
import java.io.File


class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _profileResult = MutableLiveData<ProfileResult>()
    val profileResult: LiveData<ProfileResult> = _profileResult

    fun getUserProfile() {
        viewModelScope.launch {
            val result = userRepository.getUserProfile(userId = userRepository.user?.userId ?: "")

            if (result.isSuccessful) {
                _profileResult.value = ProfileResult(success = result.body())
            } else {
                _profileResult.value = ProfileResult(error = R.string.login_failed)
            }
        }
    }

    fun getOutputFileUri(context: Context): Uri {
        val dir = context.cacheDir
        val imageFile = File(dir, "avatar.jpg")
        imageFile.mkdirs()
        val imageUri = FileProvider.getUriForFile(
            context,
            "com.enoclink.myaccount.provider", //(use your app signature + ".provider" )
            imageFile
        )
        return imageUri//Uri.fromFile(imageFile)
    }
}