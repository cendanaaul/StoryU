package com.cencen.storyu.view.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.cencen.storyu.R
import com.cencen.storyu.data.Libraries
import com.cencen.storyu.databinding.ActivityUploadStoryBinding
import com.cencen.storyu.utility.ViewModelProviderFactory
import com.cencen.storyu.utility.convertUriToFiles
import com.cencen.storyu.utility.makeCustomFilePhoto
import com.cencen.storyu.utility.minimizeImageFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadStoryBinding
    private lateinit var imgPath: String
    private lateinit var upStoryViewModel: UploadStoryViewModel

    private var theFiles: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                showToast(R.string.no_permission.toString())
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        initializeVM()

        binding.btnTakephoto.setOnClickListener { capturePhoto() }
        binding.btnPhotogallery.setOnClickListener { pickPhotoGallery() }
        binding.btnPublishstory.setOnClickListener { uploadPhoto() }
    }

    private fun initializeVM() {
        val fact: ViewModelProviderFactory = ViewModelProviderFactory.getInstance(this)
        upStoryViewModel = ViewModelProvider(this, fact)[UploadStoryViewModel::class.java]
    }

    private fun uploadPhoto() {
        upStoryViewModel.getMember().observe(this@UploadStoryActivity) {
            val tokens = "Bearer " + it.token
            if (theFiles != null) {
                val img = minimizeImageFile(theFiles as File)

                val desc =
                    "${binding.etDescripphoto.text}".toRequestBody("text/plain".toMediaType())
                val reqImgFile = img?.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    img?.name,
                    reqImgFile!!
                )
                upStoryViewModel.addMemberStory(tokens, imgMultipart, desc)
                    .observe(this@UploadStoryActivity) {
                        when (it) {
                            is Libraries.Success -> {
                                showToast(it.data.message)
                                showProcessing(false)
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }

                            is Libraries.Loading -> showProcessing(true)
                            is Libraries.Error -> {
                                showToast(it.error)
                                showProcessing(false)
                            }
                        }
                    }
            } else showToast(R.string.upload_warning.toString())
        }
    }

    private fun pickPhotoGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val picker = Intent.createChooser(intent, "Select photo")
        launcherIntentGallery.launch(picker)
    }

    private fun capturePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        makeCustomFilePhoto(application).also {
            val imgURI: Uri = FileProvider.getUriForFile(
                this@UploadStoryActivity,
                "com.cencen.storyu",
                it
            )
            imgPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { photoResult ->
        if (photoResult.resultCode == RESULT_OK) {
            val photoSelect: Uri = photoResult.data?.data as Uri
            val files = convertUriToFiles(photoSelect, this@UploadStoryActivity)
            theFiles = files
            binding.ivUploadimageprev.setImageURI(photoSelect)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val files = File(imgPath)
            theFiles = files
            val photoResult = BitmapFactory.decodeFile(theFiles?.path)
            binding.ivUploadimageprev.setImageBitmap(photoResult)
        }
    }

    private fun showProcessing(load: Boolean) {
        binding.loadingbars.visibility = if (load) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}