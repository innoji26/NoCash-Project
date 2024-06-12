package com.beranidigital.nocash.ui.profile.editProfil

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.ActivityEditProfilBinding
import com.beranidigital.nocash.ui.CameraActivity
import com.beranidigital.nocash.ui.ScanKtpActivity
import com.beranidigital.nocash.ui.identitas.DataIdentitasActivity
import com.beranidigital.nocash.ui.profile.ProfileActivity
import com.beranidigital.nocash.ui.profile.ProfileFragment
import com.beranidigital.nocash.util.rotateBitmap
import com.beranidigital.nocash.util.uriToFile
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

class EditProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfilBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var getFile: File? = null
    private var imgKtpUri = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        storageReference = storage.reference
        databaseReference = database.getReference("users")

        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        binding.imgProfile.setOnClickListener {
            choosePicture()
        }
        binding.button.setOnClickListener {
            uploadImageToFirebase()
        }

        setUserData()
    }

    private fun setUserData(){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val userId = auth.uid ?: ""
                databaseReference = database.getReference("users").child(userId)

                val snapshot = databaseReference.get().await() // Menunggu hasil dari Firebase secara asinkron
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UsersModel::class.java)
                    if (user != null) {
                        binding.layoutNama.editText?.setText(user.name)
                        binding.layoutNik.editText?.setText(user.nik)
                        binding.layoutGender.editText?.setText(user.gender)
                        binding.layoutTtl.editText?.setText(user.ttl)
                        binding.layoutAlamat.editText?.setText(user.address)
                        binding.layoutPhone.editText?.setText(user.phone)
                        binding.layoutEmail.editText?.setText(user.email)
                        imgKtpUri = user.imageKtp ?: ""
                        Log.w(TAG, imgKtpUri)
                        Glide.with(binding.imgProfile).load(user.imageProfile).into(binding.imgProfile)
                    }
                } else {
                    Log.w(TAG, "Data tidak ditemukan")
                    Toast.makeText(this@EditProfilActivity, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.w(TAG, "ProfileFragment:failure", e)
                Toast.makeText(this@EditProfilActivity, "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToFirebase() {
        if (getFile != null) {
            val fileUri = Uri.fromFile(getFile)
            val fileReference = storageReference.child("images/${fileUri.lastPathSegment}")
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    fileReference.putFile(fileUri).await()
                    val downloadUrl = fileReference.downloadUrl.await()
                    saveUserData(downloadUrl.toString())
                    intent.putExtra("imageUri", downloadUrl)
                } catch (e: Exception) {
                    showToast("Failed to upload image.")
                }
            }

        } else {
            showToast("No file selected.")
        }
    }

    private fun saveUserData(imageUrl: String){
        val name = binding.edtNama.text.toString().trim()
        val nik = binding.edtNik.text.toString().trim()
        val gender = binding.edtGender.text.toString().trim()
        val ttl = binding.edtTtl.text.toString().trim()
        val address = binding.edtAlamat.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()

        if (name.isEmpty() || nik.isEmpty() || gender.isEmpty() || ttl.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid

        if (userId != null) {
            val user = UsersModel(
                name,
                nik,
                gender,
                ttl,
                address,
                phone,
                email,
                imgKtpUri,
                imageUrl
            )
            CoroutineScope(Dispatchers.Main ).launch {
                try {
                    database.getReference("users").child(userId).setValue(user).await()
                    showToast("User profile saved.")
                    withContext(Dispatchers.Main) {
                        val newIntent =
                            Intent(this@EditProfilActivity, ProfileActivity::class.java)
                        startActivity(newIntent)
                        finish()
                    }
                } catch (e: Exception) {
                    showToast("Failed to save user profile.")
                }
            }
        }else{
            showToast("User not logged in.")
        }
    }



    private fun choosePicture(){
        val builder = AlertDialog.Builder(this).create()
        val view = layoutInflater.inflate(R.layout.alert_dialog_picture, null)
        builder.setCancelable(true)
        builder.setView(view)

        val btnCamera = view.findViewById<CardView>(R.id.cvCamera)
        val btnGalery = view.findViewById<CardView>(R.id.cvGalery)

        builder.show()

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        btnCamera.setOnClickListener{
            launcherIntentCameraX.launch(Intent(this, CameraActivity::class.java))
            builder.dismiss()
        }

        btnGalery.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
            builder.dismiss()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)

            getFile = myFile
            binding.imgProfile.setImageBitmap(result)

        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.imgProfile.setImageURI(selectedImg)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(!allPermissionGranted()){
                showToast("Tidak mendapatkan permission.")

            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val TAG = "EditProfileActivity"
    }
}