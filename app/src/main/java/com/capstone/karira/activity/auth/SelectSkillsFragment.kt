package com.capstone.karira.activity.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils.split
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.activity.MainActivity
import com.capstone.karira.component.compose.SmallButton
import com.capstone.karira.data.local.StaticDatas
import com.capstone.karira.databinding.FragmentSelectSkillsBinding
import com.capstone.karira.model.Freelancer
import com.capstone.karira.model.Skills
import com.capstone.karira.model.User
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.ui.theme.KariraTheme
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.launch


class SelectSkillsFragment : Fragment() {

    private lateinit var userDataStore: UserDataStore
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
        handleButtonEnable()
        observeLiveData()

    }

    private fun handleButtonEnable(skills: List<String>? = null) {
        val inputText = binding.skillTextview.text

        binding.skillAdd.isEnabled = inputText.toString().isNotEmpty()

        if (skills != null) {
            binding.authButton.isEnabled = skills.isNotEmpty()
        }
    }

    private fun handleBinding() {

        binding.skillTextview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handleButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        val skills = StaticDatas.skills.toTypedArray()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, skills)
        binding.skillTextview.setAdapter(adapter)

        binding.selectedSkills.setContent {
            authActivity.getUiState()
                .collectAsState(initial = UiState.Loading).value.let { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            authActivity.getUser()
                        }

                        is UiState.Success -> {
                            val data = uiState.data as UserDataStore
                            val skills: List<String> = data.skills.split(";").filter { it != "" }
                            SelectedButtons(skills, authActivity)

                            handleButtonEnable(skills)
                        }

                        is UiState.Error -> {}
                        is UiState.Initiate -> {}
                    }
                }
        }

        binding.skillAdd.setOnClickListener {
            val skills = userDataStore.skills.split(";").filter { it != "" }
            val inputText = binding.skillTextview.text.toString()
            val id = (StaticDatas.skills.indexOf(inputText)+1).toString()

            if (id == "0") Toast.makeText(requireContext(), "$inputText tidak valid", Toast.LENGTH_SHORT)
            else if (skills.indexOf(inputText) != -1) Toast.makeText(requireContext(), "$inputText sudah diisi", Toast.LENGTH_SHORT)
            else authActivity.addUserSkill(inputText)

            binding.skillTextview.setText("", false)
        }

        binding.authButton.setOnClickListener {
            binding.authSkillLoading.visibility = View.VISIBLE

            lifecycleScope.launch {
                val skillsObjects = userDataStore.skills.split(";").filter { it != "" }.map { (StaticDatas.skills.indexOf(it))+1 }.map { Skills(it) }

                val response = authActivity.updateFreelancer(userDataStore.token, Freelancer(skills = ArrayList(skillsObjects)))
                authActivity.activateUser()

                val newerUserResponse = authActivity.authenticate(userDataStore.firebaseToken)
                val userDataStore = UserDataStore(
                    firebaseToken = userDataStore.firebaseToken,
                    token = newerUserResponse.token.toString(),
                    fullName = response.user?.fullName.toString(),
                    id = response.user?.id.toString(),
                    role = response.user?.role.toString(),
                    skills = userDataStore.skills,
                    isActivated = true
                )
                authActivity.saveUser(userDataStore)

                changePage()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeLiveData() {
        authActivity.getUserLiveData().observe(viewLifecycleOwner) { user ->
            userDataStore = user
        }
    }

    fun changePage() {
        val i = Intent(requireActivity(), MainActivity::class.java)
        startActivity(i)
        requireActivity().finish()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectedButtons(skills: List<String>, activity: AuthActivity) {
    KariraTheme {
        Surface(
            color = Color.Transparent
        ) {
            if (skills.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier
                        .padding(top = 24.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                    content = {
                        for (skill in skills) {
                            if (skill != "") SmallButton(
                                text = skill,
                                isClosable = true,
                                onClick = { activity.removeUserSkill(skill) })
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