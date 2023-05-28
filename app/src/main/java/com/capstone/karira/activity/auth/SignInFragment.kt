package com.capstone.karira.activity.auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.capstone.karira.R
import com.capstone.karira.databinding.FragmentSelectRoleBinding
import com.capstone.karira.databinding.FragmentSignInBinding
import com.capstone.karira.model.Role
import com.capstone.karira.model.User
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var authActivity: AuthActivity
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var savedView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        // Initialize Firebase Auth
        auth = Firebase.auth

        authActivity = requireActivity() as AuthActivity

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedView = view

        observeViewModel()

        binding.authButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }
    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val mUser = auth.currentUser
                    mUser!!.getIdToken(true)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val idToken: String? = task.result.token
                                Log.v("TOKEN", idToken.toString())
                                // Send token to your backend via HTTPS
                                // ...

                                val user = User(idToken.toString(), "", "", "List")
                                authActivity.saveUser(user)
                            } else {
                                // Handle error -> task.getException();
                            }
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null, null)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateUI(currentUser: FirebaseUser?, user: User?) {
        if (currentUser != null && user != null &&  user.role != "") {
            savedView.findNavController().navigate(R.id.action_signInFragment_to_selectSkillsFragment)
        } else if (currentUser != null) {
            savedView.findNavController().navigate(R.id.action_signInFragment_to_selectRoleFragment)
        } else {
            binding.authLoading.visibility = View.GONE
            binding.authSigninCenter.visibility = View.VISIBLE
            binding.authSigninButton.visibility = View.VISIBLE
        }
    }

    private fun observeViewModel() {
        authActivity.authViewModel.getUserLiveData().observe(viewLifecycleOwner) { user ->
            val currentUser = auth.currentUser
            updateUI(currentUser, user)
        }
    }

    override fun onStart() {
        super.onStart()

//        observeViewModel()
    }

    companion object {
        private const val TAG = "SignInFragment"
    }

}