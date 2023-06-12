package com.capstone.karira.activity.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.activity.MockupActivity
import com.capstone.karira.data.local.StaticDatas
import com.capstone.karira.data.remote.model.response.AuthenticateResponse
import com.capstone.karira.databinding.FragmentSelectRoleBinding
import com.capstone.karira.model.Client
import com.capstone.karira.model.Freelancer
import com.capstone.karira.model.UserDataStore
import kotlinx.coroutines.launch

class SelectRoleFragment : Fragment() {

    private var _binding: FragmentSelectRoleBinding? = null
    private val binding get() = _binding!!

    private lateinit var userDataStore: UserDataStore
    private lateinit var authActivity: AuthActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectRoleBinding.inflate(inflater, container, false)
        val view = binding.root

        authActivity = requireActivity() as AuthActivity

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.freelancerCard.setOnClickListener {
            createDialog("FREELANCER")
        }

        binding.clientCard.setOnClickListener {
            createDialog("CLIENT")
        }

        observeViewModel()

    }

    private fun createDialog(type: String) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Kamu akan memilih role $type")
        builder.setMessage(getString(R.string.selectrole_notes))

        builder.setPositiveButton("Simpan") { _, _ ->
            lifecycleScope.launch {
                try {
                    if (type == "FREELANCER") {
                        val response: Freelancer = authActivity.createFreelancer(userDataStore.token)
                        authActivity.addUserRole("WORKER")
                    } else {
                        val response: Client = authActivity.createClient(userDataStore.token)
                        authActivity.addUserRole("CLIENT")
                        val newerUserResponse = authActivity.authenticate(userDataStore.firebaseToken)
                        val userDataStore = UserDataStore(
                            firebaseToken = userDataStore.token,
                            token = newerUserResponse.token.toString(),
                            fullName = response.user?.fullName.toString(),
                            id = response.user?.id.toString(),
                            role = response.user?.role.toString()
                        )
                        authActivity.saveUser(userDataStore)
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        e.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        builder.setNegativeButton("Cek lagi") { dialogInterface, _ ->
            dialogInterface.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    private fun observeViewModel() {
        authActivity.getUserLiveData().observe(viewLifecycleOwner) { userLiveData ->
            userDataStore = userLiveData
            if (userDataStore.role == "WORKER") {
                view?.findNavController()?.navigate(R.id.action_selectRoleFragment_to_selectSkillsFragment)
            } else {
                val i = Intent(requireActivity(), MockupActivity::class.java)
                startActivity(i)
                requireActivity().finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}