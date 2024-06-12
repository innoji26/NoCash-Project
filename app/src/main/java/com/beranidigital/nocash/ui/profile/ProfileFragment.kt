package com.beranidigital.nocash.ui.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.FragmentProfileBinding
import com.beranidigital.nocash.ui.home.HomeFragment
import com.beranidigital.nocash.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference

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
        db = Firebase.database


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
        setUserData()
    }

    private fun setUserData(){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val userId = auth.uid ?: ""
                dbRef = db.getReference("users").child(userId)

                val snapshot = dbRef.get().await() // Menunggu hasil dari Firebase secara asinkron
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UsersModel::class.java)
                    if (user != null) {
                        binding.profileName.text = user.name
                    }
                } else {
                    Log.w(TAG, "Data tidak ditemukan")
                    Toast.makeText(requireContext(), "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.w(TAG, "ProfileFragment:failure", e)
                Toast.makeText(requireContext(), "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
            }
        }
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

    companion object{
        private const val TAG = "ProfileFragment"
    }
}