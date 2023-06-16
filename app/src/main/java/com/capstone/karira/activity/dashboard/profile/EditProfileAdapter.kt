package com.capstone.karira.activity.dashboard.profile

import android.net.Uri
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.capstone.karira.R
import com.capstone.karira.model.User
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditProfileAdapter(private val user: User?) {

    private lateinit var fullNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var birthDateEditText: EditText
    private lateinit var nikEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var provinceEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var profilePictureImageView: ImageView
    private lateinit var editPictureButton: ImageButton

    fun populateAdapter(rootView: View, user: User) {
        fullNameEditText = rootView.findViewById(R.id.fullNameEditText)
        descriptionEditText = rootView.findViewById(R.id.descriptionEditText)
        birthDateEditText = rootView.findViewById(R.id.birthDateEditText)
        nikEditText = rootView.findViewById(R.id.nikEditText)
        phoneNumberEditText = rootView.findViewById(R.id.phoneNumberEditText)
        provinceEditText = rootView.findViewById(R.id.provinceEditText)
        cityEditText = rootView.findViewById(R.id.cityEditText)
        addressEditText = rootView.findViewById(R.id.addressEditText)
        editPictureButton = rootView.findViewById(R.id.editPictureButton)
        profilePictureImageView = rootView.findViewById(R.id.ivProfilePicture)
        Picasso.get().load(user.picture).into(profilePictureImageView)

        fullNameEditText.text = Editable.Factory.getInstance().newEditable(user.fullName)

        if (user.role == "WORKER") {
            descriptionEditText.text = Editable.Factory.getInstance().newEditable(user.worker?.description ?: "")
            val birthDate = user.worker?.birthDate ?: ""
            val formattedBirthDate = formatBirthDate(birthDate)
            birthDateEditText.text = Editable.Factory.getInstance().newEditable(formattedBirthDate)
            nikEditText.text = Editable.Factory.getInstance().newEditable(user.worker?.identityNumber ?: "")
            phoneNumberEditText.text = Editable.Factory.getInstance().newEditable(user.worker?.phone ?: "")
            provinceEditText.text = Editable.Factory.getInstance().newEditable(user.worker?.province ?: "")
            cityEditText.text = Editable.Factory.getInstance().newEditable(user.worker?.city ?: "")
            addressEditText.text = Editable.Factory.getInstance().newEditable(user.worker?.address ?: "")
        } else {
            descriptionEditText.text = Editable.Factory.getInstance().newEditable(user.client?.description ?: "")
            val birthDate = user.client?.birthDate ?: ""
            val formattedBirthDate = formatBirthDate(birthDate)
            birthDateEditText.text = Editable.Factory.getInstance().newEditable(formattedBirthDate)
            nikEditText.text = Editable.Factory.getInstance().newEditable(user.client?.identityNumber ?: "")
            phoneNumberEditText.text = Editable.Factory.getInstance().newEditable(user.client?.phone ?: "")
            provinceEditText.text = Editable.Factory.getInstance().newEditable(user.client?.province ?: "")
            cityEditText.text = Editable.Factory.getInstance().newEditable(user.client?.city ?: "")
            addressEditText.text = Editable.Factory.getInstance().newEditable(user.client?.address ?: "")
        }
    }

    private fun formatBirthDate(birthDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id"))
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))

        return try {
            val date = inputFormat.parse(birthDate)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            birthDate
        }
    }

    fun updateProfilePicture(imageUri: Uri) {
        Picasso.get().load(imageUri).into(profilePictureImageView)
    }

}
