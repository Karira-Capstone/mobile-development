package com.capstone.karira.activity.auth

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.databinding.FragmentSelectRoleBinding
import com.capstone.karira.model.User

class SelectRoleFragment : Fragment() {

    private var _binding: FragmentSelectRoleBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: User
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

        binding.clientCard.setOnClickListener {
            createDialog("Client")
        }

        binding.ownerCard.setOnClickListener {
            createDialog("Owner")
        }

        observeViewModel()

    }

    private fun createDialog(type: String) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Kamu akan memilih role $type")
        builder.setMessage(getString(R.string.selectrole_notes))

        builder.setPositiveButton("Simpan") { _, _ ->
            if (type == "Client") {
//                view?.findNavController()?.navigate(R.id.action_selectRoleFragment_to_selectSkillsFragment)
                authActivity.addUserRole("Client")
            } else {
                authActivity.addUserRole("Client")
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
            user = userLiveData
            if (user.role == "Client") {
                view?.findNavController()?.navigate(R.id.action_selectRoleFragment_to_selectSkillsFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}