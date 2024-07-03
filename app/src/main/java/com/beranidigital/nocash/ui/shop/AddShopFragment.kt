package com.beranidigital.nocash.ui.shop

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.beranidigital.nocash.data.model.ShopModel
import com.beranidigital.nocash.databinding.FragmentAddShopBinding
import com.beranidigital.nocash.ui.main_navigation.MainHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AddShopFragment : Fragment() {
    private lateinit var binding: FragmentAddShopBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()


        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        initUI()
    }

    private fun addShopData(shop: ShopModel){
        CoroutineScope(Dispatchers.IO).launch {
                try {
                    database.getReference("shops").push().setValue(shop).await()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Success Create Toko data", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), MainHomeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Failed to create Toko data", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initUI(){
        binding.btnDaftarToko.setOnClickListener {
            val namaToko = binding.edtNameToko.text.toString().trim()
            val typeToko = binding.edtTypeToko.text.toString().trim()
            val alamatToko = binding.edtAlamatToko.text.toString().trim()

            val currentUser = auth.currentUser?.uid

            if(currentUser != null && namaToko.isNotEmpty() && typeToko.isNotEmpty() && alamatToko.isNotEmpty()){
                val shop = ShopModel(namaToko, typeToko, alamatToko, currentUser)
                addShopData(shop)
            }else{
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}