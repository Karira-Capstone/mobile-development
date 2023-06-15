package com.capstone.karira.activity.dashboard.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.karira.R
import com.capstone.karira.databinding.ActivityEditProfileBinding
import com.capstone.karira.model.Freelancer
import com.capstone.karira.model.Profile
import com.capstone.karira.model.User
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.auth.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var birthDateEditText: EditText
    private var calendar: Calendar? = null
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private lateinit var user: UserDataStore
    private lateinit var adapter: EditProfileAdapter
    private lateinit var editPictureButton: ImageButton
    private var urlImage: String = ""
    private lateinit var saveButton: Button
    private lateinit var fullNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var nikEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var provinceEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var addressEditText: EditText
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImage = data?.data
                if (selectedImage != null) {
                    adapter.updateProfilePicture(selectedImage)
                    val imageFile = convertUriToFile(this, selectedImage)
                    if (imageFile != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            urlImage = viewModel.uploadFile(user.token, imageFile)
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        birthDateEditText = findViewById(R.id.birthDateEditText)
        editPictureButton = findViewById(R.id.editPictureButton)
        editPictureButton.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            imagePickerLauncher.launch(photoPickerIntent)
        }
        birthDateEditText.isFocusable = false
        birthDateEditText.isClickable = true
        calendar = Calendar.getInstance()
        populateAdapter()
        saveButton = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            saveProfileChanges()
        }

        fullNameEditText = findViewById(R.id.fullNameEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        nikEditText = findViewById(R.id.nikEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        provinceEditText = findViewById(R.id.provinceEditText)
        cityEditText = findViewById(R.id.cityEditText)
        addressEditText = findViewById(R.id.addressEditText)
    }

    private fun setupActionBar() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun showDatePickerDialog(view: View?) {
        val dateSetListener =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar!![Calendar.YEAR] = year
                calendar!![Calendar.MONTH] = monthOfYear
                calendar!![Calendar.DAY_OF_MONTH] = dayOfMonth
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val selectedDate: String = dateFormat.format(calendar!!.time)
                birthDateEditText!!.setText(selectedDate)
            }
        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            calendar!![Calendar.YEAR],
            calendar!![Calendar.MONTH],
            calendar!![Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    private fun populateAdapter() {
        adapter = EditProfileAdapter(User())
        viewModel.getUserLiveData().observe(this) { userDataStore ->
            user = userDataStore
            val rootView = findViewById<View>(R.id.fragmentEditContainer)
            CoroutineScope(Dispatchers.Main).launch {
                val userProfile = viewModel.getUserProfile(user.token)
                adapter.populateAdapter(rootView, userProfile)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun convertUriToFile(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        val fileExtension = getFileExtension(contentResolver, uri)
        val tempFile = createTempFile(context, fileExtension)
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val outputStream = FileOutputStream(tempFile)
                copyStream(inputStream, outputStream)
                outputStream.close()
                tempFile
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileExtension(contentResolver: ContentResolver, uri: Uri): String {
        val mimeType = contentResolver.getType(uri)
        return if (mimeType != null && mimeType.startsWith("image/")) {
            mimeType.substring("image/".length)
        } else {
            "jpg"
        }
    }

    private fun createTempFile(context: Context, fileExtension: String): File {
        val cacheDir = context.cacheDir
        return File.createTempFile("temp_image_", ".$fileExtension", cacheDir)
    }

    private fun copyStream(inputStream: java.io.InputStream, outputStream: FileOutputStream) {
        val buffer = ByteArray(4 * 1024) // 4 KB buffer
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
    }

    private fun saveProfileChanges() {
        val fullName = fullNameEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val birthDate = birthDateEditText.text.toString()
        val nik = nikEditText.text.toString()
        val phoneNumber = phoneNumberEditText.text.toString()
        val province = provinceEditText.text.toString()
        val city = cityEditText.text.toString()
        val address = addressEditText.text.toString()
        if (user.role == "WORKER") {
            val profile = Profile(fullName, description, birthDate, nik, phoneNumber, province, city, address, urlImage)
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.updateFreelancerProfile(user.token, profile)
            }
        } else {
            val profile = Profile(fullName, description, birthDate, nik, phoneNumber, province, city, address, urlImage)
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.updateClientProfile(user.token, profile)
            }
        }
    }

}