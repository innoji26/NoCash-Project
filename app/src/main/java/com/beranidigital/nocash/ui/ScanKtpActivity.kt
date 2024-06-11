package com.beranidigital.nocash.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.ActivityScanKtpBinding
import com.beranidigital.nocash.ui.identitas.DataIdentitasActivity
import com.beranidigital.nocash.ui.login.LoginActivity
import com.beranidigital.nocash.util.rotateBitmap
import com.beranidigital.nocash.util.uriToFile
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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

class ScanKtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanKtpBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanKtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Inisialisasi Firebase Storage dan Database
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        storageReference = storage.reference
        databaseReference = database.getReference("users")

//        auth = Firebase.auth
////        val firebaseUser = auth.currentUser

        binding.btnSave.setOnClickListener {
            uploadImageToFirebase()
        }

        binding.getFoto.setOnClickListener{
            choosePicture()
        }

//        if (!allPermissionGranted()) {
//            ActivityCompat.requestPermissions(
//                this,
//                REQUIRED_PERMISSIONS,
//                REQUEST_CODE_PERMISSIONS
//            )
//        }
    }

    private fun uploadImageToFirebase() {
        if (getFile != null) {
            val fileUri = Uri.fromFile(getFile)
            val fileReference = storageReference.child("images/${fileUri.lastPathSegment}")
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    fileReference.putFile(fileUri).await()
                    val downloadUrl = fileReference.downloadUrl.await()
                    saveUserProfile(downloadUrl.toString())
                } catch (e: Exception) {
                    showToast("Failed to upload image.")
                }
            }

        } else {
            showToast("No file selected.")
        }
    }

    private fun saveUserProfile(imageUrl: String) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val user = UsersModel(
                name = "",
                nik = "",
                gender = "",
                ttl = "",
                address = "",
                phone = auth.currentUser?.phoneNumber,
                email = auth.currentUser?.email,
                imageKtp = imageUrl,
                imageProfile = ""
            )
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    database.getReference("users").child(userId).setValue(user).await()
                    showToast("User profile saved.")
                    withContext(Dispatchers.Main) {
                        val newIntent =
                            Intent(this@ScanKtpActivity, DataIdentitasActivity::class.java)
                        startActivity(newIntent)
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
        }

        btnGalery.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
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
            binding.imgKtp.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.imgKtp.setImageURI(selectedImg)
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
    }
}