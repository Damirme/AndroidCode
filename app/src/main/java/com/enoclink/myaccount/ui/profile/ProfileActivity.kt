package com.enoclink.myaccount.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.enoclink.myaccount.R
import com.enoclink.myaccount.ui.login.LoginViewModelFactory
import java.io.File


class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"
    private val TAKE_PHOTO = 1
    private val MY_PERMISSIONS_REQUEST_CAMERA = 2

    private lateinit var profileViewModel: ProfileViewModel

    var imvProfile: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        imvProfile = findViewById<ImageView>(R.id.imvProfile)
        val txvEmail = findViewById<TextView>(R.id.txvEmail)
        val loading = findViewById<ProgressBar>(R.id.progressBar)

        profileViewModel = ViewModelProviders
            .of(this, LoginViewModelFactory())
            .get(ProfileViewModel::class.java)

        profileViewModel.profileResult.observe(this@ProfileActivity, Observer {
            val profileResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (profileResult.error != null) {
//                showProfileFetchFailed(profileResult.error)
            }
            if (profileResult.success != null) {
                txvEmail.text = profileResult.success.email
            }
        })

        profileViewModel.profileAvatarPath.observe(this@ProfileActivity, Observer {
            val photoPath = it ?: return@Observer

            imvProfile?.setImageURI(Uri.fromFile(File(photoPath)))

        })

        imvProfile?.setOnClickListener {
            if (loading.visibility == View.GONE) {
                showImageChooser()
            }
        }

        profileViewModel.getUserProfile()
    }

    private fun showImageChooser() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(R.string.imageChooserTitle)
        pictureDialog.setItems(
            arrayOf(
                getString(R.string.chooseFromGallary),
                getString(R.string.chooseFromCamera)
            )
        ) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun takePhotoFromCamera() {
        validatePermissions()
    }

    private fun choosePhotoFromGallary() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        Log.e(TAG, "onActivityResult: $requestCode, resultCode: $resultCode")
        when (requestCode) {
            0 -> if (resultCode == Activity.RESULT_OK) {
            }
            TAKE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    profileViewModel.setProfilePhoto()
                }
        }
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)

    }


    private fun validatePermissions() {
        if (ContextCompat.checkSelfPermission(
                this@ProfileActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ProfileActivity,
                arrayOf(
                    Manifest.permission.CAMERA
                ),
                MY_PERMISSIONS_REQUEST_CAMERA
            )
        } else {
            openCamera()
        }
    }


    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val fileUri = profileViewModel.getOutputFileUri(this)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                startActivityForResult(takePictureIntent, TAKE_PHOTO)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    // permission denied
                }
                return
            }
        }
    }
}
