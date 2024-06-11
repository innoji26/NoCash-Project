package com.beranidigital.nocash.ui.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentProfileBinding
import com.beranidigital.nocash.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavHost()

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        if(firebaseUser == null){
            //not signed in, launch the login activity
            val newIntent = Intent(requireContext(), LoginActivity::class.java)
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(newIntent)
            return
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnEdit.setOnClickListener {
            // TODO: navigate to edit user data profile
        }

        binding.contactUs.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_contactUsFragment)
        }

        binding.termCondition.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_termConditionFragment)
        }

        binding.privacyPolicy.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_privacyPolicyFragment)
        }

        binding.aboutUs.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_aboutUsFragment)
        }

        dialogLogout()
    }

    private fun dialogLogout(){
        binding.buttonLogout.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_logout)

            dialog.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.btnLogout).setOnClickListener {
                dialog.dismiss()
                signOut()
            }
            dialog.show()
        }
    }

    private fun initNavHost() {
        navController = NavHostFragment.findNavController(this)

    }

    private fun signOut() {
        auth.signOut()
        val newIntent = Intent(requireContext(), LoginActivity::class.java)
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(newIntent)
    }
}