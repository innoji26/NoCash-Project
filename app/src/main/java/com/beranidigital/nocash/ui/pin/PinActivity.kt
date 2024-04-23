package com.beranidigital.nocash.ui.pin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityPinBinding
import com.beranidigital.nocash.ui.complete_register.CompleteRegiterActivity
import com.beranidigital.nocash.ui.main_navigation.MainHomeActivity

class PinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val pinValue = binding.etPin

        val numbers =
            arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "delete", "0", "done")
        val gridAdapterNumber = PinAdapter(this, numbers)
        val pinNumber = binding.gridPinNumber
        // disable gridview scrolling
        pinNumber.setOnTouchListener { _, event ->
            event.action == MotionEvent.ACTION_MOVE
        }
        pinNumber.setAdapter(gridAdapterNumber)
        pinNumber.setOnItemClickListener { _, _, position, _ ->
            Log.d("PinActivity", "position: ${numbers[position]}")
            when (numbers[position]) {
                "done" -> {
                    val text = pinValue.text.toString()
                    if (text.isNotEmpty() && text.length == 6) {
                        val newIntent : Intent
                        val type = intent.getSerializableExtra("type") as PinType
                        if (type == PinType.CREATE) {
                            newIntent = Intent(this, CompleteRegiterActivity::class.java)
                        } else {
                            newIntent = Intent(this, MainHomeActivity::class.java)
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        }
                        startActivity(newIntent)
                    } else {
                        Log.d("PinActivity", "PIN is empty : $text")
                        Toast.makeText(this, "Harap isi PIN terlebih dahulu", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                "delete" -> {
                    if (pinValue.text.isNotEmpty()) {
                        pinValue.text.delete(pinValue.text.length - 1, pinValue.text.length)
                    }
                }

                else -> {
                    if (pinValue.text.length <= 6) {
                        pinValue.append(numbers[position])
                    }
                }
            }
        }

        val intent: PinType = intent.getSerializableExtra("type") as PinType
        val title = binding.tvTitle

        Log.d("PinActivity", "intent: $intent")
        when (intent) {
            PinType.CREATE -> {
                binding.toolbar.title = resources.getString(R.string.step, 3, 3)
                title.text = resources.getString(R.string.title_pin, "Buatlah")
            }

            PinType.VERIFY -> {
                title.text = resources.getString(R.string.title_pin, "Masukkan")
            }
        }
    }
}
