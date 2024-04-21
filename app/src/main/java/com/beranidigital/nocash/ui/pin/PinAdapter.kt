package com.beranidigital.nocash.ui.pin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.beranidigital.nocash.R


class PinAdapter(private val context: Context, private val numbers: ArrayList<String>) : BaseAdapter() {
    override fun getCount(): Int = numbers.size

    override fun getItem(position: Int): Any = numbers[position]

    override fun getItemId(position: Int): Long = numbers[position].length.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = convertView
        when(numbers[position]){
            "delete", "done" -> {
                 view = inflater.inflate(R.layout.pin_action_button, parent, false)

                val buttonText = view.findViewById<ImageView>(R.id.pin_button)
                if(numbers[position] == "done"){
                    buttonText.setImageResource(R.drawable.ic_checklist)
                }
                else{
                    buttonText.setImageResource(R.drawable.ic_delete)
                }
            }
            else ->{
                 view = inflater.inflate(R.layout.pin_button, parent, false)

                val buttonText = view.findViewById<TextView>(R.id.pin_button)
                buttonText.text = numbers[position]
            }
        }

        return view
    }

}