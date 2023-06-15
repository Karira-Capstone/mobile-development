package com.capstone.karira.activity.dashboard.profile
import com.squareup.picasso.Picasso

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.capstone.karira.R
import com.capstone.karira.model.User

class ProfileAdapter(private val user: User?) {

    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var roleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var pictureImageView: ImageView

    fun populateAdapter(rootView: View, user: User) {
        fullNameTextView = rootView.findViewById(R.id.tv_full_name)
        emailTextView = rootView.findViewById(R.id.tv_email)
        roleTextView = rootView.findViewById(R.id.tv_role)
        descriptionTextView = rootView.findViewById(R.id.tv_description)
        pictureImageView = rootView.findViewById(R.id.iv_picture)
        Picasso.get().load(user.picture).into(pictureImageView)

        fullNameTextView.text = user.fullName
        emailTextView.text = user.email

        if (user.role == "WORKER") {
            roleTextView.text = "Freelancer"
            descriptionTextView.text = if (user.worker?.description.isNullOrEmpty()) "Belum ada deskripsi" else user.worker?.description
        } else {
            roleTextView.text = "Klien"
            descriptionTextView.text = if (user.client?.description.isNullOrEmpty()) "Belum ada deskripsi" else user.client?.description
        }

    }
}
