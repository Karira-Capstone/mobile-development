package com.capstone.karira.activity.auth

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.capstone.karira.component.SmallButton
import com.capstone.karira.data.AuthRepository
import com.capstone.karira.databinding.FragmentSelectSkillsBinding
import com.capstone.karira.ui.theme.KariraTheme
import com.dicoding.jetreward.ui.common.UiState


class SelectSkillsFragment : Fragment() {

    private var _binding: FragmentSelectSkillsBinding? = null
    private val binding get() = _binding!!
    private lateinit var authActivity: AuthActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectSkillsBinding.inflate(inflater, container, false)
        val view = binding.root

        authActivity = requireActivity() as AuthActivity

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBinding()
        handleAddSkillEnable()

    }

    private fun handleAddSkillEnable() {
        val inputText = binding.skillTextview.text

        binding.authButton.isEnabled = inputText != null && inputText.toString().isNotEmpty()
    }

    private fun handleBinding() {

        binding.skillTextview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handleAddSkillEnable()
            }
            override fun afterTextChanged(s: Editable) {}
        })

        binding.selectedSkills.setContent {
            authActivity.getUiState()
                .collectAsState(initial = UiState.Loading).value.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        authActivity.authViewModel.getUser()
                    }

                    is UiState.Success -> {
                        val data = uiState.data
                        Log.d("KONZZZZZ", data.skills)
                        val skills: List<String> = data.skills.split(";")
                        SelectedButtons(skills, authActivity)
                    }

                    is UiState.Error -> {}
                }
            }
        }

        binding.skillAdd.setOnClickListener {
            val inputText = binding.skillTextview.text.toString()
            authActivity.authViewModel.addUserSkills(inputText)
        }

        binding.authButton.setOnClickListener {
            // Backend.......

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectedButtons(skills: List<String>, activity: AuthActivity) {
    KariraTheme {
        Surface(
            color = Color.Transparent
        ) {
            if (skills.get(0) != "") {
                FlowRow(
                    modifier = Modifier
                        .padding(top = 24.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                    content = {
                        for (skill in skills) {
                            if (skill != "") SmallButton(text = skill, onClick = { activity.authViewModel.removeUserSkill(skill) })
                        }
                    }
                )
            } else {
                Text(
                    text = "Belum ada skill",
                    modifier = Modifier.padding(top = 24.dp),
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSelectedButtons() {
    SelectedButtons(
        skills = listOf(
            "Frontend Developer",
            "English",
            "ChakraUI",
            "MongoDB",
            "Spring Boot",
            "TensorFlow",
            "Tailwind CSS"
        ),
        activity = AuthActivity()
    )
}