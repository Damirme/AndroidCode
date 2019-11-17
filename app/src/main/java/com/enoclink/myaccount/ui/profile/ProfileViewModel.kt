package com.enoclink.myaccount.ui.profile

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoclink.myaccount.R
import com.enoclink.myaccount.data.UserRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _profileResult = MutableLiveData<ProfileResult>()
    private val _profilePhotoFilePath = MutableLiveData<String>()
    val profileResult: LiveData<ProfileResult> = _profileResult
    val profileAvatarPath: LiveData<String> = _profilePhotoFilePath

    private var currentPhotoPath: String? = null

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

    fun setProfilePhoto() {
        _profilePhotoFilePath.value = currentPhotoPath
    }

    fun getOutputFileUri(context: Context): Uri? {
        val photoFile: File = try {
            createImageFile(context)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
            ?: return null


        return FileProvider.getUriForFile(
            context,
            "com.enoclink.myaccount.provider",
            photoFile
        )
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

}