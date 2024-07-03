package com.beranidigital.nocash.ui.hutangPiutang.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.DebtsModel
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.ActivityDetailHutangBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class DetailHutangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHutangBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHutangBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        auth = Firebase.auth
        db = Firebase.database
        dbRef = FirebaseDatabase.getInstance().reference

        fetchHutangData()
    }

    private fun fetchHutangData(){
        val getDebtId = intent.getStringExtra(EXTRA_ID_DEBTS_HUTANG)!!
        CoroutineScope(Dispatchers.Main).launch {
            try {
                dbRef = db.getReference("debts").child(getDebtId)

                val snapshot = dbRef.get().await()
                if(snapshot.exists()){
                    val debt = snapshot.getValue(DebtsModel::class.java)
                    debt?.let {
                        val sisaHutang = (it.amount.toInt() - (it.totalPaid?.toInt() ?: 0)).toString()
                        binding.tvHutangId.text = getDebtId
                        binding.tvStatusHutang.text = it.status
                        binding.tvTotalHutang.text = it.amount
                        binding.tvDeskripsi.text = it.description
                        binding.tvSisaHutang.text = sisaHutang

                        fetchUserData(it.creditorId) { user ->
                            binding.tvNamePenjual.text = user.name
                            binding.tvNomoWhatsapp.text = user.phone
                            binding.btnChatWhatsapp.setOnClickListener {
                                val formattedPhone = user.phone?.let { it1 -> formatPhoneNumber(it1) }
                                val message = "Hello, saya ${user.name}, ingin membayar hutang sebesar $sisaHutang"
                                val linkWa = "http://wa.me/$formattedPhone?text=${URLEncoder.encode(message, "UTF-8")}"
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(linkWa)))
                            }
                        }

                        val createdAtFormatted = SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(Date(it.createdAt))
                        val relativeTimeSpan = DateUtils.getRelativeTimeSpanString(it.createdAt, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE)
                        binding.tvDateHutang.text = "$createdAtFormatted ($relativeTimeSpan)"

                        val jatuhtempo = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.dueDate)
                        jatuhtempo?.let { dueDate ->
                            val dueDateFormatted = SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(dueDate)
                            val daysUntilDue = calculateDaysUntilDue(dueDate)
                            val daysUntilDueAbs = Math.abs(daysUntilDue)

                            binding.tvDateJatuhtempo.text = when {
                                daysUntilDue > 0 -> "$dueDateFormatted ($daysUntilDueAbs hari lagi)"
                                daysUntilDue == 0 -> "$dueDateFormatted (hari ini)"
                                else -> "$dueDateFormatted (lewati $daysUntilDueAbs hari)"
                            }
                        }
                    }

                }
            }catch (e: Exception){
                Log.e("DetailHutangActivity", "DetailHutang:failure", e)
                Toast.makeText(this@DetailHutangActivity, "Gagal memuat data Hutang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserData(userId: String, callback: (UsersModel) -> Unit) {
        val userRef = db.getReference("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UsersModel::class.java)
                if (user != null) {
                    callback(user)
                } else {
                    Toast.makeText(this@DetailHutangActivity, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailHutangActivity, "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun formatPhoneNumber(phone: String): String {
        return when {
            phone.startsWith("0") -> "+62" + phone.substring(1)
            phone.startsWith("62") -> "+$phone"
            phone.startsWith("+62") -> phone
            else -> "+62$phone"
        }
    }

    fun calculateDaysUntilDue(dueDate: Date): Int {
        val today = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(
            Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }.time
        val diff = dueDate.time - today.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
    }

    companion object{
        const val EXTRA_ID_DEBTS_HUTANG = "extra_id_debts_hutang"
    }


}