package com.beranidigital.nocash.ui.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.ActivityLoginBinding
import com.beranidigital.nocash.ui.ScanKtpActivity
import com.beranidigital.nocash.ui.identitas.DataIdentitasActivity
import com.beranidigital.nocash.ui.main_navigation.MainHomeActivity
import com.beranidigital.nocash.ui.otp.OtpActivity
import com.beranidigital.nocash.ui.registrasi.RegistrasiActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //inisialisasi firebase auth
        firebaseAuth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("users")

        //Configure Google Sign in
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnLogin.setOnClickListener {
            val phoneNumber = binding.edtNoHp.text.toString().trim()

            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Masukkan nomor telepon", Toast.LENGTH_SHORT).show()
            } else {
                val formattedPhoneNumber = formatPhoneNumber(phoneNumber)
                sendVerificationCode(formattedPhoneNumber)
            }
        }

        binding.btnSignInGoogle.setOnClickListener {
            googleSignin()
        }

        val btnTextRegister = binding.btnTextRegister
        btnTextRegister.setOnClickListener {
            startActivity(Intent(this, RegistrasiActivity::class.java))
        }

        binding.btnverif.setOnClickListener{
            val number = "81214042128"
            openWhatsApp(number)
        }
    }

    private fun openWhatsApp(number: String) {
        try {
            // Buat intent dengan action view
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/+62$number")
            intent.setPackage("com.whatsapp")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error occurred while opening WhatsApp.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        return if (phoneNumber.startsWith("+62")) {
            phoneNumber
        } else {
            "+62" + phoneNumber.trimStart('0')
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Auto verification
            Log.d("LoginActivity", "onVerificationCompleted: $credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.e("LoginActivity", "onVerificationFailed", e)
            Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            Log.d("LoginActivity", "onCodeSent: verificationId: $verificationId, token: $token")
            val intent = Intent(this@LoginActivity, OtpActivity::class.java)
            val phoneNumber = binding.edtNoHp.text.toString().trim()
            val formattedPhoneNumber = formatPhoneNumber(phoneNumber)
            intent.putExtra("verificationId", verificationId)
            intent.putExtra("resendingToken", token)
            intent.putExtra("phoneNumber", formattedPhoneNumber)
            startActivity(intent)
            Toast.makeText(this@LoginActivity, "Kode OTP telah dikirim", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                Log.d("LoginActivity", "signInWithCredential:success")
                val user = authResult.user
                val userId = user?.uid ?: ""

                // Check if user has completed profile
                val userRef = databaseReference.child(userId)
                val snapshot = userRef.get().await()
                if (snapshot.exists()) {
                    val userModel = snapshot.getValue(UsersModel::class.java)
                    when {
                        userModel?.imageKtp.isNullOrEmpty() -> {
                            startActivity(Intent(this@LoginActivity, ScanKtpActivity::class.java))
                        }
                        userModel?.name.isNullOrEmpty() || userModel?.nik.isNullOrEmpty() ||
                                userModel?.gender.isNullOrEmpty() || userModel?.ttl.isNullOrEmpty() ||
                                userModel?.address.isNullOrEmpty() || userModel?.email.isNullOrEmpty() ||
                                userModel?.imageProfile.isNullOrEmpty()-> {
                            startActivity(Intent(this@LoginActivity, DataIdentitasActivity::class.java))
                        }
                        else -> {
                            startActivity(Intent(this@LoginActivity, MainHomeActivity::class.java))
                        }
                    }
                } else {
                    startActivity(Intent(this@LoginActivity, ScanKtpActivity::class.java))
                }
                finish()
            } catch (e: Exception) {
                Log.w("LoginActivity", "signInWithCredential:failure", e)
                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                val user = authResult.user
                val userId = user?.uid ?: ""

                // Check if user has completed profile
                val userRef = databaseReference.child(userId)
                val snapshot = userRef.get().await()
                if (snapshot.exists()) {
                    val userModel = snapshot.getValue(UsersModel::class.java)
                    when {
                        userModel?.imageKtp.isNullOrEmpty() -> {
                            startActivity(Intent(this@LoginActivity, ScanKtpActivity::class.java))
                        }
                        userModel?.name.isNullOrEmpty() || userModel?.nik.isNullOrEmpty() ||
                                userModel?.gender.isNullOrEmpty() || userModel?.ttl.isNullOrEmpty() ||
                                userModel?.address.isNullOrEmpty() || userModel?.email.isNullOrEmpty() ||
                                userModel?.imageProfile.isNullOrEmpty() -> {
                            startActivity(Intent(this@LoginActivity, DataIdentitasActivity::class.java))
                        }
                        else -> {
                            startActivity(Intent(this@LoginActivity, MainHomeActivity::class.java))
                        }
                    }
                } else {
                    startActivity(Intent(this@LoginActivity, ScanKtpActivity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                Log.w(TAG, "signInWithCredential:failure", e)
                updateUI(null)
            }
        }
    }

    private fun googleSignin() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                //google sign in was success auth with firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:"+ account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                //google sign in failed, update UI
                Log.w(TAG, "Google Sign in Failed", e)
            }
        }
    }



    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
            startActivity(Intent(this@LoginActivity, MainHomeActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        //check if user is signed in (non-null) and update UI
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    companion object{
        private const val TAG = "LoginActivity"
    }

}